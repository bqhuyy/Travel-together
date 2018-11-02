package cs300.apcs04.traveltogether;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> implements ItemTouchHelperAdapter{

	private ArrayList<Plan> mPlanList;
	private Context mContext;
	private PopupMenu mPopupMenu;

	public PlanAdapter(ArrayList<Plan> data, Context context){
		this.mPlanList = data;
		this.mContext = context;
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
		final Plan plan = mPlanList.get(i);
		viewHolder.title.setText(plan.getTitle());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		viewHolder.date.setText(dateFormat.format(plan.getDateAdded()));

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

				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return mPlanList.size();
	}

	@Override
	public boolean onItemMove(int orgigin, int target) {

		if(mPopupMenu != null)
			mPopupMenu.dismiss();

		if(orgigin < target){
			for(int i = orgigin; i < target; i++){
				Collections.swap(mPlanList, i, i + 1);
			}
		}
		else{
			for(int i = orgigin; i > target; i--){
				Collections.swap(mPlanList, i, i - 1);
			}
		}
		notifyItemMoved(orgigin, target);
		return true;
	}

	@Override
	public void onItemRemove(int pos) {
		mPlanList.remove(pos);
		notifyItemRemoved(pos);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

		private TextView title;
		private TextView date;
		private ItemClickListener itemClickListener;

		public ViewHolder(View itemView){
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.PlanItem_plan_title);
			date = (TextView) itemView.findViewById(R.id.PlanItem_plan_added_date);
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
}
