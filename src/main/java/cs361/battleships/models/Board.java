package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Square[][] boardarray;
    private boolean[][] occupied;

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
        ships = new ArrayList<Ship>();
    	attacks = new ArrayList<>();
		//printBoard();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
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
                    shipsquares.add(boardarray[xint+i][yint]);
                }
                else return false;
            }
            else{   //check if the values exist within the array bounds, and make sure that the square isn't occupied
                if ((xint < BOARDSIZE_X) &&((yint+i) < BOARDSIZE_Y) && (!occupied[xint][yint+i])) {
                    //System.out.println(xint+","+ (yint+i));
	                //add the square to the ship square list and set the square as occupied
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
		setOccupied();
	    for (int i = 0; i < boardarray.length;i++){
	        System.out.print("|");
	        for (int j = 0; j < boardarray[0].length; j++){
                if(!occupied[i][j]){System.out.print("O|");}
                else{System.out.print("X|");}
            }
            System.out.print("\n");
        }
    }
    private void setOccupied(){
		for (int i = 0; i < ships.size(); i++){
			List <Square> shipSquares = ships.get(i).getOccupiedSquares();
			for (int j = 0; j < shipSquares.size(); j++){
				int x = shipSquares.get(j).getRow()-1;
				int y = shipSquares.get(j).getColumn()-65;
				occupied[x][y] = true;
			}
		}
    }
}
