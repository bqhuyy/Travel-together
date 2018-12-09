package cs300.apcs04.traveltogether;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PlaceListInPlanActivity extends AppCompatActivity {

	private RecyclerView mRecylerview;
	private PlaceAdapter mAdapter;
	private List<Place> mPlaceList;
	private LinearLayoutManager mLayoutManager;
	private ArrayList<PlaceShortData> mPlaceShortDataList;
	private String mPlanID = "";
	private FloatingActionButton mFabAddPlace;
	private FloatingActionButton mFabChat;
	private FloatingActionButton mFabNote;
	private FloatingActionButton mFabAddMember;
	private ItemTouchHelper.SimpleCallback mSimpleItemTouchCallback;
	private ItemTouchHelper mItemTouchHelper;

	private AlertDialog.Builder mAlert;
	private AlertDialog mAlertDialog;

	private ArrayList<String> mMemList;
	private FirebaseDatabase mDatabase;
	private DatabaseReference mRef = mDatabase.getInstance().getReference("plan"); // change this to planID later
	private DatabaseReference mRefPlace = mDatabase.getInstance().getReference("place");

	private static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

	private String mAPI_KEY = "AIzaSyB83QGQwOxKEiC2KMOKLiGK4nw5gMnXC14";
	private String mPlaceDetailURL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
	private String HostAPI = "http://210.245.20.101:8000/place?placeid=";

	private PlaceJSONParser mPlaceParser = new PlaceJSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list_in_plan);

		// Receive planID and collect places data in that plan
		/*Intent intent = getIntent();
		mPlanID = intent.getStringExtra("planID");*/

		Toolbar toolbar = findViewById(R.id.toolbar_placelist);
		setSupportActionBar(toolbar);



		// toolbar fancy stuff
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("");

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PlaceListInPlanActivity.this.finish();
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //finish
			}
		});
		mRecylerview = (RecyclerView) findViewById(R.id.place_list_in_plan);
		RecyclerView.ItemAnimator animator = mRecylerview.getItemAnimator();
		if (animator instanceof DefaultItemAnimator) {
			((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
		}

		Intent intent = getIntent();
		mPlanID = intent.getStringExtra("planID");

		init();
		setGestureForItemInRecylerview();
		setUpRecylerView();
		getDataFromPlanID(mPlanID);
	}

	public void getDataFromPlanID(String PlanID){


		ValueEventListener placeListener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				// Get Post object and use the values to update the UI

				DataSnapshot PlaceIDDataList = dataSnapshot.child("mPlaceList");

				final HashMap<String, String> placeIDMap = (HashMap<String, String>) PlaceIDDataList.getValue();

				mRefPlace.addChildEventListener(new ChildEventListener() {
					@Override
					public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

						String mPlaceID = dataSnapshot.child("mPlaceId").getValue(String.class);
						if(placeIDMap != null && placeIDMap.containsKey(mPlaceID)){
							/*String address = dataSnapshot.child("items").child("0").child("mContent").getValue(String.class);
							String week_time_txt = dataSnapshot.child("items").child("1").child("mContent").getValue(String.class);*/


							String mName = dataSnapshot.child("mName").getValue(String.class);
							float mRating = dataSnapshot.child("mRating").getValue(Float.class);
							String mAddress = dataSnapshot.child("mAddress").getValue(String.class);
							String mPhone = dataSnapshot.child("mPhone").getValue(String.class);
							String mWebsiteURL = dataSnapshot.child("mWebsiteURL").getValue(String.class);
							Boolean mIsOpen = dataSnapshot.child("mIsOpen").getValue(Boolean.class);
							ArrayList<String> mWeektime = (ArrayList<String>) dataSnapshot.child("mWeek_time").getValue();
							ArrayList<String> mType = (ArrayList<String>) dataSnapshot.child("mType").getValue();

							/*ArrayList<PlaceShortData> PlaceExpandData = new ArrayList<>();
							PlaceShortData ExpandAddress = new PlaceShortData(address);
							PlaceShortData ExpandWeekTime = new PlaceShortData(week_time_txt);
							PlaceExpandData.add(ExpandAddress);
							PlaceExpandData.add(ExpandWeekTime);*/

							Place p = new Place(mPlaceID, mName, mRating, mAddress, mPhone, mWebsiteURL, mIsOpen, mWeektime, mType);
							mPlaceList.add(p);
							mAdapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

					}

					@Override
					public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

					}

					@Override
					public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {

					}
				});
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				// Getting Post failed, log a message

				// ...
			}
		};
		mRef.child(mPlanID).addListenerForSingleValueEvent(placeListener);
	}

	public void setUpRecylerView(){

		mPlaceList = new ArrayList<>();
		mPlaceShortDataList = new ArrayList();
		mLayoutManager = new LinearLayoutManager(this);

		mAdapter= new PlaceAdapter(this, mPlaceList);
		mRecylerview.setLayoutManager(mLayoutManager);
		mRecylerview.setAdapter(mAdapter);

		/*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecylerview.getContext(),
				mLayoutManager.getOrientation());
		mRecylerview.addItemDecoration(dividerItemDecoration);*/

	}



	public void init(){

		mMemList = new ArrayList<>();

		mFabAddPlace = (FloatingActionButton) findViewById(R.id.place_list_fab_add_place);
		mFabChat = (FloatingActionButton) findViewById(R.id.place_list_fab_chat);
		mFabNote = (FloatingActionButton) findViewById(R.id.place_list_fab_note);
		mFabAddMember = (FloatingActionButton) findViewById(R.id.place_list_fab_add_member);

		mAlert = new AlertDialog.Builder(this);
		AlertConfigure();
		mFabAddMember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mAlertDialog.show();
			}
		});

		/*mFabChat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//new PutUserID(PlaceListInPlanActivity.this).execute(mRef);
				mRef.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

						/long date = (long) dataSnapshot.child("dateAdded").getValue();
						String planid = (String) dataSnapshot.child("mPlanID").getValue();
						String title = (String) dataSnapshot.child("title").getValue();
						HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.child("mMemberList").getValue();
						if(map == null)
							Log.d("qwerty", "nulllll");
						for(String id : map.keySet()){
							mMemList.add(id);
						}

						Intent intent = new Intent(getApplicationContext(), TestActivity.class);
						intent.putStringArrayListExtra("listMem", mMemList);
						startActivity(intent);

					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {

					}
				});
				Intent intent = new Intent(PlaceListInPlanActivity.this, GroupChatActivity.class);
				intent.putExtra("planID", mPlanID);
				startActivity(intent);
			}
		});


		mFabAddPlace.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					Intent intent =
							new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
									.build(PlaceListInPlanActivity.this);
					startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
				} catch (GooglePlayServicesRepairableException e) {
					// TODO: Handle the error.
				} catch (GooglePlayServicesNotAvailableException e) {
					// TODO: Handle the error.
				}
			}
		});*/
	}

	private void AlertConfigure() {
		final EditText editText = new EditText(this);
		mAlert.setTitle("Add new member");
		mAlert.setMessage("Enter email of the one you want to add:");
		mAlert.setView(editText);
		mAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				String emailAdded = editText.getText().toString();
				addMemberWithEmail(emailAdded);
			}
		});
		mAlertDialog = mAlert.create();
	}

	private void addMemberWithEmail(String emailAdded) {
		DatabaseReference findingUser = FirebaseDatabase.getInstance().getReference("user");
		Query findUser = findingUser.orderByChild("email").equalTo(emailAdded);
		findUser.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot addedUser : dataSnapshot.getChildren())
				{
					String addedUserID = addedUser.getKey();
					addPlanToUserWithEmail(addedUserID);
					DatabaseReference thisPlan=mRef.child(mPlanID);
					thisPlan.child("mMemberList").child(addedUserID).setValue(addedUserID);
				}
			}

			private void addPlanToUserWithEmail(String addedUserID) {
				DatabaseReference thisUser = FirebaseDatabase.getInstance().getReference().child("user").child(addedUserID);
				thisUser.child("mPlans").child(mPlanID).setValue(mPlanID);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public void onFabButtonClicked(View view) {

		int id = view.getId();

		if(id == R.id.place_list_fab_chat){
			Intent intent = new Intent(PlaceListInPlanActivity.this, GroupChatActivity.class);
			intent.putExtra("planID", mPlanID);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left); //start
		}
		else if(id == R.id.place_list_fab_note){
			Intent intent = new Intent(PlaceListInPlanActivity.this, Note_Main_Activity.class);
			intent.putExtra("planID", mPlanID);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left); //start
		}
		else{
			try {
				Intent intent =
						new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
								.build(PlaceListInPlanActivity.this);
				startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
			} catch (GooglePlayServicesRepairableException e) {
				// TODO: Handle the error.
			} catch (GooglePlayServicesNotAvailableException e) {
				// TODO: Handle the error.
			}
		}
	}

	public void setGestureForItemInRecylerview(){
		mSimpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
				return true;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
				Toast.makeText(PlaceListInPlanActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
				//Remove swiped item from list and notify the RecyclerView
				/*int position = viewHolder.getAdapterPosition();
				arrayList.remove(position);
				adapter.notifyDataSetChanged();*/
				Place place = (Place) mPlaceList.get(viewHolder.getAdapterPosition());

				mRef.child(mPlanID).child("mPlaceList").child(place.getmPlaceId()).removeValue();
				mAdapter.onItemRemove(viewHolder.getAdapterPosition());

			}
		};

		mItemTouchHelper = new ItemTouchHelper(mSimpleItemTouchCallback);
		mItemTouchHelper.attachToRecyclerView(mRecylerview);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				com.google.android.gms.location.places.Place place = PlaceAutocomplete.getPlace(this, data);
				Log.i("place_auto", "Place: " + place.getName());
				new DataGetter().execute(HostAPI + place.getId() + "&language=en" + "&key=" + mAPI_KEY);
			} else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
				Status status = PlaceAutocomplete.getStatus(this, data);
				// TODO: Handle the error.
				Log.i("place_auto", status.getStatusMessage());

			} else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
			}
		}
	}

	public class DataGetter extends AsyncTask<String , Void , Place> {

		@Override
		protected Place doInBackground(String... params) {

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

				Place place= mPlaceParser.parse(sb.toString());
				Log.d("data: ", place.getmName());
				return place;

			} catch (MalformedURLException me) {
				Log.d("123", "Malformed");
			} catch (IOException ioe) {
				Log.d("456", "IOE");
			}  catch (Exception e) {
				Log.d("789", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Place p) {

			super.onPostExecute(p);
			final Place place = p;
			if(p != null){
				mPlaceList.add(p);
				mAdapter.notifyDataSetChanged();
				final String id = p.getmPlaceId();
				mRef.child(mPlanID).child("mPlaceList").child(id).setValue(id);
				mRefPlace.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						if(!dataSnapshot.hasChild(id)){
							mRefPlace.child(id).setValue(place);
						}
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {

					}
				});
			}
			else{
				Toast.makeText(PlaceListInPlanActivity.this, "Can not add this place!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_menu, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) searchItem.getActionView();

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				mAdapter.getFilter().filter(s);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				mAdapter.getFilter().filter(s);
				return false;
			}
		});

		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //finish
	}
}
