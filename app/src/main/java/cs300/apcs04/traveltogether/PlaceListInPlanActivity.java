package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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

	private ArrayList<String> mMemList;
	private FirebaseDatabase mDatabase;
	private DatabaseReference mRef = mDatabase.getInstance().getReference("plan").child("07b097ea-1aec-4be5-aa0d-863fad22a1ff"); // change this to planID later

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list_in_plan);

		// Receive planID and collect places data in that plan
		/*Intent intent = getIntent();
		mPlanID = intent.getStringExtra("planID");*/

		mRecylerview = (RecyclerView) findViewById(R.id.place_list_in_plan);
		RecyclerView.ItemAnimator animator = mRecylerview.getItemAnimator();
		if (animator instanceof DefaultItemAnimator) {
			((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
		}


		init();
		getDataFromPlanID(mPlanID);
		setUpRecylerView();
	}

	public void getDataFromPlanID(String PlanID){
		mPlaceList = new ArrayList<>();
		mPlaceShortDataList = new ArrayList();
		mLayoutManager = new LinearLayoutManager(this);

		// this is data of address, weektime. It will be displayed when clicking on arrow down button in one item
		PlaceShortData placeshort = new PlaceShortData("eqweqweq");
		PlaceShortData placeshort2 = new PlaceShortData("vdsfsfsdf");
		mPlaceShortDataList.add(placeshort);
		mPlaceShortDataList.add(placeshort2);

		// Main Place data
		Place place = new Place("wqeqweqsadas", "Truong Gia Dinh", 4, "dsdqqweq", "012587998", "wqeqweqw", false, null, null, mPlaceShortDataList);
		mPlaceList.add(place);
		place = new Place("eherhhfbdb", "Truong Le Hong Phong", 4, "dsdqqweq", "012587998", "wqeqweqw", false, null, null, mPlaceShortDataList);
		mPlaceList.add(place);
	}

	public void setUpRecylerView(){

		mAdapter= new PlaceAdapter(mPlaceList, this);
		mRecylerview.setLayoutManager(mLayoutManager);
		mRecylerview.setAdapter(mAdapter);

	}

	public void init(){

		mMemList = new ArrayList<>();

		mFabAddPlace = (FloatingActionButton) findViewById(R.id.place_list_fab_add_place);
		mFabChat = (FloatingActionButton) findViewById(R.id.place_list_fab_chat);

		mFabChat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//new PutUserID(PlaceListInPlanActivity.this).execute(mRef);
				mRef.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

						long date = (long) dataSnapshot.child("dateAdded").getValue();
						String planid = (String) dataSnapshot.child("mPlanID").getValue();
						String title = (String) dataSnapshot.child("title").getValue();
						HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.child("mMemberList").getValue();
						if(map == null)
							Log.d("qwerty", "nulllll");
						for(String id : map.keySet()){
							mMemList.add(id);
						}

						Intent intent = new Intent(getApplicationContext(), TestActivity.class);
						/*Bundle extras = new Bundle();
						extras.putStringArrayList("listMem", mMemList);
						intent.putExtras(extras);
						startActivity(intent);
						finish();*/
						intent.putStringArrayListExtra("listMem", mMemList);
						startActivity(intent);

					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {

					}
				});
				/*final Handler handler = new Handler();
				final int delay = 1000; //milliseconds


				handler.postDelayed(new Runnable(){
					public void run(){
						if(!mMemList.isEmpty())//checking if the data is loaded or not
						{
							Intent intent = new Intent(getApplicationContext(), TestActivity.class);
							Bundle extras = new Bundle();
							extras.putStringArrayList("listMem", mMemList);
							intent.putExtras(extras);
							startActivity(intent);
							finish();
						}
						else
							handler.postDelayed(this, delay);
					}
				}, delay);*/
			}
		});
	}

}
