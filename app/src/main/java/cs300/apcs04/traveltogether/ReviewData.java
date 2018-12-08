package cs300.apcs04.traveltogether;

public class ReviewData {
	private String username;
	private String avt;
	private String review;
	private String date;
	private float rating;

	public ReviewData(){

	}
	public ReviewData(String username, String avt, String review, String date, float rating) {
		this.username = username;
		this.avt = avt;
		this.review = review;
		this.date = date;
		this.rating = rating;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvt() {
		return avt;
	}

	public void setAvt(String avt) {
		this.avt = avt;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
}
