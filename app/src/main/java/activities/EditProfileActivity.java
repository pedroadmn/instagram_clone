package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import pedroadmn.instagramclone.com.R;

public class EditProfileActivity extends AppCompatActivity {

    private CircleImageView cvEditProfilePhoto;
    private EditText etEditProfileName;
    private EditText etEditProfileEmail;
    private EditText etRegisterPassword;
    private Button btEditProfileUpdateChanges;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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

        etEditProfileEmail.setFocusable(false);

        auth = FirebaseConfig.getAuthFirebase();
    }
}