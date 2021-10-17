package com.example.simpleinsta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    EditText etSignupEmail;
    EditText etSignupUsername;
    EditText etSignupPassword;
    EditText etSignupPasswordConfirm;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etSignupEmail = findViewById(R.id.etSignupEmail);
        etSignupUsername = findViewById(R.id.etSignupUsername);
        etSignupPassword = findViewById(R.id.etSignupPassword);
        etSignupPasswordConfirm = findViewById(R.id.etSignupPasswordConfirm);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etSignupEmail.getText().toString();
                String username = etSignupUsername.getText().toString();

                String password = etSignupPassword.getText().toString();
                String passwordCheck = etSignupPasswordConfirm.getText().toString();

                if(password.equals(passwordCheck)) {
                    registerUser(email, username, password);
                } else {
                    Toast.makeText(SignUpActivity.this, "Password Must Match", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registerUser(String email, String username, String password) {
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    //TODO handle problem
                    Log.e(TAG, "Error Signing Up", e);
                } else {
                    Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

}