package com.cryptic.imed.util.photo.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * @author sharafat
 */
public class ImageUtils {

    public static byte[] getByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    public static byte[] getByteArray(Drawable drawable) {
        return getByteArray(getBitmap(drawable));
    }

    public static Bitmap getBitmap(byte[] byteArray) {
        return byteArray == null ? null : BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap getBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * @deprecated Use {@link #getDrawable(byte[], Resources)} to ensure
     * that the drawable has correctly set its target density.
     */
    public static Drawable getDrawable(byte[] byteArray) {
        return byteArray == null ? null : getDrawable(getBitmap(byteArray));
    }

    public static Drawable getDrawable(byte[] byteArray, Resources resources) {
        return byteArray == null ? null : getDrawable(getBitmap(byteArray), resources);
    }

    /**
     * @deprecated Use {@link #getDrawable(Bitmap, Resources)} to ensure
     * that the drawable has correctly set its target density.
     */
    public static Drawable getDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    public static Drawable getDrawable(Bitmap bitmap, Resources resources) {
        return bitmap == null ? null : new BitmapDrawable(resources, bitmap);
    }

    public static Bitmap getNonEmptyImage(Bitmap image, Bitmap defaultImage) {
        return image != null? image : defaultImage;
    }

    public static Bitmap getNonEmptyImage(Bitmap image, byte[] defaultImage) {
        return image != null? image : getBitmap(defaultImage);
    }

    public static Bitmap getNonEmptyImage(Bitmap image, Drawable defaultImage) {
        return image != null? image : getBitmap(defaultImage);
    }

    public static Bitmap getNonEmptyImage(byte[] image, byte[] defaultImage) {
        return image != null? getBitmap(image) : getBitmap(defaultImage);
    }

    public static Bitmap getNonEmptyImage(byte[] image, Bitmap defaultImage) {
        return image != null? getBitmap(image) : defaultImage;
    }

    public static Bitmap getNonEmptyImage(byte[] image, Drawable defaultImage) {
        return image != null? getBitmap(image) : getBitmap(defaultImage);
    }

    public static Bitmap getNonEmptyImage(Drawable image, Drawable defaultImage) {
        return image != null? getBitmap(image) : getBitmap(defaultImage);
    }

    public static Bitmap getNonEmptyImage(Drawable image, byte[] defaultImage) {
        return image != null? getBitmap(image) : getBitmap(defaultImage);
    }

    public static Bitmap getNonEmptyImage(Drawable image, Bitmap defaultImage) {
        return image != null? getBitmap(image) : defaultImage;
    }
}
