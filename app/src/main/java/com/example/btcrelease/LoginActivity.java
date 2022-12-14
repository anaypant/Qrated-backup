package com.example.btcrelease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btcrelease.Interfaces.FirebaseBoolCallback;
import com.example.btcrelease.utils.firebaseUtils;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView usernameField, passwordField, emailField;
    MaterialButton loginButton, signUpButton, forgotPasswordButton;
    String email, user, pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Basic setting up view stuff
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        usernameField = findViewById(R.id.userNameField);

        signUpButton = findViewById(R.id.SignUpBtn);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordButton = findViewById(R.id.pwdButton);


        // If user is already logged in, send them to main instantly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            },0);
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String user = usernameField.getText().toString().trim();
                String pwd = passwordField.getText().toString().trim();

                // Makes sure all entries are valid.
                if(passedNullChecks()){
                    firebaseUtils.createUser(user, email, pwd, LoginActivity.this);
                }
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String user = usernameField.getText().toString().trim();
                String pwd = passwordField.getText().toString().trim();

                //more null checks
                if (email.equals("") || user.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(email.contains("@") && email.contains("."))) {
                    Toast.makeText(LoginActivity.this, "Email is Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(!email.contains("@") || !email.contains("."))){
                    firebaseUtils.checkIfEmailExists(email, new FirebaseBoolCallback() {
                        @Override
                        public void onBoolCallback(boolean value) {
                            if(value){
                                //email doesn't exist
                                firebaseUtils.resetUserPassword(email);
                                Toast.makeText(LoginActivity.this, "Check email for reset.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String user = usernameField.getText().toString().trim();
                String pwd = passwordField.getText().toString().trim();

                if(passedNullChecks()){
                    firebaseUtils.logInUser(email, pwd, user, LoginActivity.this, new FirebaseBoolCallback() {
                        @Override
                        public void onBoolCallback(boolean value) {
                            if(value){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                },0);
                            }
                        }
                    });
                }
            }
        });

    }
    private boolean passedNullChecks(){
        email = emailField.getText().toString().trim();
        user = usernameField.getText().toString().trim();
        pwd = passwordField.getText().toString().trim();
        // Makes sure all entries are valid.
        if (email.equals("") || pwd.equals("") || user.equals("")) {
            Toast.makeText(LoginActivity.this, "Null Checks: Please enter all the fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(email.contains("@") && email.contains("."))) {
            Toast.makeText(LoginActivity.this, "Email is Invalid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd.length() < 6){
            Toast.makeText(LoginActivity.this, "Please enter min. 6 length password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}