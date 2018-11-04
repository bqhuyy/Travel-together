package cs300.apcs04.traveltogether;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class PlaceShortDataViewHolder extends ChildViewHolder implements  View.OnClickListener, View.OnLongClickListener{

	private TextView mContent;
	private ItemClickListener mitemClickListener;

	public PlaceShortDataViewHolder(View itemView) {
		super(itemView);
		mContent = (TextView) itemView.findViewById(R.id.PlaceSubItem_place_sub_content);

		itemView.setOnClickListener(this);
		itemView.setOnLongClickListener(this);
	}

	public void setmContent(String address) {mContent.setText(address);}

	public void setItemClickListener(ItemClickListener itemClickListener){
		this.mitemClickListener = itemClickListener;
	}

	@Override
	public void onClick(View view) {
		mitemClickListener.onClick(view, getAdapterPosition(), false);
	}

	@Override
	public boolean onLongClick(View view) {
		mitemClickListener.onClick(view, getAdapterPosition(), true);
		return true;
	}
}
