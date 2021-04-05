package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import models.Comment;
import models.User;
import pedroadmn.instagramclone.com.R;

public class CommentsActivity extends AppCompatActivity {

    private EditText etComment;
    private Button btSendComment;
    private String postId;
    private User user;

    private FirebaseAuth auth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = findViewById(R.id.idToolbar);
        toolbar.setTitle("Comments");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        initializeComponents();

        btSendComment.setOnClickListener(v -> saveComment());

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            postId = bundle.getString("postId");
        }
    }

    private void initializeComponents() {
        etComment = findViewById(R.id.etComment);
        btSendComment = findViewById(R.id.btSendComment);

        user = FirebaseUserHelper.getLoggedUserInfo();
        auth = FirebaseConfig.getAuthFirebase();
        storageReference = FirebaseConfig.getFirebaseStorage();
    }

    private void saveComment() {
        String commentText = etComment.getText().toString();

        if (commentText != null && !commentText.equals("")) {
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setUserId(user.getId());
            comment.setUserName(user.getName());
            comment.setPhotoPath(user.getPhotoPath());
            comment.setComment(commentText);

            if (comment.save()) {
                Toast.makeText(this, "Comment saved ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Type a comment before try to save", Toast.LENGTH_SHORT).show();
        }

        etComment.setText("");

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}