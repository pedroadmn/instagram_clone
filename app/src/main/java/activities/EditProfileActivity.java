package activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import models.User;
import pedroadmn.instagramclone.com.R;

public class EditProfileActivity extends AppCompatActivity {

    private static final int GALLERY_SELECTION = 200;

    private CircleImageView cvEditProfilePhoto;
    private EditText etEditProfileName;
    private EditText etEditProfileEmail;
    private Button btEditProfileUpdateChanges;
    private TextView tvChangeProfilePhoto;
    private ProgressBar progressEditProfile;

    private User loggedUser;
    private String userId;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.idToolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        initializeComponents();

        FirebaseUser user = FirebaseUserHelper.getCurrentUser();
        etEditProfileName.setText(user.getDisplayName());
        etEditProfileEmail.setText(user.getEmail());

        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(cvEditProfilePhoto);
        }
    }

    private void initializeComponents() {
        cvEditProfilePhoto = findViewById(R.id.cvEditProfilePhoto);
        etEditProfileName = findViewById(R.id.etEditProfileName);
        etEditProfileEmail = findViewById(R.id.etEditProfileEmail);
        progressEditProfile = findViewById(R.id.progressEditProfile);
        btEditProfileUpdateChanges = findViewById(R.id.btEditProfileUpdateChanges);
        tvChangeProfilePhoto = findViewById(R.id.tvChangeProfilePhoto);

        btEditProfileUpdateChanges.setOnClickListener(v -> updateChanges());
        tvChangeProfilePhoto.setOnClickListener(v -> openGalleryAndChoosePhoto());

        etEditProfileEmail.setFocusable(false);

        userId = FirebaseUserHelper.getUserId();
        loggedUser = FirebaseUserHelper.getLoggedUserInfo();

        auth = FirebaseConfig.getAuthFirebase();
        storageReference = FirebaseConfig.getFirebaseStorage();
    }

    private void updateChanges() {
        progressEditProfile.setVisibility(View.VISIBLE);
        String updatedName = etEditProfileName.getText().toString();

        FirebaseUserHelper.updateUsername(updatedName);

        loggedUser.setName(updatedName);
        loggedUser.setSearchName(updatedName.toLowerCase());
        loggedUser.update();

        Toast.makeText(this, "Successfully updated changes!", Toast.LENGTH_SHORT).show();
        progressEditProfile.setVisibility(View.INVISIBLE);
    }

    private void openGalleryAndChoosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, GALLERY_SELECTION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            progressEditProfile.setVisibility(View.VISIBLE);
            Bitmap bitmap = null;
            try {
                switch (requestCode) {
                    case GALLERY_SELECTION:
                        Uri selectedImageLocal = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageLocal);
                        break;
                }

                if (bitmap != null) {
                    cvEditProfilePhoto.setImageBitmap(bitmap);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] imageData = baos.toByteArray();

                    final StorageReference imageRef = storageReference
                            .child("images")
                            .child("perfil")
                            .child(userId + ".jpeg");

                    UploadTask uploadTask = imageRef.putBytes(imageData);
                    uploadTask.addOnFailureListener(e -> {
                        Toast.makeText(EditProfileActivity.this, "Error on upload image", Toast.LENGTH_SHORT).show();
                        progressEditProfile.setVisibility(View.INVISIBLE);
                    })
                            .addOnSuccessListener(taskSnapshot -> {
                                Toast.makeText(EditProfileActivity.this, "Photo Successfully uploaded", Toast.LENGTH_SHORT).show();
                                imageRef.getDownloadUrl().addOnCompleteListener(task -> {
                                    Uri url = task.getResult();
                                    updateUserPhoto(url);
                                    progressEditProfile.setVisibility(View.INVISIBLE);
                                })
                                .addOnFailureListener(error -> {
                                    progressEditProfile.setVisibility(View.INVISIBLE);
                                });
                            });
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                progressEditProfile.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updateUserPhoto(Uri url) {
        boolean result = FirebaseUserHelper.updateUserPhoto(url);

        if (result) {
            loggedUser.setPhotoPath(url.toString());
            loggedUser.update();

            String photoPath =  loggedUser.getPhotoPath();

            if (photoPath != null) {
                Uri uri = Uri.parse(photoPath);
                Glide.with(EditProfileActivity.this).load(uri).into(cvEditProfilePhoto);
            }

            Toast.makeText(EditProfileActivity.this, "The photo was updated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}