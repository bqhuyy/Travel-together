package cs300.apcs04.traveltogether;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Plan {

	private String mPlanID;
	private String mTitle;
	private Date mDateAdded;
	private String mNote;
	private ArrayList<String> mPlaceList;
	private ArrayList<String> mMemberList;

	public Plan() {

	}

	public Plan(String title) {
		mTitle = title;
		mDateAdded = new Date();
		mPlanID = UUID.randomUUID().toString();
		mNote = null;
		mPlaceList = null;
		mMemberList = null;
	}

	public ArrayList<String> getmMemberList() {
		return mMemberList;
	}

	public void setmMemberList(ArrayList<String> mMemberList) {
		if(mMemberList != null){
			this.mMemberList = new ArrayList<>();
			for(String id : mMemberList){
				this.mMemberList.add(id);
			}
		}
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

	public ArrayList<String> getmPlaceList() {
		return mPlaceList;
	}

	public void setmPlaceList(ArrayList<String> mPlaceList) {
		if(mPlaceList != null){
			this.mPlaceList = new ArrayList<>();
			for(String place : mPlaceList){
				this.mPlaceList.add(place);
			}
		}
	}
}
