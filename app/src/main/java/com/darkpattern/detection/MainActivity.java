package com.darkpattern.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.darkpattern.detection.Fragments.CommunityFragment;
import com.darkpattern.detection.Fragments.ComplaintFragment;
import com.darkpattern.detection.Fragments.UploadFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    LinearLayout C_post;

    TextView tittle_toolbar;

//    private Interpreter interpreter;
    Toolbar toolbar;

    ConstraintLayout constraintLayout;
    NavigationView navigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        C_post = findViewById(R.id.post);
        navigationView = findViewById(R.id.navigation_View);
        View headerView = navigationView.getHeaderView(0);
        constraintLayout = findViewById(R.id.constraintLayout);
        toolbar = findViewById(R.id.toolbar);
        tittle_toolbar = findViewById(R.id.toolbar_title);


        C_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CommunityPost.class);
                startActivity(intent);
            }
        });



        replaceFragment(new UploadFragment());
        C_post.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("fragment_to_load")) {
            String fragmentToLoad = intent.getStringExtra("fragment_to_load");
            if (fragmentToLoad != null && fragmentToLoad.equals("complaint")) {
                replaceFragment(new ComplaintFragment());
                tittle_toolbar.setText("Complaint");
                C_post.setVisibility(View.INVISIBLE);
            }
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = db.collection("users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get user data
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String imageUrl = document.getString("image"); // Assuming "image" is the field for the image URL

                        CircleImageView profileImg = headerView.findViewById(R.id.profileimg);
                        TextView profileName = headerView.findViewById(R.id.profilename);
                        TextView profileEmail = headerView.findViewById(R.id.profileEmail_header);

                        // Load the image into the profile_image CircleImageView using Picasso
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).into(profileImg, new Callback() {
                                @Override
                                public void onSuccess() {
                                    // Image loaded successfully
                                }

                                @Override
                                public void onError(Exception e) {
                                    // Handle error, for example, show a placeholder image
                                    profileImg.setImageResource(R.drawable.user_profile_icon);
                                }
                            });
                        } else {
                            // No image URL provided, show a placeholder image
                            profileImg.setImageResource(R.drawable.user_profile_icon);
                        }

                        // Set data to TextViews
                        profileName.setText(name);
                        profileEmail.setText(email);
                    }  // Handle the case when the document does not exist

                }  // Handle exceptions

            });
        }



        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigation();



        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.upload) {
                replaceFragment(new UploadFragment());
                tittle_toolbar.setText("Upload");
                C_post.setVisibility(View.INVISIBLE);
            } else if (item.getItemId() == R.id.complaint) {
                replaceFragment(new ComplaintFragment());
                tittle_toolbar.setText("Complaint");
                C_post.setVisibility(View.INVISIBLE);
            } else if (item.getItemId() == R.id.community) {
                replaceFragment(new CommunityFragment());
                tittle_toolbar.setText("Community");
                C_post.setVisibility(View.VISIBLE);
            }
            return true;
        });
    }

    // Method to load TensorFlow Lite model file
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("tflite.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
    private void navigation() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.track) {
            Intent intent = new Intent(getApplicationContext(),TrackinComplaint.class);
            startActivity(intent);

        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(getApplicationContext(), EditProfile.class);
            startActivity(intent);

        } else if (itemId == R.id.link) {
            Intent intent = new Intent(getApplicationContext(),LinkAccounts.class);
            startActivity(intent);

        }else if (itemId == R.id.refer) {


        }else if (itemId == R.id.why) {
            Intent intent = new Intent(getApplicationContext(),WhyThisApp.class);
            startActivity(intent);

        }else if (itemId == R.id.logout) {
            showLogoutConfirmationDialog();

        }
        closeDrawer();
        return true;
    }

    private void showLogoutConfirmationDialog() {
        // Create a confirmation dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Logout Confirmation");
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            // User clicked Yes, perform logout
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });
        alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> {
            // User clicked No, dismiss the dialog
            dialogInterface.dismiss();
        });

        // Show the confirmation dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void closeDrawer() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Get the current fragment
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);

            // Check if the current fragment is HomeFragment
            if (currentFragment instanceof UploadFragment) {
                // Handle the back press in HomeFragment, e.g., navigate to the previous fragment or activity
                // If you want to go back to the previous fragment, you can use the popBackStack method
                getSupportFragmentManager().popBackStack();
            } else {
                // If the current fragment is not HomeFragment, perform the default behavior of onBackPressed
                super.onBackPressed();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        toggle.onOptionsItemSelected(item);
        return true;
    }

}
