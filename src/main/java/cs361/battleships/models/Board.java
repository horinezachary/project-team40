package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {

	// List of ships currently on this board
	private List<Ship> ships;
	// List of attacks made on this board
	private List<Result> attacks;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// setup the ships & attacks arrays
		ships = new ArrayList<>();
		attacks = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		// TODO Implement (added conditions so other tests may progress)
		return (x >= 0 && x <= 9 && y >= 'A' && y <= 'J');
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement fully, just stubbed so other tests work right now
		Result r = new Result();
		if(x >= 0 && x <= 9 && y >= 'A' && y <= 'J') {
			r.setResult(AtackStatus.MISS);
		} else {
			r.setResult(AtackStatus.INVALID);
		}
		return r;
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public List<Result> getAttacks() {
		return attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}
}
