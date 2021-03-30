package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import pedroadmn.instagramclone.com.R;

public class FilterActivity extends AppCompatActivity {

    private ImageView ivSelectedImage;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initializeComponents();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] imageData = bundle.getByteArray("selectedImage");
            image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            ivSelectedImage.setImageBitmap(image);
        }
    }

    private void initializeComponents() {
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
    }
}