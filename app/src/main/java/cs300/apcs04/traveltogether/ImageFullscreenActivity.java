package cs300.apcs04.traveltogether;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageFullscreenActivity extends AppCompatActivity {

	private Uri imgUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_image_fullscreen);

		Intent i = getIntent();
		imgUri = Uri.parse(i.getStringExtra("image_uri_view"));

		ImageView img = new ImageView(this);
		img.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		Picasso.get().load(imgUri).into(img);

		LinearLayout linearLayout = findViewById(R.id.layout_img);
		linearLayout.addView(img);
	}

	@Override
	protected void onStop() {
		super.onStop();
		deleteImage(imgUri);
	}

	private String getUriRealPath(Context ctx, Uri uri)
	{
		String ret = "";

		if( isAboveKitKat() )
		{
			// Android OS above sdk version 19.
			ret = getUriRealPathAboveKitkat(ctx, uri);
		}else
		{
			// Android OS below sdk version 19
			ret = getImageRealPath(getContentResolver(), uri, null);
		}

		return ret;
	}

	private String getUriRealPathAboveKitkat(Context ctx, Uri uri)
	{
		String ret = "";

		if(ctx != null && uri != null) {

			if(isContentUri(uri))
			{
				if(isGooglePhotoDoc(uri.getAuthority()))
				{
					ret = uri.getLastPathSegment();
				}else {
					ret = getImageRealPath(getContentResolver(), uri, null);
				}
			}else if(isFileUri(uri)) {
				ret = uri.getPath();
			}else if(isDocumentUri(ctx, uri)){

				// Get uri related document id.
				String documentId = DocumentsContract.getDocumentId(uri);

				// Get uri authority.
				String uriAuthority = uri.getAuthority();

				if(isMediaDoc(uriAuthority))
				{
					String idArr[] = documentId.split(":");
					if(idArr.length == 2)
					{
						// First item is document type.
						String docType = idArr[0];

						// Second item is document real id.
						String realDocId = idArr[1];

						// Get content uri by document type.
						Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
						if("image".equals(docType))
						{
							mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
						}else if("video".equals(docType))
						{
							mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
						}else if("audio".equals(docType))
						{
							mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
						}

						// Get where clause with real document id.
						String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

						ret = getImageRealPath(getContentResolver(), mediaContentUri, whereClause);
					}

				}else if(isDownloadDoc(uriAuthority))
				{
					// Build download uri.
					Uri downloadUri = Uri.parse("content://downloads/public_downloads");

					// Append download document id at uri end.
					Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

					ret = getImageRealPath(getContentResolver(), downloadUriAppendId, null);

				}else if(isExternalStoreDoc(uriAuthority))
				{
					String idArr[] = documentId.split(":");
					if(idArr.length == 2)
					{
						String type = idArr[0];
						String realDocId = idArr[1];

						if("primary".equalsIgnoreCase(type))
						{
							ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
						}
					}
				}
			}
		}

		return ret;
	}

	/* Check whether current android os version is bigger than kitkat or not. */
	private boolean isAboveKitKat()
	{
		boolean ret = false;
		ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		return ret;
	}

	/* Check whether this uri represent a document or not. */
	private boolean isDocumentUri(Context ctx, Uri uri)
	{
		boolean ret = false;
		if(ctx != null && uri != null) {
			ret = DocumentsContract.isDocumentUri(ctx, uri);
		}
		return ret;
	}

	/* Check whether this uri is a content uri or not.
	 *  content uri like content://media/external/images/media/1302716
	 *  */
	private boolean isContentUri(Uri uri)
	{
		boolean ret = false;
		if(uri != null) {
			String uriSchema = uri.getScheme();
			if("content".equalsIgnoreCase(uriSchema))
			{
				ret = true;
			}
		}
		return ret;
	}

	/* Check whether this uri is a file uri or not.
	 *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
	 * */
	private boolean isFileUri(Uri uri)
	{
		boolean ret = false;
		if(uri != null) {
			String uriSchema = uri.getScheme();
			if("file".equalsIgnoreCase(uriSchema))
			{
				ret = true;
			}
		}
		return ret;
	}


	/* Check whether this document is provided by ExternalStorageProvider. */
	private boolean isExternalStoreDoc(String uriAuthority)
	{
		boolean ret = false;

		if("com.android.externalstorage.documents".equals(uriAuthority))
		{
			ret = true;
		}

		return ret;
	}

	/* Check whether this document is provided by DownloadsProvider. */
	private boolean isDownloadDoc(String uriAuthority)
	{
		boolean ret = false;

		if("com.android.providers.downloads.documents".equals(uriAuthority))
		{
			ret = true;
		}

		return ret;
	}

	/* Check whether this document is provided by MediaProvider. */
	private boolean isMediaDoc(String uriAuthority)
	{
		boolean ret = false;

		if("com.android.providers.media.documents".equals(uriAuthority))
		{
			ret = true;
		}

		return ret;
	}

	/* Check whether this document is provided by google photos. */
	private boolean isGooglePhotoDoc(String uriAuthority)
	{
		boolean ret = false;

		if("com.google.android.apps.photos.content".equals(uriAuthority))
		{
			ret = true;
		}

		return ret;
	}

	/* Return uri represented document file real local path.*/
	private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause)
	{
		String ret = "";

		// Query the uri with condition.
		Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

		if(cursor!=null)
		{
			boolean moveToFirst = cursor.moveToFirst();
			if(moveToFirst)
			{

				// Get columns name by uri type.
				String columnName = MediaStore.Images.Media.DATA;

				if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
				{
					columnName = MediaStore.Images.Media.DATA;
				}else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
				{
					columnName = MediaStore.Audio.Media.DATA;
				}else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
				{
					columnName = MediaStore.Video.Media.DATA;
				}

				// Get column index.
				int imageColumnIndex = cursor.getColumnIndex(columnName);

				// Get column value which is the uri related file local path.
				ret = cursor.getString(imageColumnIndex);
			}
		}

		return ret;
	}


	public void deleteImage(Uri imgUri) {
		String file_dj_path = getUriRealPath(this, imgUri);
		File fdelete = new File(file_dj_path);
		if (fdelete.exists()) {
			if (fdelete.delete()) {
				Log.e("-->", "file Deleted :" + file_dj_path);
				callBroadCast();
			} else {
				Log.e("-->", "file not Deleted :" + file_dj_path);
			}
		}
		else
			Log.e("-->", "file not exists " + file_dj_path);
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
}
