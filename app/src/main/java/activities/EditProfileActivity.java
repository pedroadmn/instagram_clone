package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import models.User;
import pedroadmn.instagramclone.com.R;

public class EditProfileActivity extends AppCompatActivity {

    private CircleImageView cvEditProfilePhoto;
    private EditText etEditProfileName;
    private EditText etEditProfileEmail;
    private Button btEditProfileUpdateChanges;

    private User loggedUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        loggedUser = FirebaseUserHelper.getLoggedUserInfo();

        Toolbar toolbar = findViewById(R.id.idToolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        initializeComponents();

        FirebaseUser user = FirebaseUserHelper.getCurrentUser();
        etEditProfileName.setText(user.getDisplayName());
        etEditProfileEmail.setText(user.getEmail());
    }

    private void initializeComponents() {
        cvEditProfilePhoto = findViewById(R.id.cvEditProfilePhoto);
        etEditProfileName = findViewById(R.id.etEditProfileName);
        etEditProfileEmail = findViewById(R.id.etEditProfileEmail);
        btEditProfileUpdateChanges = findViewById(R.id.btEditProfileUpdateChanges);

        btEditProfileUpdateChanges.setOnClickListener(v -> updateChanges());

        etEditProfileEmail.setFocusable(false);

        auth = FirebaseConfig.getAuthFirebase();
    }

    private void updateChanges() {
        String updatedName = etEditProfileName.getText().toString();

        FirebaseUserHelper.updateUsername(updatedName);

        loggedUser.setName(updatedName);
        loggedUser.update();

        Toast.makeText(this, "Successfully updated changes!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}