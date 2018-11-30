package cs361.battleships.models;

import java.util.*;

public class Board {
    private Square[][] boardarray;
    private boolean[][] occupied;
	private Weapon w;
    private boolean sonarEnabled;
    private int sonarCount;
    private boolean fleetMoveEnabled;
    private int fleetMovecount;

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
		w = new Bomb();
		for (int i = 0; i < boardarray.length; i++) {
            for (int j = 0; j < boardarray[0].length; j++) {
                //sets row values as 1 thru BOARDSIZE_X and column values as 'A' thru 'J'
                boardarray[i][j] = new Square(i+1,(char) (65 + j));
                occupied[i][j] = false;
            }
        }
        ships = new ArrayList<>();
    	attacks = new ArrayList<>();

    	// set default sonar stats
		sonarEnabled = false;
		sonarCount = 2;

		// fleet move disabled by default
		fleetMovecount = 2;
		fleetMoveEnabled = false;

	}

	/**
	 * Returns whether the coordinates given lie on the board
	 *
	 * @param x	X coordinate
	 * @param y	Y Coordinate
	 * @return	Whether the given 2d coord lies on the board
	 */
	public boolean areCoordinatesOnBoard(int x, int y) {
		return x >= 1 && x <= BOARDSIZE_X && y >= 1 && y <= BOARDSIZE_Y;
	}

	public boolean isOccupied(int x, int y) {
		return occupied[x][y];
	}

	public Square getSquare(int x, int y) {
		return boardarray[x][y];
	}


	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		setOccupied();

		// check for duplicate ships first
		if(Ship.is_duplicate_ship(ship, getShips())) {
			// can't place the same ship twice
			return false;
		}

		int yint = ((int)y)-65;
		int xint = x-1;

		if(!areCoordinatesOnBoard(x, yint+1)) {
			return false;
		}

		List<Square> shipsquares = new ArrayList<>();
		for (int i = 0; i < ship.getLength(); i++){
			if (isVertical){    //check if the values exist within the array bounds, and make sure that the square isn't occupied
				if ((xint+i < BOARDSIZE_X) && yint < BOARDSIZE_Y && (!isOccupied(xint+i,yint) || ship.getSubmerged())) {
					//add the square to the ship square list and set the square as occupied

					ship.checkToMakeCaptainsQuarters(getSquare(xint+i,yint), i);

					// add square to ship
					shipsquares.add(getSquare(xint+i,yint));
				}
				else return false;
			} else {   //check if the values exist within the array bounds, and make sure that the square isn't occupied
				if ((xint < BOARDSIZE_X) &&((yint+i) < BOARDSIZE_Y) && (!isOccupied(xint, yint+i) || ship.getSubmerged())) {
					//add the square to the ship square list and set the square as occupied

					ship.checkToMakeCaptainsQuarters(getSquare(xint,yint+i), i);

					// add square to ship
					shipsquares.add(getSquare(xint,yint+i));
				}
				else return false;
			}
		}

		// submarine condition
		if(ship.getShipType().equals("SUBMARINE")) {
			// add periscope
			if(isVertical) {
				if(yint+1 < BOARDSIZE_Y && (!isOccupied(xint+2,yint+1) || ship.getSubmerged())) {
					// add periscope square to the ship square list
					shipsquares.add(getSquare(xint+2,yint+1));

				} else {
					return false;
				}

			} else {
				if(xint-1 >= 0 && ((yint+2) < BOARDSIZE_Y) && (!isOccupied(xint-1, yint+2) || ship.getSubmerged())) {
					// add periscope square to the ship square list
					shipsquares.add(getSquare(xint-1,yint+2));
				} else {
					return false;
				}
			}
		}

		//add the squares to the ship and the ship to the board
		ship.setOccupiedSquares(shipsquares);
		addShip(ship);

		return true;
	}


	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result r = new Result();
		r.setLocation(new Square(x,y));

		for(Result check: getAttacks()) { //begin iterating through attack list
			if(check.isResultSpaceAlreadyAttacked(x, y)) {
				// already attacked this space, return invalid
				r.setResult(AttackStatus.INVALID);
				return r;

			} else if(check.wasSpaceFormerlyArmoredCaptain(x, y)) {
				// FOR SPRINT #3
				// attacking a previously 'captained' space that was missed
				// we're going to update the result so it registers as a SINK properly
				// If this change is NOT made the previously 'missed' captain's quarters space
				// does not register a sink or hit, it remains missed while the rest is updated visually.
				check.setResult(AttackStatus.SUNK);

			}
		}
		r = w.Fire(this, x, y, r);

		return r;
	}


	/**
	 * Makes a sonar pulse on the board
	 *
	 * @param x	X coordinate to make the pulse at
	 * @param y	Y coordinate to make the pulse at
	 * @return	Whether or not the pulse was successful
	 */
	public boolean sonar(int x, int y) {
		setOccupied();
		if(!areCoordinatesOnBoard(x,y)) {
			// out of bounds sonar attempt
			return false;

		}

		List<Result> results = new ArrayList<>();

		// conditionally add 4 outlier sonar coordinates
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

		// setup and add the center 9 coordinates conditionally
		for(int z = 0; z < 3; z++) {
			for(int q = 0; q < 3; q++) {
				if(areCoordinatesOnBoard(xx+q, yy+z)) {
					results.add(getSonarHitSquare(xx+q,yy+z));

				}
			}
		}

		// add all these results from the sonar pulse to display
		addAttacks(results);

		// decrement sonar count & return success
		setSonarCount(getSonarCount()-1);
		return true;
	}

	/**
	 * Returns the sonar pulse result (occupied or empty) for a given space on the board
	 *
	 * @param x     X coordinate of square to scan
	 * @param y	    Y coordinate of square to scan
	 * @return	Result object containing status of sonar pulse on this square
	 */
	public Result getSonarHitSquare(int x, int y) {
		Result r = new Result();
		r.setResult(isOccupied(x-1,y-1) ? AttackStatus.SONAR_OCCUPIED : AttackStatus.SONAR_EMPTY);
		r.setLocation(new Square(x, (char) (y + 64)));
		return r;
	}

	/**
	 * Moves ships on board. Checks to see if any of the ships would have a square off the board
	 *
	 * @param x value to increment x by
	 * @param y value to increment y by
	 *
	 * @return true if successful, false if not possible
	 */
	public boolean moveShips(int x, int y){
		List<Ship> movedShips = new ArrayList<>();
		for (int i = 0; i < ships.size(); i++){
			Ship temp = ships.get(i);
			List<Square> squares = temp.getOccupiedSquares();
			boolean hasCollision = false;
			for (int j = 0; j < squares.size(); j++){
				//System.out.println((squares.get(j).getColumn()-65+x) + ", " + (squares.get(j).getRow()+y));
				if (areCoordinatesOnBoard(squares.get(j).getColumn()-65+x, squares.get(j).getRow()+y)){
					continue;
				}
				else{
					hasCollision = true;
				}
			}
			if (hasCollision){
				for (int j = 0; j < squares.size(); j++) {
					int checkX = (int)(squares.get(j).getColumn())-64-x;
					int checkY = squares.get(j).getRow()-y;
					setOccupied();
					if (isOccupied(checkX,checkY) && !temp.occupiesSpace(checkX,(char)(checkY+65))){
						return false;
					}
				}
			}
			else{
				movedShips.add(temp);
			}
		}
		if (movedShips.size() == 0){
			return false;
		}
		for (int i = 0; i < movedShips.size(); i++) {
			movedShips.get(i).move(x, y);
		}

		setFleetMovecount(getFleetMovecount()-1);

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

    public void removeShip(Ship ship) {
		ships.remove(ship);
	}

	public List<Result> getAttacks() {
		return attacks;
	}

	public void addAttack(Result attack) {
		this.attacks.add(attack);
	}

	public void addAttacks(List<Result> attacks) {
		this.attacks.addAll(attacks);
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}


	/**
	 * Sets the 'occupied' array for each square taken on the board.
	 * Used by the sonar pulse logic to determine whether a scan returns EMPTY or OCCUPIED
	 * for a given square.
	 */
    public void setOccupied() {
    	occupied = new boolean[BOARDSIZE_X][BOARDSIZE_Y];
		for (int i = 0; i < ships.size(); i++){
			List <Square> shipSquares = ships.get(i).getOccupiedSquares();
			for (int j = 0; j < shipSquares.size(); j++){
				occupied[shipSquares.get(j).getRow()-1][shipSquares.get(j).getColumn()-65] = true;
			}
		}
    }

    public void setSonarEnabled(boolean enabled) {
		sonarEnabled = enabled;
	}

	public boolean getSonarEnabled() {
		return sonarEnabled;
	}

	public void setFleetMoveEnabled(boolean enabled) {
    	fleetMoveEnabled = enabled;
	}

	public boolean getFleetMoveEnabled() {
    	return fleetMoveEnabled;
	}

	public void setFleetMovecount(int count) {
    	fleetMovecount = count;
	}

	public int getFleetMovecount() {
    	return fleetMovecount;
	}

	public void setSonarCount(int count) {
		sonarCount = count;
	}

	public int getSonarCount() {
		return sonarCount;
	}

	public void setWeapon(int type){
    	switch(type){
			/*case 0:
				w = new Bomb();*/
			case 1:
				w = new SpaceLaser();
				break;
			default:
				w = new Bomb();
		}
	}

	public String getWeapon(Board board){
    	return board.w.getWeaponName();
	}

	/*public boolean getLaserStatus(){
    	return laserEnabled;
	}*/
}
