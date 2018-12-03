package cs300.apcs04.traveltogether;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> implements ItemTouchHelperAdapter, Filterable{

	private Context mContext;
	private List<Place> mPlaceList;
	private List<Place> mPlaceListFiltered;

	public PlaceAdapter(Context context, List<Place> PlaceList){
		this.mContext = context;
		this.mPlaceList = PlaceList;
		this.mPlaceListFiltered = PlaceList;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		LayoutInflater inflater = LayoutInflater.from(this.mContext);
		View v = inflater.inflate(R.layout.place_item, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

		final Place place = mPlaceListFiltered.get(i);

		//int index =  (int) (Math.random()*5);
		viewHolder.mImageChar.setBackgroundResource(viewHolder.getmCircleColors()[i%5]);
		viewHolder.mFirstChar.setText(String.valueOf(place.getmName().charAt(0)));
		viewHolder.mPlaceName.setText(place.getmName());
		viewHolder.mAddess.setText(place.getmAddress());
		viewHolder.mRating.setRating(place.getmRating());

		viewHolder.setmItemClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, int position, boolean isLongClick) {
				if(!isLongClick){
					String placeID = place.getmPlaceId();
					Intent intent = new Intent(view.getContext(), PlaceDetailActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("placeID", placeID);
					mContext.startActivity(intent);
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return mPlaceListFiltered.size();
	}

	@Override
	public boolean onItemMove(int orgigin, int target) {

		if(orgigin < target){
			for(int i = orgigin; i < target; i++){
				Collections.swap(mPlaceListFiltered, i, i + 1);
			}
		}
		else{
			for(int i = orgigin; i > target; i--){
				Collections.swap(mPlaceListFiltered, i, i - 1);
			}
		}
		notifyItemMoved(orgigin, target);
		return true;
	}

	@Override
	public void onItemRemove(int pos) {
		mPlaceListFiltered.remove(pos);
		notifyItemRemoved(pos);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

		private TextView mPlaceName;
		private TextView mAddess;
		private RatingBar mRating;
		private TextView mFirstChar;
		private ImageView mImageChar;
		private int[] mCircleColors = {R.drawable.circlebackgroundblue,
								 R.drawable.circlebackgroundgreen,
								 R.drawable.circlebackgroundpink,
								 R.drawable.circlebackgroundpurple,
								 R.drawable.circlebackgroundred,
							     R.drawable.circlebackgroundyellow};
		private ItemClickListener  mItemClickListener;

		public ViewHolder(View itemView){
			super(itemView);
			mPlaceName = (TextView) itemView.findViewById(R.id.PlaceItem_place_name);
			mAddess = (TextView) itemView.findViewById(R.id.PlaceItem_place_address);
			mRating = (RatingBar) itemView.findViewById(R.id.PlaceItem_place_rating);
			mImageChar = (ImageView) itemView.findViewById(R.id.PlaceItem_circular_image);
			mFirstChar = (TextView) itemView.findViewById(R.id.PlaceItem_first_char_of_name);
			itemView.setOnLongClickListener(this);
			itemView.setOnClickListener(this);
		}

		public int[] getmCircleColors(){
			return this.mCircleColors;
		}

		public void setmItemClickListener(ItemClickListener itemClickListener){
			this.mItemClickListener = itemClickListener;
		}

		@Override
		public void onClick(View view) {
			mItemClickListener.onClick(view, getAdapterPosition(), false);
		}

		@Override
		public boolean onLongClick(View view) {
			mItemClickListener.onClick(view, getAdapterPosition(), true);
			return true;
		}
	}

	@Override
	public Filter getFilter() {
		return planFilter;
	}

	private Filter planFilter = new Filter() {
		@Override
		protected FilterResults performFiltering(CharSequence charSequence) {

			if(charSequence == null || charSequence.length() == 0){
				mPlaceListFiltered = mPlaceList;
			}
			else{
				ArrayList<Place> filteredList = new ArrayList<>();
				String filterPattern = charSequence.toString().toLowerCase().trim();

				for(Place place : mPlaceList){
					if(place.getmName().toLowerCase().contains(filterPattern)){
						filteredList.add(place);
					}
				}
				mPlaceListFiltered = filteredList;
			}
			FilterResults results = new FilterResults();
			results.values = mPlaceListFiltered;

			return results;
		}

		@Override
		protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
			mPlaceListFiltered = (ArrayList<Place>) filterResults.values;
			notifyDataSetChanged();
		}
	};
}
