package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.IOException;

public class UploadImageActivity extends AppCompatActivity {

	private ImageButton mButtonLoadImage;
	private LinearLayout mLayoutChooseImage;
	private Bitmap mBitmap;
	private static int RESULT_LOAD_IMAGE = 1;

	private ImageView mCaptureView;
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

		Button button = (Button) findViewById(R.id.btntest);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(UploadImageActivity.this, PlanListActivity.class);
				startActivity(intent);
			}
		});

		Button button2 = (Button) findViewById(R.id.btntest2);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(UploadImageActivity.this, PlaceListInPlanActivity.class);
				startActivity(intent);
			}
		});
	}

	public void init(){
		mButtonLoadImage = (ImageButton) findViewById(R.id.buttonLoadPicture);
		mLayoutChooseImage = (LinearLayout) findViewById(R.id.layout_chooseImg);
		mCaptureView = (ImageView) findViewById(R.id.LoadedImage);
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
	}
}
