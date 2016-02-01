package com.zarea.restconnection.utilits;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zarea on 9/9/15.
 */
public class Misc {


    /**
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * @param context
     */
    public static void noInternetToast(Context context) {
        Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
    }

    /**
     * get year from iso8601
     *
     * @param iso
     * @return
     */
    public static String getYearFromIso(String iso) {
        return iso.substring(0, 4);
    }

    /**
     * get month from iso8601
     *
     * @param iso
     * @return
     */
    public static String getMonthFromIso(String iso) {
        return iso.substring(5, 7);
    }

    /**
     * get day from iso8601
     *
     * @param iso
     * @return
     */
    public static String getDayFromIso(String iso) {
        return iso.substring(8, 10);
    }

    /**
     * get full date without time from iso8601
     *
     * @param iso
     * @return
     */
    public static String getDateFromIso(String iso) {
        return iso.substring(0, 10);
    }

    /**
     * get time from iso8601
     *
     * @param iso
     * @return
     */
    public static String getTimeFromIso(String iso) {
        return iso.substring(11, 16);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void setupUI(View view, final Activity activity) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView, activity);
            }
        }
    }

    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "zarea");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                /*Log.d(TAG, "Oops! Failed create " + Config.IMAGE_DIRECTORY_NAME
                        + " directory");*/
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static Boolean checkStringIsEmpty(String value) {
        return value.equals("");
    }
    public static String compressImage(String imagePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap scaledBitmap = BitmapFactory.decodeFile(imagePath, options);
            FileOutputStream out = null;
            String filename = imagePath;
            try {
                out = new FileOutputStream(filename);

                // write the compressed bitmap at the destination specified by
                // filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return filename;
        } catch (OutOfMemoryError outOfMemoryError) {
            return "false";
        }
    }

}
