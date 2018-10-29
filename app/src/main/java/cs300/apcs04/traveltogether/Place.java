package cs300.apcs04.traveltogether;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class Place extends ExpandableGroup<PlaceShortData> {

	private String mPlaceId;
	private String mName;
	private float mRating;
	private String mAddress;
	private String mPhone;
	private String mWebsiteURL;
	private boolean mIsOpen;
	private ArrayList<String> mWeek_time;
	private ArrayList<String> mType;

	public Place(String placeId, String name, float rating, String address, String phone, String websiteURL
			, boolean isOpen, ArrayList<String> week_time, ArrayList<String> type, List<PlaceShortData> items) {
		super(name, items);
		this.mPlaceId = placeId;
		this.mName = name;
		this.mRating = rating;
		this.mAddress = address;
		this.mPhone = phone;
		this.mWebsiteURL = websiteURL;
		this.mIsOpen = isOpen;
		if(week_time != null){
			this.mWeek_time = new ArrayList<>();
			for(int i = 0; i < week_time.size(); i++){
				this.mWeek_time.add(week_time.get(i));
			}
		}
		if(type != null){
			this.mType = new ArrayList<>();
			for(int i = 0; i < type.size(); i++){
				this.mType.add(type.get(i));
			}
		}
	}

	public String getmPlaceId() {
		return mPlaceId;
	}

	public void setmPlaceId(String mPlaceId) {
		this.mPlaceId = mPlaceId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public float getmRating() {
		return mRating;
	}

	public void setmRating(float mRating) {
		this.mRating = mRating;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public String getmWebsiteURL() {
		return mWebsiteURL;
	}

	public void setmWebsiteURL(String mWebsiteURL) {
		this.mWebsiteURL = mWebsiteURL;
	}

	public boolean ismIsOpen() {
		return mIsOpen;
	}

	public void setmIsOpen(boolean mIsOpen) {
		this.mIsOpen = mIsOpen;
	}

	public ArrayList<String> getmWeek_time() {
		return mWeek_time;
	}

	public void setmWeek_time(ArrayList<String> mWeek_time) {
		this.mWeek_time = mWeek_time;
	}

	public ArrayList<String> getmType() {
		return mType;
	}

	public void setmType(ArrayList<String> mType) {
		this.mType = mType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Place)) return false;

		Place place = (Place) o;

		return getmPlaceId() == place.getmPlaceId();

	}
}
