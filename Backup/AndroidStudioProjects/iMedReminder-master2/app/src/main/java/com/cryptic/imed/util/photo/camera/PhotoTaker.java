package com.cryptic.imed.util.photo.camera;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.cryptic.imed.util.photo.imagecrop.*;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

import java.io.File;

/**
 * @author sharafat
 */
@ContextSingleton
public class PhotoTaker {
    private static final int REQ_CODE_TAKE_PHOTO_FROM_CAMERA = 0;
    private static final int REQ_CODE_PICK_IMAGE_FROM_GALLERY = 1;

    private static final String TEMP_PHOTO_FILE_NAME = "tmp_iMedReminder_";

    @Inject
    private Activity activity;

    private boolean cropImage = true;
    private int aspectXForCropping = 0;
    private int aspectYForCropping = 0;
    private int imageWidthAfterCropping = 0;
    private int imageHeightAfterCropping = 0;
    private boolean scaleImageAfterCropping = true;

    private Uri tempPhotoFileUri;

    public void takePhotoFromCamera() throws CameraUnavailableException {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tempPhotoFileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                TEMP_PHOTO_FILE_NAME + String.valueOf(System.currentTimeMillis())));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoFileUri);
        try {
            cameraIntent.putExtra("return-data", true);
            activity.startActivityForResult(cameraIntent, REQ_CODE_TAKE_PHOTO_FROM_CAMERA);
        } catch (ActivityNotFoundException ignore) {    // device has no camera
            throw new CameraUnavailableException();
        }
    }

    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        activity.startActivityForResult(intent, REQ_CODE_PICK_IMAGE_FROM_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, OnPhotoTakeListener onPhotoTakeListener) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_TAKE_PHOTO_FROM_CAMERA:
                    cropPhotoOrNotifyPhotoTakenIfCroppingCannotBeDone(tempPhotoFileUri, onPhotoTakeListener);
                    break;
                case REQ_CODE_PICK_IMAGE_FROM_GALLERY:
                    cropPhotoOrNotifyPhotoTakenIfCroppingCannotBeDone(data.getData(), onPhotoTakeListener);
                    break;
                case ImageCrop.REQ_CODE_CROP_IMAGE:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        onPhotoTakeListener.onPhotoTaken((Bitmap) extras.getParcelable("data"));
                    }
                    deleteTempPhotoFile();
                    break;
            }
        }
    }

    private void cropPhotoOrNotifyPhotoTakenIfCroppingCannotBeDone(Uri photoFileUri,
                                                                   OnPhotoTakeListener onPhotoTakeListener) {
        if (cropImage) {
            if (ImageCrop.isImageCroppingAppAvailable(activity)) {
                ImageCrop.cropImage(photoFileUri, activity, imageWidthAfterCropping, imageHeightAfterCropping,
                        aspectXForCropping, aspectYForCropping, scaleImageAfterCropping);
                return;
            } else {
                Log.w(getClass().getCanonicalName(), "No crop app found.");
            }
        }

        Bitmap photo = BitmapFactory.decodeFile(photoFileUri.getPath());
        onPhotoTakeListener.onPhotoTaken(photo);
        deleteTempPhotoFile();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteTempPhotoFile() {
        if (tempPhotoFileUri != null) {
            File tempImageFile = new File(tempPhotoFileUri.getPath());
            if (tempImageFile.exists()) {
                tempImageFile.delete();
            }
        }
    }

    public void setCropImage(boolean cropImage) {
        this.cropImage = cropImage;
    }

    public void setImageWidthAfterCropping(int imageWidthAfterCropping) {
        this.imageWidthAfterCropping = imageWidthAfterCropping;
    }

    public void setImageHeightAfterCropping(int imageHeightAfterCropping) {
        this.imageHeightAfterCropping = imageHeightAfterCropping;
    }

    public void setAspectXForCropping(int aspectXForCropping) {
        this.aspectXForCropping = aspectXForCropping;
    }

    public void setAspectYForCropping(int aspectYForCropping) {
        this.aspectYForCropping = aspectYForCropping;
    }

    public void setScaleImageAfterCropping(boolean scaleImageAfterCropping) {
        this.scaleImageAfterCropping = scaleImageAfterCropping;
    }
}
