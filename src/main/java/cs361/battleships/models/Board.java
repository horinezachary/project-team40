package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Square[][] boardarray;
    private List<Ship> ships;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		boardarray = new Square[8][8];
		for (int i = 0; i < boardarray.length; i++) {
            for (int j = 0; j < boardarray[0].length; j++) {
                boardarray[i][j].setRow(i + 1); //sets row values as 1 thru 8
                boardarray[i][j].setColumn((char) (65 + j));   //sets column values as 'A' thru 'H'
            }
        }
        ships = new ArrayList<Ship>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		// TODO Implement
		return false;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		return null;
	}

	public List<Ship> getShips() {
		//TODO implement
		return null;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}
}
