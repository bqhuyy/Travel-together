package cs300.apcs04.traveltogether;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUploader extends AsyncTask<Bitmap, Void, String> {

	private ProgressDialog mProgressDialog;
	private ByteArrayOutputStream mbos;
	private byte[] mBitmapdata;
	private String mImgdata;
	private Context mContext;
	private Handler mHandler;
	private static final String URLAPI = "http://210.245.20.101:8000/predict";
	public ImageUploader(Context context){

		mContext = context;
		mHandler = new Handler(context.getMainLooper());
		mProgressDialog = new ProgressDialog(context);
	}

	private void comPressBitmap(Bitmap mBitmap){
		if(mBitmap != null) {
			mbos = new ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, mbos);
			mBitmapdata = mbos.toByteArray();
			mImgdata = Base64.encodeToString(mBitmapdata, Base64.DEFAULT);
		}
	}

	@Override
	protected String doInBackground(Bitmap... bitmaps) {

		Bitmap mBitmap = bitmaps[0];
		//sending image to server
		if(mBitmap != null) {

			new Thread(new Runnable() {
				@Override
				public void run() {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mProgressDialog.setMessage("Uploading, please wait...");
							mProgressDialog.show();
						}
					});
				}
			}).start();

			comPressBitmap(mBitmap);

			StringRequest request = new StringRequest(Request.Method.POST, URLAPI, new Response.Listener<String>() {
				@Override
				public void onResponse(String s) {
					mProgressDialog.dismiss();
					Toast.makeText(mContext, "Uploaded successfully " + s, Toast.LENGTH_LONG).show();
					Intent intent = new Intent(mContext, PlaceDetailActivity.class);
					intent.putExtra("placeID", s);
					mContext.startActivity(intent);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError volleyError) {
					mProgressDialog.dismiss();
					Toast.makeText(mContext, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
				}
			}) {
				//adding parameters to send
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					if (mImgdata != null) {
						Map<String, String> parameters = new HashMap<String, String>();
						parameters.put("img", mImgdata);
						return parameters;
					}
					return null;
				}
			};

			RequestQueue rQueue = Volley.newRequestQueue(mContext);
			rQueue.add(request);
		/*Integer tmp = mImgdata.length();
		Log.d("quyen", String.valueOf(tmp));*/
		}

		return mImgdata;
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
		if(mImgdata == null){
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mContext, "Please choose or capture your image", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
