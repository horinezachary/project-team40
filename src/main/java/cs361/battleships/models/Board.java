package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Square[][] boardarray;

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
		for (int i = 0; i < boardarray.length; i++) {
            for (int j = 0; j < boardarray[0].length; j++) {
                //sets row values as 1 thru 10 and column values as 'A' thru 'J'
                boardarray[i][j] = new Square(i+1,(char) (65 + j));
            }
        }
        ships = new ArrayList<Ship>();
    	attacks = new ArrayList<>();
		//printBoard();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
	    if (x > boardarray.length || ((int)y)-65 > boardarray[0].length){return false;}
        List<Square> shipsquares = new ArrayList<Square>();
		for (int i = 0; i < ship.getLength(); i++){
		    if (isVertical){    //check if the values exist within the array bounds, and make sure that the square isn't occupied
		        if ((x-1+i < BOARDSIZE_X) && (((int)y)-65) < BOARDSIZE_Y && !boardarray[x-1+i][((int)y)-65].getOccupied()) {
                    //System.out.println(x-1+","+ (((int)y)-65+i));
			        //add the square to the ship square list and set the square as occupied
                    shipsquares.add(boardarray[x-1+i][((int)y)-65]);
			        boardarray[x-1+i][((int)y)-65].setOccupied(true);
                }
                else return false;
            }
            else{   //check if the values exist within the array bounds, and make sure that the square isn't occupied
                if ((x-1+i < BOARDSIZE_X) && (((int)y)-65+i) < BOARDSIZE_Y && !boardarray[x-1][((int)y)-65+i].getOccupied()) {
                    //System.out.println(x-1+","+ (((int)y)-65+i));
	                //add the square to the ship square list and set the square as occupied
                    shipsquares.add(boardarray[x-1][((int)y)-65+i]);
                    boardarray[x-1][((int)y)-65+i].setOccupied(true);
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

		if(x >= 1 && x <= 10 && y >= 'A' && y <= 'J') {
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
	public void addShip(Ship ship) {
	    ships.add(ship);
    }

	public List<Result> getAttacks() {
		return attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}

    private void printBoard (){ //method for printing out the board on the backend
	    for (int i = 0; i < boardarray.length;i++){
	        System.out.print("|");
	        for (int j = 0; j < boardarray[0].length; j++){
                if(!boardarray[i][j].getOccupied()){System.out.print("O|");}
                else{System.out.print("X|");}
            }
            System.out.print("\n");
        }
    }
}
