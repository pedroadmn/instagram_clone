package activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.PostsGridAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import models.Post;
import models.User;
import pedroadmn.instagramclone.com.R;

public class ProfileActivity extends AppCompatActivity {

    private Button btFollow;
    private CircleImageView cvProfileImage;
    private TextView tvPosts;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private GridView gvPosts;
    private PostsGridAdapter postsGridAdapter;

    private User selectedUser;
    private User loggedUser;
    private DatabaseReference firebaseRef;
    private DatabaseReference usersRef;
    private DatabaseReference loggedUserRef;
    private DatabaseReference followersRef;
    private DatabaseReference friendUserRef;
    private DatabaseReference postsRef;
    private ValueEventListener valueEventListenerFriendProfile;

    private String userLoggedId;

    private List<Post> posts;

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

            postsRef = firebaseRef.child("posts").child(selectedUser.getId());

            getSupportActionBar().setTitle(selectedUser.getName());

            String photoPath = selectedUser.getPhotoPath();

            if (photoPath != null) {
                Uri url = Uri.parse(selectedUser.getPhotoPath());
                Glide.with(ProfileActivity.this).load(url).into(cvProfileImage);
            } else {
                cvProfileImage.setImageResource(R.drawable.avatar);
            }

            initImageLoader();
            loadPosts();

            gvPosts.setOnItemClickListener((adapterView, view, i, l) -> {
                Post post = posts.get(i);
                Intent intent = new Intent(ProfileActivity.this, PostActivity.class);
                intent.putExtra("selectedPost", post);
                intent.putExtra("selectedUser", selectedUser);
                startActivity(intent);
            });
        }
    }

    private void initializeComponents() {
        btFollow = findViewById(R.id.btFollow);
        cvProfileImage = findViewById(R.id.cvProfileImage);
        tvFollowers = findViewById(R.id.tvFollowersNumber);
        tvPosts = findViewById(R.id.tvPostNumber);
        tvFollowing = findViewById(R.id.tvFollowingNumber);
        gvPosts = findViewById(R.id.gvPosts);

        userLoggedId = FirebaseUserHelper.getLoggedUserId();

        firebaseRef = FirebaseConfig.getFirebase();

        usersRef = firebaseRef.child("users");
        followersRef = firebaseRef.child("followers");
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

    private void verifyIfFollowTheUser() {
        DatabaseReference followerRef = followersRef.child(userLoggedId).child(selectedUser.getId());

        followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    enableFollowButton(true);
                    btFollow.setText("Following");
                } else {
                    enableFollowButton(false);
                    btFollow.setText("Follow");

                    btFollow.setOnClickListener(v -> followUser(loggedUser, selectedUser));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void followUser(User loggedUser, User userToFollow) {
        HashMap<String, Object> userToFollowData = new HashMap<>();
        userToFollowData.put("name", userToFollow.getName());
        userToFollowData.put("photoPath", userToFollow.getPhotoPath());

        DatabaseReference followerRef = followersRef.child(loggedUser.getId())
                .child(userToFollow.getId());

        followerRef.setValue(userToFollowData);

        btFollow.setText("Following");
        btFollow.setOnClickListener(null);

        int following = loggedUser.getFollowing() + 1;
        HashMap<String, Object> followingData = new HashMap<>();
        followingData.put("following", following);
        DatabaseReference userFollowing = usersRef.child(loggedUser.getId());
        userFollowing.updateChildren(followingData);

        int followers = userToFollow.getFollowers() + 1;
        HashMap<String, Object> followersData = new HashMap<>();
        followersData.put("followers", followers);
        DatabaseReference userFollowers = usersRef.child(userToFollow.getId());
        userFollowers.updateChildren(followersData);

    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        ImageLoader.getInstance().init(configuration);
    }

    private void loadPosts() {
        posts = new ArrayList<>();
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int gridSize = getResources().getDisplayMetrics().widthPixels;
                int imageSize = gridSize / 3;

                gvPosts.setColumnWidth(imageSize);

                List<String> photoUrls = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);
                    posts.add(post);
                    photoUrls.add(post.getPhotoPath());
                }

                postsGridAdapter = new PostsGridAdapter(getApplicationContext(), R.layout.grid_post_adapter, photoUrls);
                gvPosts.setAdapter(postsGridAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLoggedUserData() {
        loggedUserRef = usersRef.child(userLoggedId);
        loggedUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loggedUser = snapshot.getValue(User.class);

                verifyIfFollowTheUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void enableFollowButton(boolean followUser) {
        if (followUser) {
            btFollow.setText("Following");
            btFollow.setEnabled(false);
        } else {
            btFollow.setText("Follow");
            btFollow.setEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        getFriendProfileData();
        getLoggedUserData();
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