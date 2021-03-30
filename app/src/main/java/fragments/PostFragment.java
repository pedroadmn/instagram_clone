package fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import helpers.Permission;
import pedroadmn.instagramclone.com.R;

public class PostFragment extends Fragment {

    private Button btOpenGallery;
    private Button btOpenCamera;

    private static final int GALLERY_SELECTION = 100;
    private static final int CAMERA_SELECTION = 200;

    private String[] permissions = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public PostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        initializeComponents(view);

        Permission.validatePermissions(permissions, getActivity(), 1);

        return view;
    }

    private void initializeComponents(View view) {
        btOpenGallery = view.findViewById(R.id.btOpenGallery);
        btOpenCamera = view.findViewById(R.id.btOpenCamera);

        btOpenGallery.setOnClickListener(v -> openGallery());
        btOpenCamera.setOnClickListener(v -> openCamera());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, GALLERY_SELECTION);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_SELECTION);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        for(int permissionResult : grantResults) {
//            if (permissionResult == PackageManager.PERMISSION_DENIED) {
//                warningPermissionValidation();
//            }
//        }
//    }
//
//    private void warningPermissionValidation() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Denied Permissions");
//        builder.setMessage("To use the app is necessary accept the permissions");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Confirm", (dialogInterface, i) -> getActivity().finish());
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == getActivity().RESULT_OK) {
//            Bitmap bitmap = null;
//            try {
//                switch (requestCode) {
//                    case CAMERA_SELECTION:
//                        bitmap = (Bitmap) data.getExtras().get("data");
//                        break;
//                    case GALLERY_SELECTION:
//                        Uri selectedImageLocal = data.getData();
//                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageLocal);
//                        break;
//                }
//
//                if (bitmap != null) {
//                    civProfileImage.setImageBitmap(bitmap);
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
//                    byte[] imageData = baos.toByteArray();
//
//                    final StorageReference imageRef = storageReference
//                            .child("images")
//                            .child("perfil")
//                            .child(userId + ".jpeg");
//
//                    UploadTask uploadTask = imageRef.putBytes(imageData);
//                    uploadTask.addOnFailureListener(e -> {
//                        Toast.makeText(getActivity(), "Error on upload image", Toast.LENGTH_SHORT).show();
//                    })
//                            .addOnSuccessListener(taskSnapshot -> {
//                                Toast.makeText(getActivity(), "Photo Successfully uploaded", Toast.LENGTH_SHORT).show();
//                                imageRef.getDownloadUrl().addOnCompleteListener(task -> {
//                                    Uri url = task.getResult();
//                                    updateUserPhoto(url);
//                                });
//                            });
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//    }
}