package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.Math;
import static cs361.battleships.models.AttackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        Ship s2;
        switch(ship.getShipType()) {
            case "BATTLESHIP":
                ship = new Battleship();
                s2 = new Battleship();
                break;
            case "DESTROYER":
                ship = new Destroyer();
                s2 = new Destroyer();
                break;
            case "MINESWEEPER":
                ship = new Minesweeper();
                s2 = new Minesweeper();
                break;
            case "SUBMARINE":
                ship = new Submarine();
                s2 = new Submarine();
                break;
            default:
                return false;
        }

        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(s2, randRow(), randCol(), randVertical());
        } while (!opponentPlacedSuccessfully);

        return true;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char  y) {
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);

        return true;
    }


    /**
     * Performs sonar of the enemy player's board
     *
     * @param x X coordinate to perform the sonar at
     * @param y Y coordinate to perform the sonar at
     * @return
     */
    public boolean sonar(int x, char y) {
        if(!opponentsBoard.getSonarEnabled() || opponentsBoard.getSonarCount() <= 0) {
            // sonar is not enabled yet or no more sonars left
            return false;

        }

        // perform the sonar on the player's board
        if(!opponentsBoard.sonar(x, y-64)) {
            // sonar failed for some reason
            System.out.println("* sonar failed for player");
            return false;

        }

        return true;
    }

    char randCol() {
	    String column = "ABCDEFGHIJ";
	    int x = (int)(Math.random()*10);
        return column.charAt(x);
    }

    int randRow() {
        return (int)(Math.random()*10)+1;
    }

    private boolean randVertical() {
	    double x = Math.random();
	    if(x > 0.50){
		    return true;
	    }
	    else {
	        return false;
   	    }
   }

    /**
     * Returns the player's current board
     * @return Board
     */
   public Board getPlayersBoard() {
        return playersBoard;
   }

    /**
     * Returns the opponent's board
     * @return board
     */
   public Board getOpponentsBoard() {
        return opponentsBoard;
   }
}
