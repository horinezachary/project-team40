package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the action of placing a ship on the board
 */
public class PlaceUtility {

    public static boolean place(Board board, Ship ship, int x, char y, boolean isVertical) {
        // check for duplicate ships first
        if(is_duplicate_ship(ship, board.getShips())) {
            // can't place the same ship twice
            return false;
        }

        int yint = ((int)y)-65;
        int xint = x-1;

        if(!board.areCoordinatesOnBoard(x, yint+1)) {
            return false;
        }

        List<Square> shipsquares = new ArrayList<>();
        for (int i = 0; i < ship.getLength(); i++){
            if (isVertical){    //check if the values exist within the array bounds, and make sure that the square isn't occupied
                if ((xint+i < board.BOARDSIZE_X) && (yint) < board.BOARDSIZE_Y && !board.isOccupied(xint+i,yint)) {
                    //add the square to the ship square list and set the square as occupied

                    checkToMakeCaptainsQuarters(board.getSquare(xint+i,yint), ship, i);

                    // add square to ship
                    shipsquares.add(board.getSquare(xint+i,yint));
                }
                else return false;
            } else {   //check if the values exist within the array bounds, and make sure that the square isn't occupied
                if ((xint < board.BOARDSIZE_X) &&((yint+i) < board.BOARDSIZE_Y) && (!board.isOccupied(xint, yint+i))) {
                    //add the square to the ship square list and set the square as occupied

                    checkToMakeCaptainsQuarters(board.getSquare(xint,yint+i), ship, i);

                    // add square to ship
                    shipsquares.add(board.getSquare(xint,yint+i));
                }
                else return false;
            }
        }
        //add the squares to the ship and the ship to the board
        ship.setOccupiedSquares(shipsquares);
        board.addShip(ship);

        return true;
    }


    /**
     * Checks to make a given square the captain's quarters
     *
     * @param square    Square to check to make a captain's quarters
     * @param s		    Ship to check length of
     * @param i		    Index along length of ship
     */
    private static void checkToMakeCaptainsQuarters(Square square, Ship s, int i) {
        if (i == (s.getLength() - 2)) {
            // make this square a captain's quarters
            square.setCaptainsQuarters(true);

        }
    }


    /**
     * Determines whether this ship is a duplicate of an existing one
     *
     * @param ship  Ship to check for duplication
     * @param ships List of ships to check against
     * @return whether or not the ship is a duplicate
     */
    private static boolean is_duplicate_ship(Ship ship, List<Ship> ships) {
        for(Ship s: ships) {
            if(s.getShipType().equals(ship.getShipType())) {
                // same type
                return true;
            }
        }
        return false;
    }
}
