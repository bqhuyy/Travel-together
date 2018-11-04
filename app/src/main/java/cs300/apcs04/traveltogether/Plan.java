package cs300.apcs04.traveltogether;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Plan {

	private String mPlanID;
	private String mTitle;
	private long mDateAdded;
	private String mNote;
	private HashMap<String, String> mPlaceList;
	private HashMap<String, String> mMemberList;

	public Plan() {

	}

	public Plan(String title) {
		mTitle = title;
		mDateAdded = new Date().getTime();
		mPlanID = UUID.randomUUID().toString();
		mNote = null;
		mPlaceList = null;
		mMemberList = null;
	}

	public HashMap<String, String> getmMemberList() {
		return mMemberList;
	}

	public void setmMemberList(HashMap<String, String> mMemberList) {
		if(mMemberList != null){
			this.mMemberList = new HashMap<>();
			for(String id : mMemberList.keySet()){
				this.mMemberList.put(id, mMemberList.get(id));
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

	public long getDateAdded() {
		return mDateAdded;
	}

	public void setDateAdded(long dateAdded) {
		mDateAdded = dateAdded;
	}

	public String getNote() {
		return mNote;
	}

	public void setNote(String note) {
		this.mNote = note;
	}

	public HashMap<String, String> getmPlaceList() {
		return mPlaceList;
	}

	public void setmPlaceList(HashMap<String, String> mPlaceList) {
		if(mPlaceList != null){
			this.mPlaceList = new HashMap<>();
			for(String place : mPlaceList.keySet()){
				this.mPlaceList.put(place, mPlaceList.get(place));
			}
		}
	}
}
