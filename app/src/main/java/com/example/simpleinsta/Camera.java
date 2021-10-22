package com.example.simpleinsta;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class Camera extends Fragment {
    protected static final String TAG = "Camera";

    //UI Elements
    protected ImageView ivPostImage;
    protected EditText etDescription;
    protected Button btnSubmit;
    protected ImageButton ibPostCamera;

    //handle saving method.
    protected String mode;

    //Bitmap Saving
    public static final int CAPTURE_IMAGE_REQUEST_CODE = 23;
    protected File photoFile;
    protected final String photoFileName = "capture.jpg";
    protected final String APP_TAG = "SimpleInsta";

    //-- User methods
    protected void launchCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(requireContext(), "com.simpleinsta.fileprovider", photoFile);

        i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if(i.resolveActivity(requireContext().getPackageManager()) != null){
            startActivityForResult(i, CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPostImage.setImageBitmap(takenImage);

                //auto Save Post dependent 'mode'.
                savePost();

            } else {
                Toast.makeText(getContext(), "No Image Taken", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorgeDir = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if(!mediaStorgeDir.exists() && !mediaStorgeDir.mkdirs()){
            Log.d(TAG, "Failed to Create Directory");
        }

        return new File(mediaStorgeDir.getPath() + File.separator + photoFileName);
    }

    protected void savePost(){
        if(mode.equals("autosave")) {
            ParseQuery<UserImgs> query = ParseQuery.getQuery(UserImgs.class);
            query.whereEqualTo(UserImgs.KEY_USER, ParseUser.getCurrentUser());

            query.findInBackground(new FindCallback<UserImgs>() {
                @Override
                public void done(List<UserImgs> objects, ParseException e) {
                    for (UserImgs userimg: objects){
                        userimg.put(UserImgs.KEY_IMAGE, new ParseFile(photoFile));
                        userimg.saveInBackground();
                    }
                }
            });
        } else {
            return;
        }
    }
}
