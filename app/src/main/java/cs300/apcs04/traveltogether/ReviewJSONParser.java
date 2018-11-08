package cs300.apcs04.traveltogether;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReviewJSONParser {

	private ArrayList<ReviewData> arrayList;
	private float rating;

	public ReviewJSONParser(){
		arrayList = new ArrayList<>();
		rating = -1;
	}

	public ArrayList<ReviewData> parse(String sb){

		ReviewData reviewData = null;

		try {
			JSONObject jsonObject = new JSONObject(sb);
			JSONObject result = jsonObject.getJSONObject("result");
			if(result.has("rating")){
				rating = (float) result.getDouble("rating");
			}
			if(result.has("reviews")) {
				JSONArray reviews = result.getJSONArray("reviews");
				for(int i = 0; i < reviews.length(); i++){
					JSONObject object = reviews.getJSONObject(i);
					String username = object.getString("author_name");
					String avt = object.getString("profile_photo_url");
					float rating = (float) object.getDouble("rating");
					long date = object.getLong("time");
					String review = object.getString("text");

					Date d = new Date(date*1000);
					SimpleDateFormat d2 = new SimpleDateFormat("dd/MM/yyyy");
					String date_review = d2.format(d);
					reviewData = new ReviewData(username, avt, review, date_review, rating);
					arrayList.add(reviewData);
					//adapter.notifyDataSetChanged();
				}
			}
		}catch(JSONException je){
			Log.d("ffff", "Dsahdjsah");
		}

		return arrayList;
	}

	public float getRating(){
		return this.rating;
	}
}
