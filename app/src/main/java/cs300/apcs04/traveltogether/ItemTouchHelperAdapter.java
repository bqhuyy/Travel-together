package cs300.apcs04.traveltogether;

public interface ItemTouchHelperAdapter {

	boolean onItemMove(int orgigin, int target);

	void onItemRemove(int pos);
}
