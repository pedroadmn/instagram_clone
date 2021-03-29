package activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import helpers.FirebaseConfig;
import models.User;
import pedroadmn.instagramclone.com.R;

public class ProfileActivity extends AppCompatActivity {

    private Button btFollow;
    private CircleImageView cvProfileImage;
    private TextView tvPosts;
    private TextView tvFollowers;
    private TextView tvFollowing;

    private User selectedUser;
    private DatabaseReference usersRef;
    private DatabaseReference friendUserRef;
    private ValueEventListener valueEventListenerFriendProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.idToolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        initializeComponents();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            selectedUser =  (User) bundle.getSerializable("selectedUser");
            getSupportActionBar().setTitle(selectedUser.getName());

            String photoPath = selectedUser.getPhotoPath();

            if (photoPath != null) {
                Uri url = Uri.parse(selectedUser.getPhotoPath());
                Glide.with(ProfileActivity.this).load(url).into(cvProfileImage);
            } else {
                cvProfileImage.setImageResource(R.drawable.avatar);
            }
        }
    }

    private void initializeComponents() {
        btFollow = findViewById(R.id.btFollow);
        cvProfileImage = findViewById(R.id.cvProfileImage);
        tvFollowers = findViewById(R.id.tvFollowersNumber);
        tvPosts = findViewById(R.id.tvPostNumber);
        tvFollowing = findViewById(R.id.tvFollowingNumber);

        usersRef = FirebaseConfig.getFirebase().child("users");
    }

    private void getFriendProfileData() {
        friendUserRef = usersRef.child(selectedUser.getId());

        valueEventListenerFriendProfile = friendUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                tvFollowers.setText(String.valueOf(user.getFollowers()));
                tvFollowing.setText(String.valueOf(user.getFollowing()));
                tvPosts.setText(String.valueOf(user.getPosts()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getFriendProfileData();
    }

    @Override
    protected void onStop() {
        super.onStop();

        friendUserRef.removeEventListener(valueEventListenerFriendProfile);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}