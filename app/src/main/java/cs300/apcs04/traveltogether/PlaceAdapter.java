package cs300.apcs04.traveltogether;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.thoughtbot.expandablerecyclerview.ExpandableListUtils;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaceAdapter extends ExpandableRecyclerViewAdapter<PlaceViewHolder, PlaceShortDataViewHolder> implements Filterable{

	private Context mContext;
	private List<ExpandableGroup> mPlaceFilteredList;

	public PlaceAdapter(List<? extends ExpandableGroup> groups, Context context) {
		super(groups);
		this.mContext = context;
		mPlaceFilteredList = new ArrayList<>(groups);
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
		final Place place = (Place) group;
		holder.setmContent(placeShortData.getmContent());
		holder.setItemClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, int position, boolean isLongClick) {
				Intent i = new Intent(mContext, PlaceDetailActivity.class);
				i.putExtra("placeID", place.getmPlaceId());
				mContext.startActivity(i);
			}
		});
	}

	@Override
	public void onBindGroupViewHolder(final PlaceViewHolder holder, int flatPosition, ExpandableGroup group) {
		holder.setPlaceName(group);
	}

	public void add(ExpandableGroup group) {
		((List<ExpandableGroup>)getGroups()).add(group);
		ExpandableListUtils.notifyGroupDataChanged(this);
		notifyDataSetChanged();
	}

	public boolean onItemMove(int orgigin, int target) {


		if(orgigin < target){
			for(int i = orgigin; i < target; i++){
				Collections.swap(getGroups(), i, i + 1);
			}
		}
		else{
			for(int i = orgigin; i > target; i--){
				Collections.swap(getGroups(), i, i - 1);
			}
		}
		notifyItemMoved(orgigin, target);
		return true;
	}



	public void onItemRemove(int pos) {
		getGroups().remove(pos);
		notifyItemRemoved(pos);
	}

	@Override
	public Filter getFilter() {
		return placeFilter;
	}

	private Filter placeFilter = new Filter() {
		@Override
		protected FilterResults performFiltering(CharSequence charSequence) {

			FilterResults results = new FilterResults();

			if(charSequence == null || charSequence.length() == 0){
				results.values = mPlaceFilteredList;
			}
			else{
				String patternFilter = charSequence.toString().toLowerCase().trim();
				ArrayList<ExpandableGroup> filterResults = new ArrayList<>();
				for(ExpandableGroup group : mPlaceFilteredList){
					Place place = (Place) group;
					if(place.getmName().toLowerCase().contains(patternFilter)){
						filterResults.add(group);
					}
				}
				results.values = filterResults;
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
			List<ExpandableGroup> list = (ArrayList<ExpandableGroup>)filterResults.values;

			((List<ExpandableGroup>)getGroups()).clear();
			for(ExpandableGroup group : list){
				PlaceAdapter.this.add(group);
			}
		}
	};
}
