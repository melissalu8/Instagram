package codepath.com.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import codepath.com.instagram.Model.Post;

// parse-dashboard --appId mlinstagram --masterKey masterkey --serverURL http://melissalu8-instagram.herokuapp.com/parse

public class HomeActivity extends AppCompatActivity {

    private static final String imagePath = ""; // TODO: camera and imagePath
    private EditText descriptionInput;
    private ImageButton createButton;
    private Button refreshButton;
    private Button logoutBtn;
    private Button postBtn;


    // Launching the camera variables
    public final String APP_TAG = "Instagram";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final int SOME_WIDTH = 100;
    public final int SOME_HEIGHT = 100;
    public String photoFileName = "photo.jpg";
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.tbTop);
        setSupportActionBar(toolbar);

        descriptionInput = findViewById(R.id.etDescription);
        createButton = findViewById(R.id.ibCreate);
        refreshButton = findViewById(R.id.btnRefresh);
        logoutBtn = findViewById(R.id.btnLogout);
        postBtn = findViewById(R.id.btnPost);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera();
//                final String description = descriptionInput.getText().toString();
//                final ParseUser user = ParseUser.getCurrentUser();
//
//                final File file = new File(imagePath);
//                final ParseFile parseFile = new ParseFile(file);
//
//                createPost(description, parseFile, user);
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final File file = getPhotoFileUri(photoFileName);
                final ParseFile parseFile = new ParseFile(file);
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        createPost(description, parseFile, user);
                    }
                });
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                final Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "You have successfully logged out", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        // TODO - create and save post
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Create post succcess");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        Log.d("HomeActivity", "Post[" + i + "' = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    // TODO: connect onLaunchCamera to camera button
    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it'bfkvekjieijdfbvvddkdnutnfeljlhcetullvnfeihinrfenrhtvfvcgbfhkhnkcdkflkfvrjlvtlhnctkhgivvubigknjctnnuubjfjlelvjhkuricdfglcchhiilgdrbhbihvllhinjbditvcjbfdjfghbujigikrekcgdvhgtgeejcjbvjtvttjgnhfgts safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    // When the camera app finishes, the onActivityResult() method will be called
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
               Bitmap resizedBitmap = Bitmap.createScaledBitmap(takenImage, SOME_WIDTH, SOME_HEIGHT, false);
                // Load the taken image into a preview
                // TODO: FIX IDs
                ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                ivPreview.setImageBitmap(resizedBitmap);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
