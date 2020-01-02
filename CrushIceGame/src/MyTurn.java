
public class MyTurn {
	private int myTurn;

	public MyTurn(int num) {
		if (num % 2 == 0) {
			myTurn = 0;
		} else {
			myTurn = 1;
		}
	}

	public boolean isMyTurn() {
		if (myTurn == 1) {
			return true;
		}
		return false;
	}

	public int getMyTurn() {
		return myTurn;
	}

	public void setMyTurn() {
		myTurn = 1 - myTurn;
	}
}
