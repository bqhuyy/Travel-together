package cs300.apcs04.traveltogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlaceListInPlanActivity extends AppCompatActivity {

	private RecyclerView mRecylerview;
	private PlaceAdapter mAdapter;
	private List<Place> mPlaceList;
	private LinearLayoutManager mLayoutManager;
	private ArrayList<PlaceShortData> mPlaceShortDataList;
	private String mPlanID = "";
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

		mAdapter= new PlaceAdapter(mPlaceList);
		mRecylerview.setLayoutManager(mLayoutManager);
		mRecylerview.setAdapter(mAdapter);

	}
}
