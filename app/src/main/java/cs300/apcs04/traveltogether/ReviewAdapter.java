package cs300.apcs04.traveltogether;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

	private ArrayList<ReviewData> arrayList;
	private Context context;

	public ReviewAdapter(ArrayList<ReviewData> arrayList, Context context) {
		this.arrayList = arrayList;
		this.context = context;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.person_review_item, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		ReviewData data = arrayList.get(position);
		holder.username.setText(data.getUsername());
		holder.ratingBar.setRating(data.getRating());
		holder.avt.setScaleType(ImageView.ScaleType.FIT_XY);
		Picasso.get().load(data.getAvt()).into(holder.avt);
		holder.review.setText(data.getReview());
		holder.date.setText(data.getDate());
	}

	@Override
	public int getItemCount() {
		return arrayList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder{
		private TextView username;
		private ImageView avt;
		private RatingBar ratingBar;
		private TextView date;
		private TextView review;

		public ViewHolder(View itemView) {
			super(itemView);
			username = (TextView) itemView.findViewById(R.id.username);
			avt = (ImageView) itemView.findViewById(R.id.user_avt);
			ratingBar = (RatingBar) itemView.findViewById(R.id.user_rating);
			date = (TextView) itemView.findViewById(R.id.date_review);
			review = (TextView) itemView.findViewById(R.id.user_review);
		}
	}
}
