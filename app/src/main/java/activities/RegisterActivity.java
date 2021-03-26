package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import helpers.FirebaseConfig;
import helpers.FirebaseUserHelper;
import models.User;
import pedroadmn.instagramclone.com.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterName;
    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private Button btRegister;
    private ProgressBar progressRegister;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeComponents();

        btRegister.setOnClickListener(v -> registerUser());
    }

    private void initializeComponents() {
        etRegisterName = findViewById(R.id.etRegisterName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btRegister = findViewById(R.id.btRegister);
        progressRegister = findViewById(R.id.progressRegister);

        auth = FirebaseConfig.getAuthFirebase();
    }

    private void registerUser() {
        progressRegister.setVisibility(View.VISIBLE);
        String name = etRegisterName.getText().toString();
        String email = etRegisterEmail.getText().toString();
        String password = etRegisterPassword.getText().toString();

        if (!name.isEmpty()) {
            if (!email.isEmpty()) {
                if (!password.isEmpty()) {
                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);

                    auth.createUserWithEmailAndPassword(
                            user.getEmail(), user.getPassword()
                    ).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            try {
                                String userId = task.getResult().getUser().getUid();
                                user.setUserId(userId);
                                user.save();

                                FirebaseUserHelper.updateUsername(user.getName());

                                FirebaseUserHelper.updateUsername(user.getName());

                                Toast.makeText(this, "Successfully registered.", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            String erroExcecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = "Type a stronger password";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = "Please, type a valid e-mail";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = "This account is already registered";
                            } catch (Exception e) {
                                erroExcecao = "Error on register an account: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(RegisterActivity.this, erroExcecao,
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressRegister.setVisibility(View.INVISIBLE);
                    });
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Fill the password!",
                            Toast.LENGTH_SHORT).show();
                    progressRegister.setVisibility(View.INVISIBLE);
                }
            } else {
                Toast.makeText(RegisterActivity.this,
                        "Fill the E-mail!",
                        Toast.LENGTH_SHORT).show();
                progressRegister.setVisibility(View.INVISIBLE);
            }
        } else {
            Toast.makeText(RegisterActivity.this,
                    "Fill the name!",
                    Toast.LENGTH_SHORT).show();
            progressRegister.setVisibility(View.INVISIBLE);
        }
    }
}