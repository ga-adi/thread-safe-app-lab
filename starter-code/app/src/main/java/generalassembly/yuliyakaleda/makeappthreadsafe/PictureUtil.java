package generalassembly.yuliyakaleda.makeappthreadsafe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class PictureUtil {
  private static final String TAG = "makeappthreadsafe";

  public static File getSavePath() {
    File path;
    if (hasSDCard()) {
      path = new File(getSDCardPath() + "/prof/");
      path.mkdir();
    } else {
      path = Environment.getDataDirectory();
    }
    return path;
  }

  public static String getCacheFilename() {
    File f = getSavePath();
    return f.getAbsolutePath() + "/profile.png";
  }

  public static Bitmap loadFromFile(String filename) {
    try {
      File f = new File(filename);
      if (!f.exists()) {
        return null;
      }
      Bitmap bitmap = BitmapFactory.decodeFile(filename);
      return bitmap;
    } catch (Exception e) {
      Log.d(TAG, "Bitmap is not retrieved from the file");
      return null;
    }
  }

  public static Bitmap loadFromCacheFile() {
    return loadFromFile(getCacheFilename());
  }

  public static void saveToCacheFile(Bitmap bmp) throws IOException {
    saveToFile(getCacheFilename(), bmp);
  }

  public static void saveToFile(String filename, Bitmap bmp)
      throws IOException {



      FileOutputStream out = new FileOutputStream(filename);
    Log.d("PictureUtil","file stream to "+filename);
      bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
      out.flush();
      out.close();
  }

  public static boolean hasSDCard() {
    String status = Environment.getExternalStorageState();
    return status.equals(Environment.MEDIA_MOUNTED);
  }

  public static String getSDCardPath() {
    File path = Environment.getExternalStorageDirectory();
    return path.getAbsolutePath();
  }
}
