package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import static cs361.battleships.models.AtackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;

        // setup a new ship for the bot
        Ship s2 = new Ship(ship.getShipType());

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
