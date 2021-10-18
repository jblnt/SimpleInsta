package com.example.simpleinsta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PostActivity extends AppCompatActivity {
    public static final String TAG = "PostActivity";
    public static final int CAPTURE_IMAGE_REQUEST_CODE = 23;

    private ImageView ivPostImage;
    private EditText etDescription;
    private Button btnSubmit;
    //private Button btnCamera;
    private ImageButton ibPostCamera;

    private File photoFile;
    private final String photoFileName = "capture.jpg";
    private final String APP_TAG = "SimpleInsta";
    private Object BitmapScaler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ivPostImage = findViewById(R.id.ivPostImage);
        etDescription = findViewById(R.id.etDescription);
        btnSubmit = findViewById(R.id.btnSubmit);
        ibPostCamera = findViewById(R.id.ibPostCamera);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                ParseUser currentUser =  ParseUser.getCurrentUser();

                savePost(description,currentUser, photoFile);
            }
        });

        ibPostCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();

        //handle description
        if (description.isEmpty()){
            Toast.makeText(PostActivity.this, "Description Needed!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            post.setDescription(description);
        }

        //handle user
        post.setUser(currentUser);

        //handle photo
        if(photoFile == null || ivPostImage.getDrawable() == null){
            Toast.makeText(PostActivity.this, "No Image Present", Toast.LENGTH_SHORT).show();
            return;
        }

        post.setImage(new ParseFile(photoFile));

        //Now save to backend
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "error", e);
                    Toast.makeText(PostActivity.this, "Error Posting...", Toast.LENGTH_SHORT).show();
                } else {
                    //Log.i(TAG, "Post Saved...");
                    Toast.makeText(PostActivity.this, "Post Saved", Toast.LENGTH_SHORT).show();
                    etDescription.setText("");
                    ivPostImage.setImageResource(0);

                    reDirectMainActivity();
                }
            }
        });
    }

    private void reDirectMainActivity() {
        Intent i = new Intent(PostActivity.this, MainActivity.class);
        startActivity(i);

        finish();
    }

    private void launchCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.simpleinsta.fileprovider", photoFile);

        i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPostImage.setImageBitmap(takenImage);
            } else {
                Toast.makeText(PostActivity.this, "No Image Taken", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorgeDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if(!mediaStorgeDir.exists() && !mediaStorgeDir.mkdirs()){
            Log.d(TAG, "Failed to Create Directory");
        }

        return new File(mediaStorgeDir.getPath() + File.separator + photoFileName);
    }

}