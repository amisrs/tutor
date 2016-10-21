package com.amisrs.gavin.tutorhelp.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by karenhuang on 21/10/16.
 * http://www.androidhive.info/2013/11/android-sliding-menu-using-navigation-drawer/  * This class is a custom view to transform profile images to be displayed in a circular fashion  * <p>  * Glide.with(this).load(URL).transform(new ProfileCircle(context)).into(imageView);
 * This class is a custom view to transform profile images to be displayed in a circular fashion
 */

public class ProfileCircle extends BitmapTransformation {
    public ProfileCircle(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return cropCircle(pool, toTransform);
    }

    private static Bitmap cropCircle(BitmapPool pool, Bitmap imageSource) {
        if (imageSource == null) {
            return null;
        }

        int size = Math.min(imageSource.getWidth(), imageSource.getHeight());
        int x = (imageSource.getWidth() - size) / 2;
        int y = (imageSource.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(imageSource, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);

        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
