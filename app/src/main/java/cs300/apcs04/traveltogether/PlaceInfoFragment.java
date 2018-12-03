package cs300.apcs04.traveltogether;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaceInfoFragment extends Fragment{

	private TextView mphone;
	private TextView mweek_time_txt;
	private TextView mAddress;
	private TextView mopenHours;
	private ImageView mphoneicon;
	private ImageButton mbtnShare;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_place_info, container, false);

		final Place place = (Place) getArguments().getSerializable("placedata");
		final ArrayList<String> arr_fav_id = (ArrayList<String>) getArguments().getStringArrayList("fav_id");

		mphone = (TextView) v.findViewById(R.id.Phone);
		mopenHours = (TextView) v.findViewById(R.id.openhours);
		mphoneicon = (ImageView) v.findViewById(R.id.phoneicon);
		mAddress = (TextView) v.findViewById(R.id.address);
		mweek_time_txt = (TextView) v.findViewById(R.id.week_time);
		mbtnShare = (ImageButton) v.findViewById(R.id.btnShare);

		if(place != null) {

			mAddress.setText(place.getmAddress());
			mphone.setText(place.getmPhone());
			ArrayList<String> weektime = place.getmWeek_time();
			if (weektime != null) {
				StringBuilder s = new StringBuilder();
				s.append("\n");
				for (int i = 0; i < weektime.size(); i++) {
					s.append(weektime.get(i));
					s.append("\n");
				}
				mweek_time_txt.setText(s.toString());
			} else {
				mweek_time_txt.setText("None");
			}
			Boolean isOpen = place.ismIsOpen();
			if (isOpen == null) {
				mopenHours.setText("None");
				mopenHours.setTextColor(Color.RED);
			} else if (!isOpen) {
				mopenHours.setText("Closed");
				mopenHours.setTextColor(Color.RED);
			} else {
				mopenHours.setText("Open now");
				mopenHours.setTextColor(Color.GREEN);
			}
			LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.type);
			ArrayList<String> type = place.getmType();
			if (type != null) {
				for (int i = 0; i < type.size(); i++) {
					TextView txt_type = new TextView(getContext());
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.setMargins(5, 0, 5, 0);
					txt_type.setLayoutParams(layoutParams);
					txt_type.setTextSize(10);
					txt_type.setPadding(5, 0, 5, 0);
					txt_type.setText(type.get(i));
					txt_type.setTextColor(getResources().getColor(R.color.white));
					txt_type.setBackgroundResource(R.drawable.custom_border_layout);
					linearLayout.addView(txt_type);
				}
			}


		}
		return v;
	}
}
