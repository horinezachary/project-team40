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

	/**
	 * Returns whether a space was already attacked for this result
	 *
	 * @param x		X to compare for row
	 * @param y		Y to compare for column
	 * @return	Whether the space was already attacked
	 */
	public boolean isResultSpaceAlreadyAttacked(int x, char y) {
		return getLocation() != null &&
				getLocation().getRow() == x &&
				getLocation().getColumn() == y &&
				getResult() != AttackStatus.SONAR_EMPTY &&
				getResult() != AttackStatus.SONAR_OCCUPIED &&
				!getLocation().getCaptain();
	}

	/**
	 * Returns whether a space was a formerly armored captain's square, and is a valid attack again
	 *
	 * @param x		X to compare for row
	 * @param y		Y to compare for col
	 * @return	Whether the space was a formerly armored captain, and is a valid space to attack again
	 */
	public boolean wasSpaceFormerlyArmoredCaptain(int x, char y) {
		return getLocation() != null &&
				getLocation().getCaptain() &&
				getLocation().getRow() == x &&
				getLocation().getColumn() == y &&
				getResult() == AttackStatus.MISS;
	}

}
