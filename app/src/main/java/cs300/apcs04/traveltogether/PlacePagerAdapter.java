package cs300.apcs04.traveltogether;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class PlacePagerAdapter extends FragmentPagerAdapter {

	private Place mPlacedata;
	private ArrayList<String> mArrayListFavID;
	private ArrayList<ReviewData> mArrayListReviews;
	private float mAverageRating;

	public PlacePagerAdapter(FragmentManager fm, Place placedata, ArrayList<String> arrayListFavID, ArrayList<ReviewData> reviewsData, float rating) {
		super(fm);
		this.mPlacedata = placedata;
		this.mArrayListFavID = arrayListFavID;
		this.mArrayListReviews = reviewsData;
		this.mAverageRating = rating;
		Log.d("kkkk", placedata.getmName());
	}

	@Override
	public Fragment getItem(int i) {
		Fragment frag = null;
		Bundle bundle = new Bundle();


		switch (i){
			case 0:
				frag = new PlaceInfoFragment();
				bundle.putSerializable("placedata", mPlacedata);
				bundle.putStringArrayList("fav_id", mArrayListFavID);
				frag.setArguments(bundle);
				break;
			case 1:
				frag = new PlaceImagesFragment();
				bundle.putString("placeid", mPlacedata.getmPlaceId());
				frag.setArguments(bundle);
				break;
			case 2:
				frag = new PlaceReviewsFragment();
				bundle.putSerializable("reviewdata", mArrayListReviews);
				bundle.putFloat("average_rating", mAverageRating);
				frag.setArguments(bundle);
				break;
		}
		return frag;
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
