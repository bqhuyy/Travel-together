package cs300.apcs04.traveltogether;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceJSONParser {

	public PlaceJSONParser(){

	}

	public Place parse(String sb){

		Place place = null;

		try {
			String address = "None";
			String phone = "None";
			Boolean open_now = null;
			String open_now_txt = "";
			float rating = 0;
			ArrayList<String> week_day_time = null;
			ArrayList<String> type = null;
			String website = "None";

			JSONObject jsonObject = new JSONObject(sb);

			JSONObject result = jsonObject.getJSONObject("result");
			String placeID = result.getString("place_id");
			String name = result.getString("name");

			if (result.has("formatted_address")) {

				address = result.getString("formatted_address");
			}
			if (result.has("formatted_phone_number")) {

				phone = result.getString("formatted_phone_number");
			}
			if (result.has("opening_hours")) {

				JSONObject opening_hours = result.getJSONObject("opening_hours");

				if (opening_hours.has("open_now")) {
					open_now = opening_hours.getBoolean("open_now");
				}

				if (opening_hours.has("weekday_text")) {
					week_day_time = new ArrayList<>();
					JSONArray weekDay = opening_hours.getJSONArray("weekday_text");
					for (int i = 0; i < weekDay.length(); i++) {
						week_day_time.add(String.valueOf(weekDay.get(i)));
					}
				}
			}
			if (result.has("rating")) {
				rating = (float) result.getDouble("rating");
			}
			if (result.has("types")) {
				type = new ArrayList<>();
				JSONArray types = result.getJSONArray("types");
				for (int i = 0; i < types.length(); i++) {
					type.add(String.valueOf(types.get(i)));
				}
			}
			if (result.has("website")) {
				website = result.getString("website");
			}

			String week_time_txt = "";

			if(week_day_time != null){
				StringBuilder s = new StringBuilder();
				s.append("\n");
				for(int i = 0; i < week_day_time.size(); i++){
					s.append(week_day_time.get(i));
					s.append("\n");
				}
				week_time_txt = s.toString();
			}
			else{
				week_time_txt = "None";
			}

			List<PlaceShortData> PlaceExpandData = new ArrayList<>();
			PlaceShortData ExpandAddress = new PlaceShortData(address);
			PlaceShortData ExpandWeekTime = new PlaceShortData(week_time_txt);
			PlaceExpandData.add(ExpandAddress);
			PlaceExpandData.add(ExpandWeekTime);

			place = new Place(placeID, name, rating, address, phone, website, open_now, week_day_time, type, PlaceExpandData);

		} catch (JSONException je) {
			Log.d("ssss", "null goyy");
		}
		return place;
	}
}
