package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import fragments.FeedFragment;
import fragments.PostFragment;
import fragments.ProfileFragment;
import fragments.SearchFragment;
import helpers.FirebaseConfig;
import pedroadmn.instagramclone.com.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseConfig.getAuthFirebase();

        Toolbar toolbar = findViewById(R.id.idToolbar);
        toolbar.setTitle("Instagram");
        setSupportActionBar(toolbar);

        setupBottomNavigationView();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();
    }

    private void setupBottomNavigationView() {
        bottomNavigationViewEx = findViewById(R.id.bottomNavigation);

        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);

        enableNavigation(bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    private void enableNavigation(BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(item -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.icHome:
                    fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();
                    return true;
                case R.id.icSearch:
                    fragmentTransaction.replace(R.id.viewPager, new SearchFragment()).commit();
                    return true;
                case R.id.icPost:
                    fragmentTransaction.replace(R.id.viewPager, new PostFragment()).commit();
                    return true;
                case R.id.icProfile:
                    fragmentTransaction.replace(R.id.viewPager, new ProfileFragment()).commit();
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu:
                logout();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        try {
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}