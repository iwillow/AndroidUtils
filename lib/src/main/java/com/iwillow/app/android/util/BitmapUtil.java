package com.iwillow.app.android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by https://github.com/iwillow/ on 2016/12/20.
 */

public class BitmapUtil {
    public static final String TAG = "BitmapUtil";
    public static final int MAX_WIDTH = 800;
    public static final int MAX_HEIGHT = 800;

    public static Bitmap decodeBitmap(String imagePath, int targetWidth, int targetHeight) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetWidth > 0) || (targetHeight > 0)) {
            scaleFactor = Math.min(photoW / targetWidth, photoH / targetHeight);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        return BitmapFactory.decodeFile(imagePath, bmOptions);

    }

    public static Bitmap getUploadBitmap(String imagePath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor;
        if (photoW < MAX_WIDTH && photoH < MAX_HEIGHT) {
            scaleFactor = 1;
        } else {
            scaleFactor = Math.max(photoW / MAX_WIDTH, photoH / MAX_HEIGHT);
        }
        Log.d(TAG, "scaleFactor:" + scaleFactor);
        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        /* Decode the JPEG file into a Bitmap */
        return BitmapFactory.decodeFile(imagePath, bmOptions);

    }

    public static InputStream bitmap2InputStream(Bitmap bm, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(format, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public static byte[] bitmap2Bytes(Bitmap bm, Bitmap.CompressFormat format, int quality) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(format, quality, baos);
            return baos.toByteArray();
        } finally {
            bm.recycle();
        }
    }
}
