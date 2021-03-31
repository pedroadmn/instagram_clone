package activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

import adapters.FilterThumbnailsAdapter;
import listeners.RecyclerItemClickListener;
import pedroadmn.instagramclone.com.R;

public class FilterActivity extends AppCompatActivity {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private ImageView ivSelectedImage;
    private Bitmap image;
    private Bitmap imageFilter;
    private RecyclerView rvFilters;
    private List<ThumbnailItem> filterList;
    private FilterThumbnailsAdapter filterThumbnailsAdapter;

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

    private void initializeComponents() {
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        filterList = new ArrayList<>();
        rvFilters = findViewById(R.id.rvFilters);
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