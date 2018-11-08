package cs300.apcs04.traveltogether;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class PlaceAdapter extends ExpandableRecyclerViewAdapter<PlaceViewHolder, PlaceShortDataViewHolder> {

	private Context mContext;


	public PlaceAdapter(List<? extends ExpandableGroup> groups, Context context) {
		super(groups);
		this.mContext = context;
	}

	@Override
	public PlaceViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.place_item, parent, false);
		return new PlaceViewHolder(view);
	}

	@Override
	public PlaceShortDataViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.place_sub_data_item, parent, false);
		return new PlaceShortDataViewHolder(view);
	}

	@Override
	public void onBindChildViewHolder(PlaceShortDataViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
		final PlaceShortData placeShortData = ((Place) group).getItems().get(childIndex);
		holder.setmContent(placeShortData.getmContent());
		holder.setItemClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, int position, boolean isLongClick) {
				Intent i = new Intent(mContext, PlaceDetailActivity.class);
				i.putExtra("placeID", "ChIJUSTY5jcvdTERRVvtbJNZT-g");
				mContext.startActivity(i);
			}
		});
	}

	@Override
	public void onBindGroupViewHolder(final PlaceViewHolder holder, int flatPosition, ExpandableGroup group) {
		holder.setPlaceName(group);
	}
}
