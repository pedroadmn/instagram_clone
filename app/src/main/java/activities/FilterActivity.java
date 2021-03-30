package activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import pedroadmn.instagramclone.com.R;

public class FilterActivity extends AppCompatActivity {

    private ImageView ivSelectedImage;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = findViewById(R.id.idToolbar);
        toolbar.setTitle("Filters");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.publishMenu:
                publish();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void publish() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}