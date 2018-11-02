package cs300.apcs04.traveltogether;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class PlaceShortDataViewHolder extends ChildViewHolder {

	private TextView mContent;

	public PlaceShortDataViewHolder(View itemView) {
		super(itemView);
		mContent = (TextView) itemView.findViewById(R.id.PlaceSubItem_place_sub_content);
	}

	public void setmContent(String address) {mContent.setText(address);}
}
