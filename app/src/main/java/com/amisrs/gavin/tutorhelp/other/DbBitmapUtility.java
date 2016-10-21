package com.amisrs.gavin.tutorhelp.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

/**
 * Created by karenhuang on 17/10/16.
 * <p>
 * From Stack Overflow user Lazy Ninja.
 * http://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database
 * <p>
 * Just converts bitmap to and from byte array.
 */


public class DbBitmapUtility {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    //convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
