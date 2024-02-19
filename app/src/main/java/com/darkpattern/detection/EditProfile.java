package com.darkpattern.detection;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    Uri selectedImageUri;

    ActivityResultLauncher<Intent> imagePickerLauncher;
    private CircleImageView manage_profile;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    private String userId;
    private TextInputEditText edit_name;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button update = findViewById(R.id.Update_btn);
        mAuth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        ImageView back_btn = findViewById(R.id.backbtn);
        edit_name = findViewById(R.id.manage_name);
        manage_profile = findViewById(R.id.manage_profile);
        progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Updating Profile...");
        progressDialog.setCancelable(false);

        manage_profile.setOnClickListener((view -> ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512).createIntent(intent1 -> {
            imagePickerLauncher.launch((intent1));
            return null;
        })));

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();

                    // Corrected the variable name here from imageUri to selectedImageUri
                    Glide.with(getApplicationContext())
                            .load(selectedImageUri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(manage_profile);
                }
            }
        });
        back_btn.setOnClickListener(view -> onBackPressed());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    updateUserData();
                }


            }

            private void updateUserData() {
                progressDialog.show();

                String name = Objects.requireNonNull(edit_name.getText()).toString().trim();


                // Check if a new image is selected
                if (selectedImageUri == null) {
                    // If no new image is selected, fetch the existing image URL
                    firestore.collection("users").document(userId)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String existingImageUrl = document.getString("image");
                                        // Update the user data in Firestore
                                        updateUserDataInFirestore(existingImageUrl, name);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditProfile.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfile.this, "Error fetching document", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // If a new image is selected, proceed with image upload and update user data
                    StorageReference reference = storage.getReference().child("Profiles").child(userId);
                    reference.putFile(selectedImageUri).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                // Update the user data in Firestore
                                updateUserDataInFirestore(imageUrl, name);
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "Error getting download URL", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private void updateUserDataInFirestore(String imageUrl, String name) {
                firestore.collection("users").document(userId)
                        .update("image", imageUrl, "name", name)
                        .addOnCompleteListener(task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                Intent intent13 = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent13);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                            }
                        });
            }


            private boolean validateInputs() {
                String name = Objects.requireNonNull(edit_name.getText()).toString().trim();

                if (TextUtils.isEmpty(name)) {
                    edit_name.setError("Name is Required.");
                    progressDialog.dismiss();
                    return false;
                }
                return true;

            }
        });

    }


}