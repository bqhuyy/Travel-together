package cs300.apcs04.traveltogether;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

	private static final int ACTIVITY_START_CAMERA_APP = 0;
	private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 1;
	private String mImageFileLocation;
	private Bitmap mBitmap;
	private ImageView mCaptureView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		mCaptureView = (ImageView) findViewById(R.id.CameraActivity_LoadedImage);

		takePhoto();

		Intent image_data = new Intent();

		image_data.putExtra("Image_file_location", mImageFileLocation);

		setResult(Activity.RESULT_OK, image_data);

		finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //finish
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK){
			rotateImage(setReducedImageSize());
			//new UploadImageActivity.ImageUploader().execute();
		}
	}

	public void takePhoto(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

			if (ContextCompat.checkSelfPermission(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				callCameraApp();
			} else {
				if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					Toast.makeText(this, "Exxternal storage permission required to save images", Toast.LENGTH_SHORT).show();
				}
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_RESULT);
			}
		} else {
			callCameraApp();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_EXTERNAL_STORAGE_RESULT){
			if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
				callCameraApp();
			} else {
				Toast.makeText(this, "External write permission has not been granted, camera cannot save images", Toast.LENGTH_SHORT).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	private void callCameraApp(){
		Intent callCameraApplicationIntent = new Intent();
		callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		File photoFile = null;
		try{
			photoFile = createImageFile();
		} catch (IOException e){
			e.printStackTrace();
		}
		String authorities = getApplicationContext().getPackageName() + ".fileprovider";
		Uri imageUri = FileProvider.getUriForFile(this, authorities, photoFile);
		callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
		overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left); //start
	}

	private File createImageFile() throws IOException{
		String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		String imageFileName = "IMAGE_" + timestamp + '_';
		File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File image = File.createTempFile(imageFileName, ".jpeg", storageDirectory);
		mImageFileLocation = image.getAbsolutePath();
		return image;
	}

	private Bitmap setReducedImageSize(){
		int targetImageViewWidth = mCaptureView.getWidth();
		int targetImageViewHeight = mCaptureView.getHeight();
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
		int cameraImageWidth = bmOptions.outWidth;
		int cameraImageHeight = bmOptions.outHeight;
		int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
	}

	private void rotateImage (Bitmap bitmap){
		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(mImageFileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
		Matrix matrix = new Matrix();
		switch (orientation){
			case ExifInterface.ORIENTATION_ROTATE_90:
				matrix.setRotate(90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				matrix.setRotate(180);
				break;
			default:
		}
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		/*mCaptureView.setImageBitmap(rotatedBitmap);
		mBitmap = rotatedBitmap;*/
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent image_data = new Intent();

		image_data.putExtra("Image_file_location", mImageFileLocation);

		setResult(Activity.RESULT_OK, image_data);

		finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //finish
	}
}
