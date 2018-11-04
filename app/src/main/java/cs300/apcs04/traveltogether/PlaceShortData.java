package cs300.apcs04.traveltogether;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceShortData implements Parcelable {

	private String mContent;

	public PlaceShortData(String content){
		this.mContent = content;
	}

	protected  PlaceShortData(Parcel in){
		this.mContent = in.readString();
	}

	public String getmContent(){
		return this.mContent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PlaceShortData)) return false;

		PlaceShortData place = (PlaceShortData) o;

		if(!getmContent().equals(place.getmContent())) return false;

		return true;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mContent);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PlaceShortData> CREATOR = new Creator<PlaceShortData>() {
		@Override
		public PlaceShortData createFromParcel(Parcel in) {
			return new PlaceShortData(in);
		}

		@Override
		public PlaceShortData[] newArray(int size) {
			return new PlaceShortData[size];
		}
	};
}
