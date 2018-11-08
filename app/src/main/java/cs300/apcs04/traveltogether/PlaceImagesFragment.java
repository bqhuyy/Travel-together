package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class PlaceImagesFragment extends Fragment{

	protected GeoDataClient mGeoDataClient;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_place_images, container, false);
		final String placeId = (String) getArguments().getString("placeid");

		mGeoDataClient = Places.getGeoDataClient(getContext(), null);
		final GridLayout gridLayout = (GridLayout) v.findViewById(R.id.grid_layout_img);

		final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
		photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
			@Override
			public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
				// Get the list of photos.
				PlacePhotoMetadataResponse photos = task.getResult();
				// Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
				PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
				// Get the first photo in the list.
				int i = 0;
				int size = photoMetadataBuffer.getCount();
				PlacePhotoMetadata photoMetadata;
				while(i < size){
					photoMetadata = photoMetadataBuffer.get(i);
					if(photoMetadata != null) {
						CharSequence attribution = photoMetadata.getAttributions();
						// Get a full-size bitmap for the photo.
						Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
						photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
							@Override
							public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
								PlacePhotoResponse photo = task.getResult();
								final Bitmap bitmap = photo.getBitmap();

								int width = (Resources.getSystem().getDisplayMetrics().widthPixels) / 3;
								final ImageView img = new ImageView(getContext());
								img.setLayoutParams(new LinearLayout.LayoutParams(width, width));
								img.setScaleType(ImageView.ScaleType.FIT_XY);
								img.setImageBitmap(bitmap);
								img.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										ByteArrayOutputStream bytes = new ByteArrayOutputStream();
										Bitmap bm = ((BitmapDrawable) img.getDrawable()).getBitmap();
										bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
										String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bm, "Title", null);
										Uri imgUri = Uri.parse(path);
										Intent intent = new Intent(getContext(), ImageFullscreenActivity.class);
										intent.putExtra("image_uri_view", imgUri.toString());
										startActivity(intent);
									}
								});
								gridLayout.addView(img);
							}
						});
					}
					i++;
				}
				photoMetadataBuffer.release();
			}
		});
		return v;
	}
}
