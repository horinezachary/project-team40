package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Square[][] boardarray;
    private boolean[][] occupied;

    private boolean sonarEnabled;
    private int sonarCount;

    final int BOARDSIZE_X = 10;
    final int BOARDSIZE_Y = 10;

	// List of ships currently on this board
	private List<Ship> ships;
	// List of attacks made on this board
	private List<Result> attacks;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		boardarray = new Square[BOARDSIZE_X][BOARDSIZE_Y];
		occupied = new boolean[BOARDSIZE_X][BOARDSIZE_Y];

		for (int i = 0; i < boardarray.length; i++) {
            for (int j = 0; j < boardarray[0].length; j++) {
                //sets row values as 1 thru 10 and column values as 'A' thru 'J'
                boardarray[i][j] = new Square(i+1,(char) (65 + j));
                occupied[i][j] = false;
            }
        }
        ships = new ArrayList<>();
    	attacks = new ArrayList<>();

    	// set default sonar stats
		sonarEnabled = false;
		sonarCount = 2;
	}

	/**
	 * Returns whether the coordinates given lie on the board
	 *
	 * @param x	X coordinate
	 * @param y	Y Coordinate
	 * @return	Whether the given 2d coord lies on the board
	 */
	private boolean areCoordinatesOnBoard(int x, int y) {
		return x >= 1 && x <= 10 && y >= 1 && y <= 10;
	}

	/**
	 * Determines whether this ship is a duplicate of an existing one
	 *
	 * @param ship Ship to check for duplication
	 * @return whether or not the ship is a duplicate
	 */
	private boolean is_duplicate_ship(Ship ship) {
		for(Ship s: ships) {
			if(s.getShipType().equals(ship.getShipType())) {
				// same type
				return true;
			}
		}
		return false;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {

		// check for duplicate ships first
		if(is_duplicate_ship(ship)) {
			// can't place the same ship twice
			return false;
		}

		int yint = ((int)y)-65;
		int xint = x-1;
		setOccupied();
		//printBoard();
	    if (x > boardarray.length || yint > boardarray[0].length){return false;}
        List<Square> shipsquares = new ArrayList<Square>();
		for (int i = 0; i < ship.getLength(); i++){
		    if (isVertical){    //check if the values exist within the array bounds, and make sure that the square isn't occupied
		        if ((xint+i < BOARDSIZE_X) && (yint) < BOARDSIZE_Y && !occupied[xint+i][yint]) {
                    //System.out.println(xint+i+","+ (yint));
			        //add the square to the ship square list and set the square as occupied

					if (i == (ship.getLength() - 2)) {
						// make this square a captain's quarters
						boardarray[xint+i][yint].setCaptainsQuarters(true);

					}

					// add square to ship
					shipsquares.add(boardarray[xint+i][yint]);
				}
                else return false;
            }
            else{   //check if the values exist within the array bounds, and make sure that the square isn't occupied
                if ((xint < BOARDSIZE_X) &&((yint+i) < BOARDSIZE_Y) && (!occupied[xint][yint+i])) {
					//add the square to the ship square list and set the square as occupied
					if (i == (ship.getLength() - 2)) {
						// make this square a captain's quarters
						boardarray[xint][yint+i].setCaptainsQuarters(true);

					}

					// add square to ship
					shipsquares.add(boardarray[xint][yint+i]);
                }
                else return false;
            }
        }
        //add the squares to the ship and the ship to the board
        ship.setOccupiedSquares(shipsquares);
		this.addShip(ship);

        //printBoard();
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result r = new Result();
		r.setLocation(new Square(x,y));

		for(Result check: attacks) { //begin iterating through attack list
			if(
					check.getLocation() != null &&
					check.getLocation().getRow() == x &&
					check.getLocation().getColumn() == y &&
					check.getResult() != AtackStatus.SONAR_EMPTY &&
					check.getResult() != AtackStatus.SONAR_OCCUPIED &&
					!check.getLocation().getCaptain()
			) {
				// already attacked this space, return invalid
				r.setResult(AtackStatus.INVALID);
				return r;

			} else if(
					check.getLocation() != null &&
					check.getLocation().getCaptain() &&
					check.getLocation().getRow() == x &&
					check.getLocation().getColumn() == y &&
					check.getResult() == AtackStatus.MISS
			) {
				// FOR SPRINT #3
				// attacking a previously 'captained' space that was missed
				// we're going to update the result so it registers as a SINK properly
				// If this change is NOT made the previously 'missed' captain's quarters space
				// does not register a sink or hit, it remains missed while the rest is updated visually.
				check.setResult(AtackStatus.SUNK);

			}
		}


		if(x >= 1 && x <= 10 && y >= 'A' && y <= 'J') {
			// loop over each ship to check for a hit
			for(Ship ship: ships) {
				// get this ship's squares
				boolean didHit = false;
				List<Square> squares = ship.getOccupiedSquares();
				for(Square shipSquare: squares) {
					if(shipSquare.getRow() == x && shipSquare.getColumn() == y) {
						// Hit! mark this ship down health
						if(shipSquare.getCaptain() && ship.getArmor()) {
							// hit captain's quarters but counts as a miss, was still armored
							ship.setArmor(false);

							// readjust 'Result' location to show it was a captain's quarter
							Square lloc = r.getLocation();
							lloc.setCaptainsQuarters(true);
							r.setLocation(lloc);

						} else if(shipSquare.getCaptain() && !ship.getArmor()) {
							// hit unarmored captain's quarters, sink the ship
							ship.forceSink();
							didHit = true;
							r.setShip(ship);

						} else {
							// normal hit
							ship.decrementHealth();
							didHit = true;
							r.setShip(ship);

						}

						break;

					}
				}

				if(didHit && ship.getHealth() == 0) {
					// hit and SUNK, remove this ship
					ships.remove(ship);

					// mark each part of this ship as SUNK
					// CSS of 'sink' class will take precedence over hit
					// turning all affected pieces red
					Result rr = new Result();
					for(Square sq: ship.getOccupiedSquares()) {
						rr = new Result();
						rr.setLocation(sq);
						rr.setShip(ship);
						rr.setResult(AtackStatus.SUNK);
						setSonarEnabled(true);
						attacks.add(rr);
					}

					// check to see if the game is over
					if(ships.size() > 0) {
						// at least one more ship, return our last sunken result
						return rr;

					} else {
						// this was the last ship, game over!
						r.setResult(AtackStatus.SURRENDER);
						attacks.add(r);
						return r;

					}

				} else if(didHit) {
					// just a hit, not sunk
					r.setResult(AtackStatus.HIT);
					attacks.add(r);
					return r;

				}
			}

			// valid coordinates, determine if there is a ship here
			r.setResult(AtackStatus.MISS);

		} else {
			// invalid coordinates
			r.setResult(AtackStatus.INVALID);
			return r;
		}

		attacks.add(r);
		return r;
	}

	private Result getSonarHitSquare(int x, int y) {
		Result r = new Result();
		r.setResult(occupied[x-1][y-1] ? AtackStatus.SONAR_OCCUPIED : AtackStatus.SONAR_EMPTY);
		r.setLocation(new Square(x, (char) (y + 64)));
		return r;
	}

	public boolean sonar(int x, int y) {

		if(!areCoordinatesOnBoard(x,y)) {
			// out of bounds sonar attempt
			System.out.println("* sonar out of bounds "+x+", "+y);
			return false;

		}

		setOccupied();

		List<Result> results = new ArrayList<>();

		// 4 outlier square coords are
		if(areCoordinatesOnBoard(x-2, y)) {
			results.add(getSonarHitSquare(x-2,y));
		}

		if(areCoordinatesOnBoard(x+2, y)) {
			results.add(getSonarHitSquare(x+2,y));
		}

		if(areCoordinatesOnBoard(x, y-2)) {
			results.add(getSonarHitSquare(x,y-2));
		}

		if(areCoordinatesOnBoard(x, y+2)) {
			results.add(getSonarHitSquare(x,y+2));
		}

		int xx = x - 1;
		int yy = y - 1;

		for(int z = 0; z < 3; z++) {
			for(int q = 0; q < 3; q++) {
				if(areCoordinatesOnBoard(xx+q, yy+z)) {
					results.add(getSonarHitSquare(xx+q,yy+z));

				}
			}
		}

		// add all these results to display
		attacks.addAll(results);

		// sonar succeeded, decrement sonar count
		setSonarCount(getSonarCount()-1);
		return true;
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}
	public void addShip(Ship ship) {
	    ships.add(ship);
    }

	public List<Result> getAttacks() {
		return attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}

    private void setOccupied() {
		for (int i = 0; i < ships.size(); i++){
			List <Square> shipSquares = ships.get(i).getOccupiedSquares();
			for (int j = 0; j < shipSquares.size(); j++){
				int x = shipSquares.get(j).getRow()-1;
				int y = shipSquares.get(j).getColumn()-65;
				occupied[x][y] = true;
			}
		}
    }

    public void setSonarEnabled(boolean enabled) {
		sonarEnabled = enabled;
	}

	public boolean getSonarEnabled() {
		return sonarEnabled;
	}

	public void setSonarCount(int count) {
		sonarCount = count;
	}

	public int getSonarCount() {
		return sonarCount;
	}
}
