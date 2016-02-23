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
    private static final String FILE_NAME = "profile.png";
    private static final String PATH_OF_FILE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/prof/";

    public static Bitmap loadFromFile(String path) {
        Log.d(TAG, "Attempting to load from file!");
        try {
            File f = new File(path);
            if (!f.exists()) {
                Log.d(TAG, "Making directory!");
                boolean created = f.mkdir();
                Log.d(TAG, "Created: " + created);
                return null;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(path + FILE_NAME);
            return bitmap;
        } catch (Exception e) {
            Log.d(TAG, "Bitmap is not retrieved from the file");
            return null;
        }
    }

    public static Bitmap loadFromCacheFile() {
        return loadFromFile(PATH_OF_FILE);
    }

    public static void saveToCacheFile(Bitmap bmp) throws IOException {
        FileOutputStream out = new FileOutputStream(PATH_OF_FILE + FILE_NAME);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();
    }
}
