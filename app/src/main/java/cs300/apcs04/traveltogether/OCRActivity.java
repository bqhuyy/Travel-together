package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OCRActivity extends AppCompatActivity {

	private TessBaseAPI m_tess;
	private ImageView mCaptureView;
	private String mImageFileLocation = "" ;
	private Bitmap mBitmap;
	private final int REQUEST_CAPTURE_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ocr);

		mCaptureView = (ImageView) findViewById(R.id.img_input);
		//mCaptureView.setImageResource(R.drawable.ddddd);
		prepareLanguageDir();
		m_tess = new TessBaseAPI();
		m_tess.init(String.valueOf(getFilesDir()), "vie");

		Button btn = (Button) findViewById(R.id.btn_rec);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OCRActivity.this, CameraActivity.class);
				startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left); //start

			}
		});
	}

	private void copyFile(){
		AssetManager assMng = getAssets();
		try {
			InputStream is = assMng.open("tessdata/vie.traineddata");
			OutputStream os = new FileOutputStream(getFilesDir()+"/tessdata/vie.traineddata");
			byte[] buffer = new byte[1024];
			int read;
			while((read=is.read(buffer))!=-1){
				os.write(buffer,0,read);
			}
			is.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareLanguageDir(){

		File dir = new File(getFilesDir() + "/tessdata");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File trainedData = new File(getFilesDir()+"/tessdata/vie.traineddata");
		if (!trainedData.exists()){
			copyFile();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK && data != null){
			String image_file_path = data.getStringExtra("Image_file_location");
			if(image_file_path == null){
				Toast.makeText(OCRActivity.this, "Please capture your image", Toast.LENGTH_SHORT).show();
			}
			else{
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				mBitmap = BitmapFactory.decodeFile(image_file_path, bmOptions);

				if(mBitmap != null){
					/*Picasso.get().load(new File(image_file_path)).into(mCaptureView);
					m_tess.setImage(mBitmap);*/
					Picasso.get().load(R.drawable.blockchain).into(mCaptureView);
					m_tess.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.blockchain));
					String result = m_tess.getUTF8Text().toLowerCase();
					TextView resultView = (TextView) findViewById(R.id.txt_result);
					resultView.setText(result);

					//mo web google trans
					Intent webIntent = new Intent(Intent.ACTION_VIEW);
					webIntent.setData(Uri.parse("https://translate.google.com/#vi/en/" + urlEscape(result).trim()));
					startActivity(webIntent);
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left); //start
					deleteImage(image_file_path);
				}
			}
		}
	}

	public void deleteImage(String ImageFileLocation) {
		File fdelete = new File(ImageFileLocation);
		if (fdelete.exists()) {
			if (fdelete.delete()) {
				Log.e("-->", "file Deleted :" + ImageFileLocation);
				callBroadCast();
			} else {
				Log.e("-->", "file not Deleted :" + ImageFileLocation);
			}
		}
		else
			Log.e("-->", "file not exists " + ImageFileLocation);
	}

	public void callBroadCast() {
		if (Build.VERSION.SDK_INT >= 14) {
			Log.e("-->", " >= 14");
			MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
				/*
				 *   (non-Javadoc)
				 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
				 */
				public void onScanCompleted(String path, Uri uri) {
					Log.e("ExternalStorage", "Scanned " + path + ":");
					Log.e("ExternalStorage", "-> uri=" + uri);
				}
			});
		} else {
			Log.e("-->", " < 14");
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		}
	}

	private static String urlEscape(String toEscape){
		//if null, keep null (no gain or loss of safety)
		if (toEscape==null)
			return null;

		StringBuilder sb=new StringBuilder();
		for (char character: toEscape.toCharArray())//for every character in the string
			switch (character){//if the character needs to be escaped, add its escaped value to the StringBuilder
				case '!': sb.append("%21"); continue;
				case '#': sb.append("%23"); continue;
				case '$': sb.append("%24"); continue;
				case '&': sb.append("%26"); continue;
				case '\'': sb.append("%27"); continue;
				case '(': sb.append("%28"); continue;
				case ')': sb.append("%29"); continue;
				case '*': sb.append("%2A"); continue;
				case '+': sb.append("%2B"); continue;
				case ',': sb.append("%2C"); continue;
				case '/': sb.append("%2F"); continue;
				case ':': sb.append("%3A"); continue;
				case ';': sb.append("%3B"); continue;
				case '=': sb.append("%3D"); continue;
				case '?': sb.append("%3F"); continue;
				case '@': sb.append("%40"); continue;
				case '[': sb.append("%5B"); continue;
				case ']': sb.append("%5D"); continue;
				case ' ': sb.append("%20"); continue;
				case '"': sb.append("%22"); continue;
				case '%': sb.append("%25"); continue;
				case '-': sb.append("%2D"); continue;
				case '.': sb.append("%2E"); continue;
				case '<': sb.append("%3C"); continue;
				case '>': sb.append("%3E"); continue;
				case '\\': sb.append("%5C"); continue;
				case '^': sb.append("%5E"); continue;
				case '_': sb.append("%5F"); continue;
				case '`': sb.append("%60"); continue;
				case '{': sb.append("%7B"); continue;
				case '|': sb.append("%7C"); continue;
				case '}': sb.append("%7D"); continue;
				case '~': sb.append("%7E"); continue;
				case '\n': sb.append("%0A"); continue;
				default: sb.append(character);//if it does not need to be escaped, add the character itself to the StringBuilder
			}
		return sb.toString();//build the string, and return
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //finish
	}
}
