package cs300.apcs04.traveltogether;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PlanListActivity extends AppCompatActivity {

	// variables needed for creating recylerview
	private ArrayList<Plan> mArrayList;
	private PlanAdapter mAdapter;
	private RecyclerView mRecyclerView;

	// varibales need for set gestures of item in recylerview
	private ItemTouchHelper.SimpleCallback mSimpleItemTouchCallback;
	private ItemTouchHelper mItemTouchHelper;

	//
	private AlertDialog.Builder mAlert;
	private AlertDialog mAlertDialog;
	private FloatingActionButton mFab;

	//
	private StringBuilder mPlanTitle = new StringBuilder();

	// Firebase databsase
	FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
	//DatabaseReference mRef = mDatabase.getReference("user").child("abcde123");
	DatabaseReference mRefPlan = mDatabase.getReference("plan");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan_list);

		Toolbar toolbar = findViewById(R.id.toolbar_planlist);
		setSupportActionBar(toolbar);

		// toolbar fancy stuff
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("");

		init();

		setUpRecylerView();

		setGestureForItemInRecylerview();
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

	public void getDataFromFireBase(){

		mRefPlan.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for(DataSnapshot PostSnapShot : dataSnapshot.getChildren()){
					Plan plan = PostSnapShot.getValue(Plan.class);
					mArrayList.add(plan);
					mAdapter.notifyDataSetChanged();
				}
				Log.d("Retreving database", "Read database successfully ");
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Log.d("Retreving database", "Read database failed ");
			}
		});
	}

	public void setUpRecylerView(){
		mRecyclerView = (RecyclerView) findViewById(R.id.PlanListActivity_plan_list);
		mArrayList = new ArrayList<>();
		mAdapter = new PlanAdapter(mArrayList, this);

		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setAdapter(mAdapter);

		getDataFromFireBase();

		/*mArrayList.add(new Plan("plan 1", new Date()));
		mArrayList.add(new Plan("plan 2", new Date()));
		mArrayList.add(new Plan("plan 3", new Date()));*/
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
				Toast.makeText(PlanListActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
				//Remove swiped item from list and notify the RecyclerView
				/*int position = viewHolder.getAdapterPosition();
				arrayList.remove(position);
				adapter.notifyDataSetChanged();*/
				Plan plan = mArrayList.get(viewHolder.getAdapterPosition());
				mRefPlan.child(plan.getmPlanID()).removeValue();
				mAdapter.onItemRemove(viewHolder.getAdapterPosition());

			}
		};

		mItemTouchHelper = new ItemTouchHelper(mSimpleItemTouchCallback);
		mItemTouchHelper.attachToRecyclerView(mRecyclerView);
	}

	public void init(){
		mAlert = new AlertDialog.Builder(this);
		alertConfigure();
		mFab = (FloatingActionButton) findViewById(R.id.plan_list_fab);
		mFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mAlertDialog.show();
			}
		});
	}

	private void alertConfigure(){
		final EditText edittext = new EditText(this);
		mAlert.setMessage("Enter plan's title:");
		mAlert.setTitle("Create new plan");

		mAlert.setView(edittext);

		mAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//What ever you want to do with the value
				String title = edittext.getText().toString();
				if(!title.trim().equals("")){
					Plan plan = new Plan(edittext.getText().toString());
					HashMap<String, String> map = new HashMap<>();
					map.put("abcde", "abcde");
					map.put("ghjkl", "ghjkl");
					plan.setmMemberList(map);
					String id = plan.getmPlanID();
					mRefPlan.child(id).setValue(plan);

					mArrayList.add(plan);
					mAdapter.notifyDataSetChanged();
					edittext.setText("");

				}
			}
		});
		mAlertDialog = mAlert.create();
	}
}
