package cs300.apcs04.traveltogether;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class PlaceInfoFragment extends Fragment{

	private TextView mphone;
	private TextView mweek_time_txt;
	private TextView mAddress;
	private TextView mopenHours;
	private ImageView mphoneicon;
	private ImageButton mbtnMap;
	private FloatingActionButton mbtnWeb;
	private FloatingActionButton mbtnPhone;
	private FloatingActionButton mbtnSms;

	private MapView mMapView;
	private GoogleMap googleMap;
	private GoogleMap mMap;
	private GeoDataClient mGeoDataClient;
	private AlertDialog.Builder mAlert;
	private AlertDialog mAlertDialog;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_place_info, container, false);


		mGeoDataClient = Places.getGeoDataClient(getContext(), null);

		final Place place = (Place) getArguments().getSerializable("placedata");
		final ArrayList<String> arr_fav_id = (ArrayList<String>) getArguments().getStringArrayList("fav_id");

		mphone = (TextView) v.findViewById(R.id.Phone);
		mopenHours = (TextView) v.findViewById(R.id.openhours);
		mphoneicon = (ImageView) v.findViewById(R.id.phoneicon);
		mAddress = (TextView) v.findViewById(R.id.address);
		mweek_time_txt = (TextView) v.findViewById(R.id.week_time);
		mbtnMap = (ImageButton) v.findViewById(R.id.btnMap);
		mAlert = new AlertDialog.Builder(getContext());
		mbtnWeb = v.findViewById(R.id.web_button);
		mbtnPhone = v.findViewById(R.id.phone_button);
		mbtnSms = v.findViewById(R.id.sms_button);

		if(place != null) {

			mAddress.setText(place.getmAddress());
			mphone.setText(place.getmPhone());
			ArrayList<String> weektime = place.getmWeek_time();
			if (weektime != null) {
				StringBuilder s = new StringBuilder();
				s.append("\n");
				for (int i = 0; i < weektime.size(); i++) {
					s.append(weektime.get(i));
					s.append("\n");
				}
				mweek_time_txt.setText(s.toString());
			} else {
				mweek_time_txt.setText("None");
			}
			Boolean isOpen = place.ismIsOpen();
			if (isOpen == null) {
				mopenHours.setText("None");
				mopenHours.setTextColor(Color.RED);
			} else if (!isOpen) {
				mopenHours.setText("Closed");
				mopenHours.setTextColor(Color.RED);
			} else {
				mopenHours.setText("Open now");
				mopenHours.setTextColor(Color.GREEN);
			}
			LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.type);
			ArrayList<String> type = place.getmType();
			if (type != null) {
				for (int i = 0; i < type.size(); i++) {
					TextView txt_type = new TextView(getContext());
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.setMargins(5, 0, 5, 0);
					txt_type.setLayoutParams(layoutParams);
					txt_type.setTextSize(10);
					txt_type.setPadding(5, 0, 5, 0);
					txt_type.setText(type.get(i));
					txt_type.setTextColor(getResources().getColor(R.color.white));
					txt_type.setBackgroundResource(R.drawable.custom_border_layout);
					linearLayout.addView(txt_type);
				}
			}

			alertConfigure(container, savedInstanceState, place.getmPlaceId());

			mbtnMap.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mAlertDialog.show();
				}
			});

			mbtnWeb.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					String url = place.getmWebsiteURL();
					Uri weburl = Uri.parse(url);
					if (!url.equals("None")) {
						Intent mapIntent = new Intent(Intent.ACTION_VIEW, weburl);
						PackageManager packageManager = getActivity().getPackageManager();
						List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
						boolean isIntentSafe = activities.size() > 0;
						if (isIntentSafe) {
							startActivity(mapIntent);
						}
					} else {
						Snackbar.make(view, "Not available for this location", Snackbar.LENGTH_LONG)
								.setAction("Action", null).show();
					}
				}
			});

			mbtnPhone.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(!place.getmPhone().equals("None")) {
						Uri number = Uri.parse("tel:" + place.getmPhone());

						Intent callIntent = new Intent(Intent.ACTION_CALL, number);
						if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
								!= PackageManager.PERMISSION_GRANTED) {
							return;
						}
						startActivity(callIntent);
					}
					else{
						Snackbar.make(view, "Not available for this location", Snackbar.LENGTH_LONG)
								.setAction("Action", null).show();
					}
				}
			});

			mbtnSms.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (!place.getmPhone().equals("None")){
						Intent smsIntent = new Intent(Intent.ACTION_VIEW);
						smsIntent.setType("vnd.android-dir/mms-sms");
						smsIntent.putExtra("sms_body", "Hi");
						smsIntent.putExtra("address", place.getmPhone());
						startActivity(smsIntent);
					}
					else{
						Snackbar.make(view, "Not available for this location", Snackbar.LENGTH_LONG)
								.setAction("Action", null).show();
					}
				}
			});

		}
		return v;
	}

	private void alertConfigure(ViewGroup container, Bundle savedInstanceState, final String mPlaceID) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View v = inflater.inflate(R.layout.dialog_food_map, container, false);

		mMapView = new MapView(getContext());
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();

		mMapView.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap mMap) {
				googleMap = mMap;

				mGeoDataClient.getPlaceById(mPlaceID).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
					@Override
					public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
						if (task.isSuccessful()) {
							PlaceBufferResponse places = task.getResult();
							com.google.android.gms.location.places.Place myPlace = places.get(0);
							Log.i("place_error", "Place found: " + myPlace.getName());
							LatLng location = myPlace.getLatLng();

							googleMap.addMarker(new MarkerOptions().title(myPlace.getAddress().toString())
									.position(location));
							googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
							places.release();
						} else {
							Log.e("place_error", "Place not found.");
						}
					}
				});
			}
		});

		mAlert.setTitle(mAddress.getText().toString());

		mAlert.setView(mMapView);

		mAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//What ever you want to do with the value

			}
		});
		mAlertDialog = mAlert.create();
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}
}
