package com.darkpattern.detection;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SelectImage extends AppCompatActivity {

    private ImageView selected_photo;
    Uri selectedImageUri;
    TextRecognizer textRecognizer;
    private Interpreter interpreter;

    FirebaseStorage storage;
    StorageReference reference;
    ProgressDialog progressDialog;

    ActivityResultLauncher<Intent> imagePickerLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);

        // Load the TensorFlow Lite model
        try {
            interpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        selected_photo = findViewById(R.id.selectedImage);
        Button get_result = findViewById(R.id.uploadTxtBtn);
        ImageButton back = findViewById(R.id.backbtn);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        selected_photo.setOnClickListener(view -> ImagePicker.with(this).createIntent(intent1 -> {
            imagePickerLauncher.launch(intent1);
            return null;
        }));

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();

                    Glide.with(getApplicationContext())
                            .load(selectedImageUri)
                            .into(selected_photo);
                }
            }
        });

        back.setOnClickListener(view -> onBackPressed());

        get_result.setOnClickListener(view -> {
            if(validateInput()){
                reconizeTxt();
            }
        });
    }

    private boolean validateInput() {
        if (selectedImageUri == null || selectedImageUri.toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Select an Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void reconizeTxt() {
        if(selectedImageUri!=null){
            try {
                InputImage inputImage = InputImage.fromFilePath(SelectImage.this,selectedImageUri);

                Task<Text> result = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        String recognizedText = text.getText();
                        performModelInference(recognizedText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Load the TensorFlow Lite model file from assets
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetManager assetManager = getAssets();
        AssetFileDescriptor fileDescriptor = assetManager.openFd("tflite.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Perform inference with TensorFlow Lite model
    private void performModelInference(String recognizedText) {
        // Placeholder method for text encoding
        float[] inputData = encodeText(recognizedText);

        // Perform inference
        int inputSize = inputData.length;

        // Get the input tensor information from the TensorFlow Lite model
        int inputTensorIndex = 0; // Assuming the input tensor index is 0
        int[] inputTensorShape = interpreter.getInputTensor(inputTensorIndex).shape();
        int expectedInputSize = inputTensorShape[1]; // Assuming the input size is the second dimension

        // Ensure the input size matches the expected input size of the model
        if (inputSize == expectedInputSize) {
            // Continue with inference
            int outputSize = 1; // Assuming the model has a single output
            float[][] output = new float[1][outputSize];
            interpreter.run(inputData, output);

            // Process model output if needed
            float outputResult = output[0][0];

            // Handle model output
            handleModelOutput(outputResult);
        } else {
            // Handle mismatched input size
            Toast.makeText(this, "Input size mismatch: " + inputSize + " (expected " + expectedInputSize + ")", Toast.LENGTH_SHORT).show();
        }
    }



    // Placeholder method for text encoding
    private float[] encodeText(String recognizedText) {
        // Placeholder implementation
        // Perform text encoding here
        float[] numericalInput = new float[recognizedText.length()];
        // Example: Convert each character to its ASCII value
        for (int i = 0; i < recognizedText.length(); i++) {
            numericalInput[i] = (float) recognizedText.charAt(i);
        }
        return numericalInput;
    }

    // Placeholder method for handling model output
    private void handleModelOutput(float outputResult) {
        // Placeholder implementation
        // Handle model output here
        Toast.makeText(this, "Model output: " + outputResult, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),ResultImage.class);
        intent.putExtra("text",outputResult);
        startActivity(intent);
    }

    private void uploadImage() {
        if (selectedImageUri != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please Wait!");
            progressDialog.setMessage("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference ref = reference.child("TnC Images/image");

            ref.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploading..." + (int) progress + "%");
            });
        }
    }
}
