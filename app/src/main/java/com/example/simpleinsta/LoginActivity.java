package com.example.simpleinsta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    EditText ptUsername;
    EditText etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Check if there is a user logged and redirect.
        if (ParseUser.getCurrentUser() != null) {
            reDirectMainActivity();
        }

        //get references to the view in loginActivity
        ptUsername = findViewById(R.id.ptUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ptUsername.getText().toString();
                String password = etPassword.getText().toString();

                //call the function to handle login presses.
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Logging In...");

        //Begin using Parse...
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                //means something is wrong
                if (e != null) {
                    //Log.e(TAG, "Error logging in...", e);
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i(TAG, "Log in was Successful");

                //If you made it here, it means we can launch the main timeline;
                reDirectMainActivity();

            }
        });

    }

    private void reDirectMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        //Finishes the loginActivity and removed it from the activity stack;
        finish();
    }
}