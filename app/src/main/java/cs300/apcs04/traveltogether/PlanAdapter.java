package cs300.apcs04.traveltogether;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> implements ItemTouchHelperAdapter, Filterable{

	private ArrayList<Plan> mPlanList;
	private ArrayList<Plan> mPlanListFiltered;
	private Context mContext;
	private PopupMenu mPopupMenu;


	public PlanAdapter(ArrayList<Plan> data, Context context){
		this.mPlanList = data;
		this.mContext = context;
		this.mPlanListFiltered = data;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.plan_item, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
		final Plan plan = mPlanListFiltered.get(i);
		viewHolder.title.setText(plan.getTitle());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		viewHolder.date.setText(dateFormat.format(plan.getDateAdded()));

		//int index = (int) (Math.random()*5);
		viewHolder.layout.setBackgroundColor(Color.parseColor(viewHolder.getBackgroundColor()[i%7]));

		viewHolder.setItemClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, int position, boolean isLongClick) {
				if(isLongClick){
					//Change Toast later
					Toast.makeText(mContext, "Long clicked", Toast.LENGTH_SHORT).show();

					mPopupMenu = new PopupMenu(mContext,  viewHolder.itemView);
					mPopupMenu.getMenuInflater().inflate(R.menu.pop_up_menu_plan_list, mPopupMenu.getMenu());
					mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem menuItem) {
							// Change Toast later
							Toast.makeText(mContext, "Edit plan clicked", Toast.LENGTH_SHORT).show();
							return true;
						}
					});
					mPopupMenu.show();
				}
				else{
					// Change Toast later
					//Toast.makeText(mContext, "item clicked", Toast.LENGTH_SHORT).show();
					// passing planID to PlaceListInPlanActivity
					String planID = plan.getmPlanID();
					Intent intent = new Intent(view.getContext(), PlaceListInPlanActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("planID", planID);
					mContext.startActivity(intent);
					((Activity)mContext).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left); //start
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return mPlanListFiltered.size();
	}

	@Override
	public boolean onItemMove(int orgigin, int target) {

		if(mPopupMenu != null)
			mPopupMenu.dismiss();

		if(orgigin < target){
			for(int i = orgigin; i < target; i++){
				Collections.swap(mPlanListFiltered, i, i + 1);
			}
		}
		else{
			for(int i = orgigin; i > target; i--){
				Collections.swap(mPlanListFiltered, i, i - 1);
			}
		}
		notifyItemMoved(orgigin, target);
		return true;
	}

	@Override
	public void onItemRemove(int pos) {
		mPlanListFiltered.remove(pos);
		notifyItemRemoved(pos);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

		private TextView title;
		private TextView date;
		private LinearLayout layout;
		private ItemClickListener itemClickListener;
		private String[] BackgroundColor = {"#7c4dff", "#E43F3F", "#3FAED3", "#09D07A", "#D81B60", "#23283a", "#9E9E9E", "#ffb300"};
		/*private int[] BackgroundLayout = {R.drawable.custom_background_list_item_yellow,
										 R.drawable.custom_background_list_item_deeppurple,
										 R.drawable.custom_background_list_item_green,
										 R.drawable.custom_background_list_item_primarydark,
										 R.drawable.custom_background_list_item_red300,
										 R.drawable.custom_background_list_item_redpink};*/

		public String[] getBackgroundColor() {
			return this.BackgroundColor;
		}

		public ViewHolder(View itemView){
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.PlanItem_plan_title);
			date = (TextView) itemView.findViewById(R.id.PlanItem_plan_added_date);
			layout = (LinearLayout) itemView.findViewById(R.id.PlanItem_plan_layout);
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		public void setItemClickListener(ItemClickListener itemClickListener){
			this.itemClickListener = itemClickListener;
		}

		@Override
		public void onClick(View view) {
			itemClickListener.onClick(view, getAdapterPosition(), false);
		}

		@Override
		public boolean onLongClick(View view) {
			itemClickListener.onClick(view, getAdapterPosition(), true);
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
				mPlanListFiltered = mPlanList;
			}
			else{
				ArrayList<Plan> filteredList = new ArrayList<>();
				String filterPattern = charSequence.toString().toLowerCase().trim();

				for(Plan plan : mPlanList){
					if(plan.getTitle().toLowerCase().contains(filterPattern)){
						filteredList.add(plan);
					}
				}
				mPlanListFiltered = filteredList;
			}
			FilterResults results = new FilterResults();
			results.values = mPlanListFiltered;

			return results;
		}

		@Override
		protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
			mPlanListFiltered = (ArrayList<Plan>) filterResults.values;
			notifyDataSetChanged();
		}
	};
}
