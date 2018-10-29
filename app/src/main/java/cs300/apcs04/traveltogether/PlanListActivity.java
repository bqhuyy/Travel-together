package cs300.apcs04.traveltogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class PlanListActivity extends AppCompatActivity {

	// variables needed for creating recylerview
	private ArrayList<Plan> mArrayList;
	private PlanAdapter mAdapter;
	private RecyclerView mRecyclerView;

	// varibales need for set gestures of item in recylerview
	private ItemTouchHelper.SimpleCallback mSimpleItemTouchCallback;
	private ItemTouchHelper mItemTouchHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan_list);

		setUpRecylerView();

		setGestureForItemInRecylerview();
	}

	public void setUpRecylerView(){
		mRecyclerView = (RecyclerView) findViewById(R.id.plan_list);
		mArrayList = new ArrayList<>();
		mArrayList.add(new Plan("plan 1", new Date()));
		mArrayList.add(new Plan("plan 2", new Date()));
		mArrayList.add(new Plan("plan 3", new Date()));

		mAdapter = new PlanAdapter(mArrayList, this);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setAdapter(mAdapter);
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
				mAdapter.onItemRemove(viewHolder.getAdapterPosition());

			}
		};

		mItemTouchHelper = new ItemTouchHelper(mSimpleItemTouchCallback);
		mItemTouchHelper.attachToRecyclerView(mRecyclerView);
	}
}
