package com.cryptic.imed.util.photo.imagecrop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sharafat
 */
public class ImageCrop {
    public static final int REQ_CODE_CROP_IMAGE = 2;
    public static final String CHOOSE_CROP_APP = "Choose Crop App";

    public static boolean isImageCroppingAppAvailable(Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(getImageCroppingIntent(), 0);
        return list.size() > 0;
    }

    private static Intent getImageCroppingIntent() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        return intent;
    }

    public static void cropImage(final Uri imageCaptureUri, final Activity activity, int imageWidth, int imageHeight,
                                 int aspectX, int aspectY, boolean scale) {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent imageCroppingIntent = getImageCroppingIntent();
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(imageCroppingIntent, 0);
        int size = list.size();

        if (size == 0) {    // No crop app found
            return;
        }

        imageCroppingIntent.setData(imageCaptureUri);

        if (imageWidth != 0) {
            imageCroppingIntent.putExtra("outputX", imageWidth);
        }
        if (imageHeight != 0) {
            imageCroppingIntent.putExtra("outputY", imageHeight);
        }
        if (aspectX != 0) {
            imageCroppingIntent.putExtra("aspectX", aspectX);
        }
        if (aspectY != 0) {
            imageCroppingIntent.putExtra("aspectY", aspectY);
        }
        imageCroppingIntent.putExtra("scale", scale);
        imageCroppingIntent.putExtra("return-data", true);

        if (size == 1) {
            Intent intent = new Intent(imageCroppingIntent);
            ResolveInfo res = list.get(0);

            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

            activity.startActivityForResult(intent, REQ_CODE_CROP_IMAGE);
        } else {
            for (ResolveInfo res : list) {
                CropOption cropOption = new CropOption();

                cropOption.title = activity.getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                cropOption.icon = activity.getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                cropOption.appIntent = new Intent(imageCroppingIntent);

                cropOption.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                cropOptions.add(cropOption);
            }

            CropOptionAdapter adapter = new CropOptionAdapter(activity.getApplicationContext(), cropOptions);

            new AlertDialog.Builder(activity)
                    .setTitle(CHOOSE_CROP_APP)
                    .setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    activity.startActivityForResult(cropOptions.get(item).appIntent, REQ_CODE_CROP_IMAGE);
                                }
                            })
                    .setOnCancelListener(
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    if (imageCaptureUri != null) {
                                        activity.getContentResolver().delete(imageCaptureUri, null, null);
                                        // imageCaptureUri = null;
                                    }
                                }
                            })
                    .create().show();
        }
    }
}
