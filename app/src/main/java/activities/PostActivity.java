package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import models.Post;
import models.User;
import pedroadmn.instagramclone.com.R;

public class PostActivity extends AppCompatActivity {

    private CircleImageView civPostUser;
    private ImageView ivPostPhoto;
    private TextView tvPostUserName;
    private TextView tvLikes;
    private TextView tvPostDescription;

    private User selectedUser;
    private Post selectedPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        initializeComponents();

        Toolbar toolbar = findViewById(R.id.idToolbar);
        toolbar.setTitle("Post");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            selectedPost = (Post) bundle.getSerializable("selectedPost");
            selectedUser = (User) bundle.getSerializable("selectedUser");

            Uri url = Uri.parse(selectedUser.getPhotoPath());

            if (url != null) {
                Glide.with(this).load(url).into(civPostUser);
            }

            tvPostUserName.setText(selectedUser.getName());

            Uri postUrl = Uri.parse(selectedPost.getPhotoPath());

            if (postUrl != null) {
                Glide.with(this).load(postUrl).into(ivPostPhoto);
            }

            tvPostDescription.setText(selectedPost.getDescription());
        }
    }

    private void initializeComponents() {
        civPostUser = findViewById(R.id.civPostUser);
        ivPostPhoto = findViewById(R.id.ivPostPhoto);
        tvPostUserName = findViewById(R.id.tvPostUserName);
        tvLikes = findViewById(R.id.tvLikes);
        tvPostDescription = findViewById(R.id.tvPostDescription);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}