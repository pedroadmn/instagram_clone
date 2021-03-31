package fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.List;

import activities.EditProfileActivity;
import adapters.PostsGridAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import models.Post;
import models.User;
import pedroadmn.instagramclone.com.R;

public class ProfileFragment extends Fragment {

    private ProgressBar progressBar;
    private CircleImageView cvProfileImage;
    private GridView gvProfilePosts;
    private PostsGridAdapter postsGridAdapter;
    private TextView tvPostNumber;
    private TextView tvFollowersNumber;
    private TextView tvFollowingNumber;
    private Button btEditProfile;

    private DatabaseReference postsRef;
    private DatabaseReference firebaseRef;
    private DatabaseReference loggedUserRef;
    private ValueEventListener valueEventListenerProfile;
    private DatabaseReference usersRef;
    private User loggedUser;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeComponents(view);

        String photoPath = loggedUser.getPhotoPath();

        if (photoPath != null) {
            Uri url = Uri.parse(photoPath);
            Glide.with(getActivity()).load(url).into(cvProfileImage);
        }

        initImageLoader();

        loadPosts();

        return view;
    }

    private void initializeComponents(View view) {
        progressBar = view.findViewById(R.id.progressBarProfile);
        cvProfileImage = view.findViewById(R.id.cvProfileImage);
        gvProfilePosts = view.findViewById(R.id.gvProfilePosts);
        tvPostNumber = view.findViewById(R.id.tvPostNumber);
        tvFollowersNumber = view.findViewById(R.id.tvFollowersNumber);
        tvFollowingNumber = view.findViewById(R.id.tvFollowingNumber);
        btEditProfile = view.findViewById(R.id.btEditProfile);

        btEditProfile.setOnClickListener(v -> openEditProfileScreen());

        loggedUser = FirebaseUserHelper.getLoggedUserInfo();

        firebaseRef = FirebaseConfig.getFirebase();
        usersRef = firebaseRef.child("users");
        postsRef = firebaseRef.child("posts").child(loggedUser.getId());
    }

    private void openEditProfileScreen() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void loadPosts() {
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int gridSize = getResources().getDisplayMetrics().widthPixels;
                int imageSize = gridSize / 3;

                gvProfilePosts.setColumnWidth(imageSize);

                List<String> photoUrls = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);
                    photoUrls.add(post.getPhotoPath());
                }

                tvPostNumber.setText(String.valueOf(photoUrls.size()));

                postsGridAdapter = new PostsGridAdapter(getActivity(), R.layout.grid_post_adapter, photoUrls);
                gvProfilePosts.setAdapter(postsGridAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getActivity())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public void onStart() {
        super.onStart();

        getLoggedUserData();
    }

    private void getLoggedUserData() {
        loggedUserRef = usersRef.child(loggedUser.getId());

        valueEventListenerProfile = loggedUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                tvFollowersNumber.setText(String.valueOf(user.getFollowers()));
                tvFollowingNumber.setText(String.valueOf(user.getFollowing()));
//                tvPosts.setText(String.valueOf(user.getPosts()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        loggedUserRef.removeEventListener(valueEventListenerProfile);
    }
}