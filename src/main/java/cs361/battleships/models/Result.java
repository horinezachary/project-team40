package cs361.battleships.models;

public class Result {

	// stores the status of the last attack
	private AtackStatus attackStatus;

	public AtackStatus getResult() {
		return attackStatus;
	}

	public void setResult(AtackStatus result) {
		attackStatus = result;
	}

	public Ship getShip() {
		//TODO implement
		return null;
	}

	public void setShip(Ship ship) {
		//TODO implement
	}

	public Square getLocation() {
		//TODO implement
		return null;
	}

	public void setLocation(Square square) {
		//TODO implement
	}
}
