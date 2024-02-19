package com.darkpattern.detection.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.darkpattern.detection.CommunityPost;
import com.darkpattern.detection.ComplaintResult;
import com.darkpattern.detection.R;
import com.darkpattern.detection.Register;
import com.darkpattern.detection.model.Complaint;
import com.darkpattern.detection.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class ComplaintFragment extends Fragment {

    private TextInputEditText complaint_box1,complaint_box2,complaint_box3;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private Button Complaint_Reg;
    private String unikey;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_complaint, container, false);

        complaint_box1 = view.findViewById(R.id.complaintBox1);
        complaint_box2 = view.findViewById(R.id.complaintBox2);
        complaint_box3 =view.findViewById(R.id.complaintBox3);
        Complaint_Reg = view.findViewById(R.id.ComplaintBtn);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        unikey = databaseReference.child("Complaints").push().getKey();

        Complaint_Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()){
                    saveUserDataToFirestore();
                    Intent intent = new Intent(getContext(), ComplaintResult.class);
                    startActivity(intent);

                }
            }
        });


        return view;
    }

    private void saveUserDataToFirestore() {
        String complaint1 = Objects.requireNonNull(complaint_box1.getText()).toString().trim();
        String complaint_url = Objects.requireNonNull(complaint_box2.getText()).toString().trim();
        String complaint_dis = Objects.requireNonNull(complaint_box3.getText()).toString().trim();


        // Create a User object
        Complaint complaint = new Complaint(unikey,complaint1,complaint_url,complaint_dis);
        // Save the user data to Firestore
        databaseReference.child("Complaints").child(unikey).setValue(complaint)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Complaint Registered Successfully", Toast.LENGTH_SHORT).show();
                    // If needed, you can also navigate to a new activity or perform other actions here
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(getContext(), "Complaint Registration Failed", Toast.LENGTH_SHORT).show();
                });
    }
    private boolean validateInputs() {
        String complaint1 = Objects.requireNonNull(complaint_box1.getText()).toString().trim();
        String complaint_url = Objects.requireNonNull(complaint_box2.getText()).toString().trim();
        String complaint_dis = Objects.requireNonNull(complaint_box3.getText()).toString().trim();

        if (complaint1.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Complaint Message", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (complaint_url.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Complaint Url", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (complaint_dis.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Complaint Discription", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}