package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import helpers.FirebaseConfig;
import pedroadmn.instagramclone.com.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private TextView tvRegister;
    private Button btEnter;
    private ProgressBar progressLogin;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeComponents();

        tvRegister.setOnClickListener(v -> goToRegisterScreen());
        btEnter.setOnClickListener(v -> login());
    }

    private void verifyLoggedUser() {
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void login() {
        progressLogin.setVisibility(View.VISIBLE);
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (!email.isEmpty()) {
            if (!password.isEmpty()) {
                auth.signInWithEmailAndPassword(
                        email, password
                ).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,
                                "Successfully logged",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        String exceptionMessage = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException exception) {
                            exceptionMessage = "User is not registered";
                        } catch (FirebaseAuthInvalidCredentialsException exception) {
                            exceptionMessage = "Email or password is invalid";
                        } catch (Exception exception) {
                            exceptionMessage = "Error on login user";
                            exception.printStackTrace();
                        }

                        Toast.makeText(this, exceptionMessage, Toast.LENGTH_SHORT).show();
                    }

                    progressLogin.setVisibility(View.INVISIBLE);
                });
            } else {
                Toast.makeText(LoginActivity.this,
                        "Fill the password!",
                        Toast.LENGTH_SHORT).show();
                progressLogin.setVisibility(View.INVISIBLE);
            }
        } else {
            Toast.makeText(LoginActivity.this,
                    "Fill the E-mail!",
                    Toast.LENGTH_SHORT).show();
            progressLogin.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeComponents() {
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        tvRegister = findViewById(R.id.tvRegister);
        btEnter = findViewById(R.id.btEnter);
        progressLogin =findViewById(R.id.progressLogin);

        auth = FirebaseConfig.getAuthFirebase();
    }

    private void goToRegisterScreen() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyLoggedUser();
    }
}