package generalassembly.yuliyakaleda.makeappthreadsafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity{
  private static final String TAG = "makeappthreadsafe";
  private static final int PICK_IMAGE_REQUEST = 1;
  private ImageView image;
  private ImageView image2;
  private Button change;
  Bitmap bm;
  private boolean start;

  Uri selectedImage;
  String oldSelectedImageUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    start = false;
    change = (Button) findViewById(R.id.choose_button);
    image = (ImageView) findViewById(R.id.image);
    image2 = (ImageView) findViewById(R.id.previous_image);
    setProfileImage();
    change.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        start =true;
        changeProfileImage();

      }
    });
  }

  // sets the chosen image as a profile picture
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK && null != data) {
      selectedImage = data.getData();
      image.setImageURI(selectedImage);

      ImageAsyncTask asyncTask = new ImageAsyncTask();
      asyncTask.execute();

    }
  }

  //sets the image view of the profile picture to the previously saved image or the placeholder if
  // the image has never been modified
  private void setProfileImage() {
    bm = PictureUtil.loadFromCacheFile();
    if (bm != null) {
      image.setImageBitmap(bm);
    } else {
      image.setImageResource(R.drawable.placeholder);
    }
  }

  // brings up the photo gallery/other resources to choose a picture
  private void changeProfileImage() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
  }

  private class ImageAsyncTask extends AsyncTask<Void,Void,Void>{
    Bitmap bitmap = null;
    @Override
    protected Void doInBackground(Void... params) {
      try {
        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
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
      super.onPostExecute(aVoid);
      Toast.makeText(MainActivity.this, "The image is set as a profile picture", Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (selectedImage != null && !selectedImage.equals("")) {
      SharedPreferences sharedPreferences = getSharedPreferences(TAG, Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPreferences.edit();
      oldSelectedImageUri = String.valueOf(selectedImage);
      editor.putString("OLDIMAGE",oldSelectedImageUri);
      editor.commit();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    SharedPreferences sharedPreferences = getSharedPreferences(TAG,Context.MODE_PRIVATE);
    String oldImageUri = sharedPreferences.getString("OLDIMAGE", null);
    if (oldImageUri != null && !oldImageUri.equals("") && start) {
      Uri oldSelectedImage = Uri.parse(oldImageUri);
      image2.setImageURI(oldSelectedImage);
    }
  }

}
