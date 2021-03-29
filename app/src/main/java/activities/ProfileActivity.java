package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import models.User;
import pedroadmn.instagramclone.com.R;

public class ProfileActivity extends AppCompatActivity {
    private Button btFollow;

    private User selectedUser;

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
        }
    }

    private void initializeComponents() {
        btFollow = findViewById(R.id.btFollow);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}