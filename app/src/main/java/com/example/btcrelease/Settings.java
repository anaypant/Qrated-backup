package com.example.btcrelease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    MaterialButton signOutButton;
    ImageButton backButton;
    String tabName = "TrendingNews";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tabName = extras.getString("tab");
        }


        signOutButton = findViewById(R.id.SignOut);
        backButton = findViewById(R.id.backArrow);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Settings.this, MainActivity.class);
                        intent.putExtra("tab", tabName);
                        startActivity(intent);

                        finish();
                    }

                },0);
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sign user out

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Settings.this, "Signed out.", Toast.LENGTH_SHORT).show();
                //go to login page
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Settings.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                },0);
            }
        });
    }
}