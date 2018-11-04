package cs300.apcs04.traveltogether;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class PlacePagerAdapter extends FragmentPagerAdapter {

	private Place placedata;
	private ArrayList<String> arrayListFavID;

	public PlacePagerAdapter(FragmentManager fm, Place placedata, ArrayList<String> arrayListFavID) {
		super(fm);
		this.placedata = placedata;
		this.arrayListFavID = arrayListFavID;
		Log.d("kkkk", placedata.getmName());
	}

	@Override
	public Fragment getItem(int i) {
		Fragment frag = null;
		Bundle bundle = new Bundle();


		switch (i){
			case 0:
				frag = new PlaceInfoFragment();
				bundle.putSerializable("placedata", placedata);
				bundle.putStringArrayList("fav_id", arrayListFavID);
				frag.setArguments(bundle);
				break;
			case 1:
				frag = new PlaceImagesFragment();
				bundle.putString("placeID", placedata.getmPlaceId());
				frag.setArguments(bundle);
				break;
			case 2:
				break;
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		String title = "";
		switch (position){
			case 0:
				title = "Overview";
				break;
			case 1:
				title = "Images";
				break;
			case 2:
				title = "Reviews";
				break;
		}
		return title;
	}
}
