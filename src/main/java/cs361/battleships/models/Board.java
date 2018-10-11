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
		Result r = new Result();
		r.setLocation(new Square(x,y));

		if(x >= 0 && x <= 9 && y >= 'A' && y <= 'J') {
			// loop over each ship to check for a hit
			for(Ship ship: ships) {
				// get this ship's squares
				boolean didHit = false;
				List<Square> squares = ship.getOccupiedSquares();
				for(Square shipSquare: squares) {
					if(shipSquare.getRow() == x && shipSquare.getColumn() == y) {
						// Hit! remove this square, set back, & break
						squares.remove(shipSquare);
						ship.setOccupiedSquares(squares);

						// set the ship hit in this result
						r.setShip(ship);

						didHit = true;
						break;

					}
				}

				if(didHit && squares.size() == 0) {
					// hit and SUNK, remove this ship
					ships.remove(ship);

					// check to see if the game is over
					if(ships.size() > 0) {
						// at least one more ship
						r.setResult(AtackStatus.SUNK);

					} else {
						// this was the last ship, game over!
						r.setResult(AtackStatus.SURRENDER);

					}

					return r;

				} else if(didHit) {
					// just a hit, not sunk
					r.setResult(AtackStatus.HIT);
					return r;

				}
			}

			// valid coordinates, determine if there is a ship here
			r.setResult(AtackStatus.MISS);

		} else {
			// invalid coordinates
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
