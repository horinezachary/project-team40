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
		return PlaceUtility.place(this, ship, x, y, isVertical);
	}


	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		return AttackUtility.attack(this, x, y);
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
		return SonarUtility.sonar(this, x, y);
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
    private void setOccupied() {
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

	public void setSonarCount(int count) {
		sonarCount = count;
	}

	public int getSonarCount() {
		return sonarCount;
	}
}
