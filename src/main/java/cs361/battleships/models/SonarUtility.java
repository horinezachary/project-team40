package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class SonarUtility {

    public static boolean sonar(Board board, int x, int y) {
        if(!board.areCoordinatesOnBoard(x,y)) {
            // out of bounds sonar attempt
            return false;

        }

        List<Result> results = new ArrayList<>();

        // conditionally add 4 outlier sonar coordinates
        if(board.areCoordinatesOnBoard(x-2, y)) {
            results.add(getSonarHitSquare(board,x-2,y));
        }

        if(board.areCoordinatesOnBoard(x+2, y)) {
            results.add(getSonarHitSquare(board,x+2,y));
        }

        if(board.areCoordinatesOnBoard(x, y-2)) {
            results.add(getSonarHitSquare(board,x,y-2));
        }

        if(board.areCoordinatesOnBoard(x, y+2)) {
            results.add(getSonarHitSquare(board,x,y+2));
        }

        int xx = x - 1;
        int yy = y - 1;

        // setup and add the center 9 coordinates conditionally
        for(int z = 0; z < 3; z++) {
            for(int q = 0; q < 3; q++) {
                if(board.areCoordinatesOnBoard(xx+q, yy+z)) {
                    results.add(getSonarHitSquare(board,xx+q,yy+z));

                }
            }
        }

        // add all these results from the sonar pulse to display
        board.addAttacks(results);

        // decrement sonar count & return success
        board.setSonarCount(board.getSonarCount()-1);
        return true;
    }


    /**
     * Returns the sonar pulse result (occupied or empty) for a given space on the board
     *
     * @param board Board to check for occupied status on
     * @param x     X coordinate of square to scan
     * @param y	    Y coordinate of square to scan
     * @return	Result object containing status of sonar pulse on this square
     */
    private static Result getSonarHitSquare(Board board, int x, int y) {
        Result r = new Result();
        r.setResult(board.isOccupied(x-1,y-1) ? AttackStatus.SONAR_OCCUPIED : AttackStatus.SONAR_EMPTY);
        r.setLocation(new Square(x, (char) (y + 64)));
        return r;
    }
}
