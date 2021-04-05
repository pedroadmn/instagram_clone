package activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import adapters.CommentAdapter;
import adapters.PostsGridAdapter;
import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import models.Comment;
import models.Post;
import models.User;
import pedroadmn.instagramclone.com.R;

public class CommentsActivity extends AppCompatActivity {

    private EditText etComment;
    private Button btSendComment;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private String postId;
    private User user;
    private List<Comment> comments = new ArrayList<>();

    private DatabaseReference commentsRef;
    private DatabaseReference firebaseRef;
    private ValueEventListener commentsValueEventListener;

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

        commentAdapter = new CommentAdapter(comments, this);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setHasFixedSize(true);
        rvComments.setAdapter(commentAdapter);

        if (bundle != null) {
            postId = bundle.getString("postId");
            commentsRef = firebaseRef.child("comments").child(postId);
        }
    }

    private void initializeComponents() {
        etComment = findViewById(R.id.etComment);
        btSendComment = findViewById(R.id.btSendComment);
        rvComments = findViewById(R.id.rvComments);

        user = FirebaseUserHelper.getLoggedUserInfo();
        firebaseRef = FirebaseConfig.getFirebase();

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

    private void loadComments() {
        commentsValueEventListener = commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Comment comment = ds.getValue(Comment.class);
                    comments.add(comment);
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadComments();
    }

    @Override
    protected void onStop() {
        super.onStop();

        commentsRef.removeEventListener(commentsValueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}