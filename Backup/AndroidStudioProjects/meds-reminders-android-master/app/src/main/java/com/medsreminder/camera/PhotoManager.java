package com.medsreminder.camera;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class PhotoManager extends DialogFragment{
	
	private String photoPath;
	
	//Dialog Options
	final CharSequence[] dialogItems = { "Take Photo", "Choose from Library","Cancel" };
	//Dialog Title
	public final static String DIALOG_TITLE ="ADD PHOTO!";
	//Needed for Photo
	public static final int REQUEST_IMAGE_CAPTURE = 1;
	public static final int SELECT_FILE = 2;
	
	private File photoFile;
	private Uri photoFileUri;
	
	public enum PhotoOptions{
		TAKE_PICTURE,
		CHOOSE_FROM_LIBRARY,
		CANCEL
	}
	
	private PhotoOptions optionSelected;
	
	Context innerContext;
	
	
	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	
	public PhotoOptions getOptionSelected() {
		return optionSelected;
	}

	public void setOptionSelected(PhotoOptions optionSelected) {
		this.optionSelected = optionSelected;
	}
	
	public File getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(File photoFile) {
		this.photoFile = photoFile;
	}

	public Uri getPhotoFileUri() {
		return photoFileUri;
	}

	public void setPhotoFileUri(Uri photoFileUri) {
		this.photoFileUri = photoFileUri;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(DIALOG_TITLE);
		builder.setItems(dialogItems, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				try{
					if (dialogItems[item].equals("Take Photo")) {

						photoFile = createImageFile(getActivity());
						Intent dialogIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						dialogIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
						dialogIntent.putExtra("PHOTO_PATH", photoPath);
						
						//Starts camera activity
						getActivity().startActivityForResult(dialogIntent, REQUEST_IMAGE_CAPTURE);
						
					} else if (dialogItems[item].equals("Choose from Library")) {
						
						File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MedsReminders");
						Intent intent = new Intent(); 
						intent.setAction(android.content.Intent.ACTION_PICK); 
						intent.setDataAndType(Uri.fromFile(f), "image/*"); 
						
						//Starts image chooser activity
						getActivity().startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
						
					} else if (dialogItems[item].equals("Cancel")) {
						dialog.dismiss();
					}
				}
				catch(Exception e){
					String s = e.getMessage();
					Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
				}
			}
		});
		return builder.create();

		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Never reached here
	}
	
	@SuppressLint("SimpleDateFormat")
	private File createImageFile(Context context) throws IOException{
	
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";

		File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MedsReminders");
		
		//Check directory has been created
		if (!storageDir.mkdirs()) {
	        Log.e("Log dir", "Directory not created");
	    }
		File imageFile = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
				);

		this.photoPath = imageFile.getAbsolutePath();
		this.photoFileUri = Uri.fromFile(imageFile);
		return imageFile;
		
	}
	
	//Returns a decoded version of the image
	public Bitmap GetBitmap(int imageViewWidth, int imageViewHeight){
		
		// Get the dimensions of the View
        int targetW = imageViewWidth;
        int targetH = imageViewHeight;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(getPhotoPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(getPhotoPath(), bmOptions);
        
        return bitmap;
	}
	
	public String GetImageAbsolutePath(Context context, Uri uri){
		
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(context, uri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
		
	}
	
	//Forces media scanner to add newly taken images
	public void ForceMediaScanner(Context context){
	
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(this.photoFileUri);
		context.sendBroadcast(mediaScanIntent);
		
	}
}
