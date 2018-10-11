package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Square[][] boardarray;
    private List<Ship> ships;

    final int BOARDSIZE_X = 10;
    final int BOARDSIZE_Y = 10;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		boardarray = new Square[BOARDSIZE_X][BOARDSIZE_Y];
		for (int i = 0; i < boardarray.length; i++) {
            for (int j = 0; j < boardarray[0].length; j++) {
                //sets row values as 1 thru 10 and column values as 'A' thru 'J'
                boardarray[i][j] = new Square(i+1,(char) (65 + j));
            }
        }
        ships = new ArrayList<Ship>();
		printBoard();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
	    if (x > boardarray.length){return false;}
        if ((int)(y+65) > boardarray[0].length){return false;}
        List<Square> shipsquares = new ArrayList<Square>();
		for (int i = 0; i < ship.getLength(); i++){
		    if (isVertical){
		        if (this.isSquareEmpty(x, y + i)) {
                    shipsquares.add(boardarray[x][y+i]);
                }
                else return false;
            }
            else{
                if (this.isSquareEmpty(x+i, y)) {
                    shipsquares.add(boardarray[x+i][y]);
                }
                else return false;
            }
        }
        ship.setOccupiedSquares(shipsquares);
		this.addShip(ship);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		return null;
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
	}
	public void addShip(Ship ship) {
	    ships.add(ship);
    }

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}

	public boolean isSquareEmpty (int x, int y){
        for (int i = 0; i < ships.size(); i++){
            for (int j = 0; j < ships.get(i).getOccupiedSquares().size(); j++) {
                if(ships.get(i).getOccupiedSquares().get(j).getColumn() == (char)(y+65)){return false;}
                if(ships.get(i).getOccupiedSquares().get(j).getRow() == (char)(x+1)){return false;}
            }
        }
	    return true;
    }
}
