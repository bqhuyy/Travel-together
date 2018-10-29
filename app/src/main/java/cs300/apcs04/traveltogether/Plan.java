package cs300.apcs04.traveltogether;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Plan {

	private String mPlanID;
	private String mTitle;
	private Date mDateAdded;
	private String mNote;
	private ArrayList<Place> mPlaceList;

	public Plan(String title, Date dateAdded) {
		mTitle = title;
		mDateAdded = dateAdded;
		mPlanID = UUID.randomUUID().toString();
	}

	public String getmPlanID() {
		return mPlanID;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Date getDateAdded() {
		return mDateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		mDateAdded = dateAdded;
	}

	public String getNote() {
		return mNote;
	}

	public void setNote(String note) {
		this.mNote = note;
	}

	public ArrayList<Place> getmPlaceList() {
		return mPlaceList;
	}

	public void setmPlaceList(ArrayList<Place> mPlaceList) {
		this.mPlaceList = mPlaceList;
	}
}
