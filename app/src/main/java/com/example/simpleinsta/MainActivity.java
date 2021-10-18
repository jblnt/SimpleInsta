package com.example.simpleinsta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    ImageButton btnPost;
    ImageButton btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handle button clicks for new post;
        btnPost = findViewById(R.id.ibPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPostActivity();
            }
        });

        //handle button clicks to logout
        btnAccount = findViewById(R.id.ibProfile);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });

    }

    private void launchPostActivity() {
        Intent i = new Intent(this, PostActivity.class);
        startActivity(i);
    }

    private void logOutUser() {
        if(ParseUser.getCurrentUser() != null){
            ParseUser.logOut();

            //If you made it here, it means we can launch the main timeline;
            reDirectLoginActivity();
            finish();
        }

    }

    private void reDirectLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

        //Finishes the loginActivity and removed it from the activity stack;
        finish();
    }
}