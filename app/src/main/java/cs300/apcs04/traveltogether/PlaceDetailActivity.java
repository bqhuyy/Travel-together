package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback{

	private TabLayout tabLayout;
	private ViewPager pager;
	private cs300.apcs04.traveltogether.Place mPlace;

	private GoogleMap mMap;
	private GeoDataClient mGeoDataClient;

	private String mAPI_KEY = "AIzaSyB83QGQwOxKEiC2KMOKLiGK4nw5gMnXC14";
	private String mPlaceDetailURL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
	private String HostAPI = "http://210.245.20.101:8000/place?placeid=";

	private CollapsingToolbarLayout mCollapsingToolbarLayout;
	private Toolbar mToolbar;

	private String mPlaceID = "";
	private ArrayList<String> mArrayListFavID;
	private ArrayList<ReviewData> mArrayListReviews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);

		mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

		/*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);*/

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PlaceDetailActivity.this.finish();
			}
		});

		mGeoDataClient = Places.getGeoDataClient(this, null);

		getDataFromAPI();
	}

	public void getDataFromAPI()
	{
		Intent intent = getIntent();
		String placeid = intent.getStringExtra("placeID");
		mPlaceID = new String(placeid);
		Log.d("url", mPlaceDetailURL + mPlaceID + "&language=en" + "&key=" + mAPI_KEY);
		new DataGetter().execute(HostAPI + mPlaceID + "&language=en" + "&key=" + mAPI_KEY);

	}

	public void createViewInfo(String StringJSONData) {

		PlaceJSONParser PlaceParser = new PlaceJSONParser();
		ReviewJSONParser ReviewParser = new ReviewJSONParser();

		mPlace = PlaceParser.parse(StringJSONData);
		if(mPlace != null){
			mToolbar.setTitle(mPlace.getmName());
			mCollapsingToolbarLayout.setTitle(mPlace.getmName());
			mArrayListReviews = ReviewParser.parse(StringJSONData);
			float rating = ReviewParser.getRating();

			pager = (ViewPager) findViewById(R.id.view_pager_place_info);
			tabLayout = (TabLayout) findViewById(R.id.tab_layout_place_info);
			FragmentManager manager = getSupportFragmentManager();
			PlacePagerAdapter adapter = new PlacePagerAdapter(manager, mPlace, mArrayListFavID, mArrayListReviews, rating);
			pager.setAdapter(adapter);
			tabLayout.setupWithViewPager(pager);
			pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
			tabLayout.setTabsFromPagerAdapter(adapter);
		}
		else{
			Toast.makeText(this, "Cannot get information from this place", Toast.LENGTH_LONG).show();
			this.finish();
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		moveCameraToThisLocation();
	}

	private void moveCameraToThisLocation() {
		mGeoDataClient.getPlaceById(mPlaceID).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
			@Override
			public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
				if (task.isSuccessful()) {
					PlaceBufferResponse places = task.getResult();
					Place myPlace = places.get(0);
					Log.i("place_error", "Place found: " + myPlace.getName());
					LatLng location = myPlace.getLatLng();

					mMap.addMarker(new MarkerOptions().title(myPlace.getAddress().toString())
							.position(location));
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
					places.release();
				} else {
					Log.e("place_error", "Place not found.");
				}
			}
		});
	}

	public class DataGetter extends AsyncTask<String , Void , StringBuilder> {

		@Override
		protected StringBuilder doInBackground(String... params) {

			String link = params[0];

			try {
				Log.e("tryyyy", "rrrrrrrrrrrrr");
				URL url = new URL(link);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
				BufferedReader br = new BufferedReader(inputStreamReader);
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					line = br.readLine();
				}

				JSONObject jsonObject = new JSONObject(sb.toString());

				/*JSONObject result = jsonObject.getJSONObject("result");

				if (result.has("website")) {
					mPlaceWebsite.append(result.getString("website"));
				}
				else{
					mPlaceWebsite.append("None");
				}
				mPlaceName.append(result.getString("name"));*/
				Log.d("jda", sb.toString());
				return sb;
			} catch (MalformedURLException me) {
				Log.d("123", "Malformed");
			} catch (IOException ioe) {
				Log.d("456", "IOE");
			}
			catch (Exception e) {
				Log.d("789", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(StringBuilder s) {
			super.onPostExecute(s);
			createViewInfo(s.toString());
		}
	}
}
