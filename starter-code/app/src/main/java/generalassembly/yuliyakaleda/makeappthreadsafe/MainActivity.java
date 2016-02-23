package generalassembly.yuliyakaleda.makeappthreadsafe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "makeappthreadsafe";
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView image;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        change = (Button) findViewById(R.id.choose_button);
        image = (ImageView) findViewById(R.id.image);
        setProfileImage();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfileImage();
            }
        });
    }

    // sets the chosen image as a profile picture
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            image.setImageURI(selectedImage);

            //saves a new picture to a file
            ImageSaveAsyncTask imageSaveAsyncTask = new ImageSaveAsyncTask();
            imageSaveAsyncTask.execute(selectedImage);


            //provides a feedback that the image is set as a profile picture
            Toast.makeText(this, "The image is set as a profile picture", Toast.LENGTH_LONG).show();
        }
    }

    //sets the image view of the profile picture to the previously saved image or the placeholder if
    // the image has never been modified
    private void setProfileImage() {
        Bitmap bm = PictureUtil.loadFromCacheFile();
        if (bm != null) {
            image.setImageBitmap(bm);
            Log.d("test", "My Image");
        } else {
            image.setImageResource(R.drawable.placeholder);
            Log.d("test", "Stock Image");
        }
    }

    // brings up the photo gallery/other resources to choose a picture
    private void changeProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private class ImageSaveAsyncTask extends AsyncTask<Uri, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Uri... params) {

            //saves a new picture to a file
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(params[0]));
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Image uri is not received or recognized");
            }
            try {
                PictureUtil.saveToCacheFile(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MainActivity.this, "Picture Saved", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }
}
