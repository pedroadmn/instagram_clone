package activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import adapters.FilterThumbnailsAdapter;
import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import listeners.RecyclerItemClickListener;
import models.Post;
import models.User;
import pedroadmn.instagramclone.com.R;

public class FilterActivity extends AppCompatActivity {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private TextInputEditText etDescription;
    private ImageView ivSelectedImage;
    private Bitmap image;
    private Bitmap imageFilter;
    private RecyclerView rvFilters;
    private List<ThumbnailItem> filterList;
    private FilterThumbnailsAdapter filterThumbnailsAdapter;

    private String loggedUserId;
    private DatabaseReference loggedUserRef;
    private DatabaseReference usersRef;
    private DatabaseReference firebaseRef;
    private User loggedUser;

    private StorageReference storageReference;

    private AlertDialog dialog;

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

        getLoggedUserData();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] imageData = bundle.getByteArray("selectedImage");
            image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            ivSelectedImage.setImageBitmap(image);

            imageFilter = image.copy(image.getConfig(), true);

            filterThumbnailsAdapter = new FilterThumbnailsAdapter(filterList, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rvFilters.setLayoutManager(layoutManager);
            rvFilters.setHasFixedSize(true);
            rvFilters.setAdapter(filterThumbnailsAdapter);

            rvFilters.addOnItemTouchListener(new RecyclerItemClickListener(
                    getApplicationContext(),
                    rvFilters,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ThumbnailItem thumbnailItem = filterList.get(position);
                            imageFilter = image.copy(image.getConfig(), true);
                            Filter filter = thumbnailItem.filter;
                            ivSelectedImage.setImageBitmap(filter.processFilter(imageFilter));
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        }
                    }
            ));

            getFilters();
        }
    }

    private void openLoadingDialog(String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setCancelable(false);
        alert.setView(R.layout.loading);

        dialog = alert.create();
        dialog.show();
    }

    private void getLoggedUserData() {
        openLoadingDialog("Loading data, wait.");
        loggedUserRef = usersRef.child(loggedUserId);
        loggedUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loggedUser = snapshot.getValue(User.class);
                dialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initializeComponents() {
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        filterList = new ArrayList<>();
        etDescription = findViewById(R.id.etFilterDescription);
        rvFilters = findViewById(R.id.rvFilters);

        loggedUserId = FirebaseUserHelper.getLoggedUserId();
        storageReference = FirebaseConfig.getFirebaseStorage();

        firebaseRef = FirebaseConfig.getFirebase();
        usersRef = firebaseRef.child("users");
    }

    private void getFilters() {
        ThumbnailsManager.clearThumbs();
        filterList.clear();

        ThumbnailItem item = new ThumbnailItem();
        item.image = image;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb(item);

        List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());

        for (Filter filter : filters) {
            ThumbnailItem filterItem = new ThumbnailItem();
            filterItem.image = image;
            filterItem.filter = filter;
            filterItem.filterName = filter.getName();
            ThumbnailsManager.addThumb(filterItem);
        }

        filterList.addAll(ThumbnailsManager.processThumbs(getApplicationContext()));
        filterThumbnailsAdapter.notifyDataSetChanged();

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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void publish() {
        openLoadingDialog("Saving post.");
        Post post = new Post();
        post.setUserId(loggedUserId);
        post.setDescription(etDescription.getText().toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageFilter.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageData = baos.toByteArray();

        final StorageReference postsRef = storageReference
                .child("images")
                .child("posts")
                .child(post.getId() + ".jpeg");

        UploadTask uploadTask = postsRef.putBytes(imageData);
        uploadTask.addOnFailureListener(e -> Toast.makeText(FilterActivity.this, "Error on save image", Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(taskSnapshot -> postsRef.getDownloadUrl().addOnCompleteListener(task -> {
                    Uri url = task.getResult();
                    post.setPhotoPath(url.toString());

                    if (post.save()) {
                        int postQtt = loggedUser.getPosts() + 1;
                        loggedUser.setPosts(postQtt);
                        loggedUser.updatePostsQtt();
                        Toast.makeText(FilterActivity.this, "Post Successfully saved", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        finish();
                    }
                })
                        .addOnFailureListener(error -> {
                        }));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}