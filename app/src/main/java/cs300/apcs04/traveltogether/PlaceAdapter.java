package cs300.apcs04.traveltogether;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class PlaceAdapter extends ExpandableRecyclerViewAdapter<PlaceViewHolder, PlaceShortDataViewHolder> {


	public PlaceAdapter(List<? extends ExpandableGroup> groups) {
		super(groups);
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
	}

	@Override
	public void onBindGroupViewHolder(PlaceViewHolder holder, int flatPosition, ExpandableGroup group) {
		holder.setPlaceName(group);
	}
}
