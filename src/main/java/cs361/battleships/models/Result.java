package cs361.battleships.models;

public class Result {

	// stores the status of the last attack
	private AttackStatus attackStatus;

	//store ship
	private Ship sShip;

	//store attack location
	private Square loc;

	public AttackStatus getResult() {
		return attackStatus;
	}

	public void setResult(AttackStatus result) {
		attackStatus = result;
	}

	public Ship getShip() {
		return sShip;
	}

	public void setShip(Ship ship) {
		sShip = ship;
	}

	public Square getLocation() {
		return loc;
	}

	public void setLocation(Square square) {
		loc = square;
	}

}
