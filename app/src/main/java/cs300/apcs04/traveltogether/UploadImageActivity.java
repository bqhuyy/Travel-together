package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

public class UploadImageActivity extends AppCompatActivity {

	private ImageButton mButtonLoadImage;
	private LinearLayout mLayoutChooseImage;
	private ImageView mCaptureView;

	private Bitmap mBitmap;

	private ImageButton mButtonCaptureImg;
	private LinearLayout mLayoutCaptureImage;

	private static int RESULT_LOAD_IMAGE = 1;
	private static int REQUEST_CAPTURE_IMAGE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_image);

		init();

		mLayoutChooseImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				chooseImageFromDevice();
			}
		});

		mLayoutCaptureImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				takeImageFromCamera(view);
			}
		});

	}

	public void init(){
		mButtonLoadImage = (ImageButton) findViewById(R.id.UploadImageActivity_buttonLoadPicture);
		mLayoutChooseImage = (LinearLayout) findViewById(R.id.UploadImageActivity_layout_chooseImg);

		mButtonCaptureImg = (ImageButton) findViewById(R.id.UploadActivity_buttonCaptureImage);
		mLayoutCaptureImage = (LinearLayout) findViewById(R.id.UploadActivity_layout_captureImg);

		mCaptureView = (ImageView) findViewById(R.id.UploadActivity_LoadedImage);

	}

	public void takeImageFromCamera(View view){
		Intent intent = new Intent(this, CameraActivity.class);
		startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
	}

	public void chooseImageFromDevice(){
		Intent itent = new Intent(Intent.ACTION_GET_CONTENT);
		itent.setType("image/*");

		Intent pickitent = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickitent.setType("image/*");

		Intent chooseitent = Intent.createChooser(itent, "Select image");
		chooseitent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickitent});

		startActivityForResult(chooseitent, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Log.d("123", "success");
			Uri imguri = data.getData();
			mCaptureView.setImageURI(imguri);
			try {
				mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imguri);
				new ImageUploader(this).execute(mBitmap);
			}catch(IOException ioe){
				Log.d("error", "Cannot choose image");
			}
		}
		else if(requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK && data != null){

			String image_file_path = data.getStringExtra("Image_file_location");
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			mBitmap = BitmapFactory.decodeFile(image_file_path, bmOptions);
			if(mBitmap != null){
				mCaptureView.setImageBitmap(mBitmap);
				new ImageUploader(this).execute(mBitmap);
			}
			else{
				Toast.makeText(UploadImageActivity.this, "Please choose or capture your image", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
