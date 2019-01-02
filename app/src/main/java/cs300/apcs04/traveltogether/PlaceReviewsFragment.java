package cs300.apcs04.traveltogether;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlaceReviewsFragment extends Fragment {
	private ArrayList<ReviewData> mArrayList;
	private RecyclerView mRecyclerView;
	private ReviewAdapter mAdapter;
	private ReviewData mReviewData;
	private TextView mRating;
	private RatingBar mRatingBar;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_place_reviews, container, false);

		mArrayList = (ArrayList<ReviewData>) getArguments().getSerializable("reviewdata");
		float rating = (float) getArguments().getFloat("average_rating");

		mRating = (TextView) v.findViewById(R.id.rating);
		mRatingBar = v.findViewById(R.id.rating_bar);

		mRecyclerView = (RecyclerView) v.findViewById(R.id.review_list);

		mRating.setText(String.valueOf(rating));
		mRatingBar.setRating(rating);

		mAdapter = new ReviewAdapter(mArrayList, getContext());
		mRecyclerView.setHasFixedSize(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
		mRecyclerView.setLayoutManager(linearLayoutManager);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
		mRecyclerView.addItemDecoration(dividerItemDecoration);
		mRecyclerView.setAdapter(mAdapter);
		return v;
	}
}
