package com.darkpattern.detection.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.darkpattern.detection.Entertext;
import com.darkpattern.detection.R;
import com.darkpattern.detection.SelectImage;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class UploadFragment extends Fragment {

    private CardView select_image;
    private CardView enter_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        select_image = view.findViewById(R.id.selectImageCV);
        enter_text = view.findViewById(R.id.enterTextCV);

        select_image.setOnClickListener(view1 -> {
            Intent intent =new Intent(getContext(), SelectImage.class);
            startActivity(intent);
        });

        enter_text.setOnClickListener(view12 -> {
            Intent intent =new Intent(getContext(), Entertext.class);
            startActivity(intent);
        });

        return view;
    }
}
