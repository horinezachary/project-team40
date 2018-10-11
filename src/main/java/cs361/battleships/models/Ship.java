package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class Ship {

    int length;
    String shipType;

	@JsonProperty private List<Square> occupiedSquares;
	
	public Ship(String kind) {
		//use the string given to set the length of the ship
		switch(kind) {
            case "BATTLESHIP":
                length = 4;
                break;
            case "DESTROYER":
                length = 3;
                break;
            case "MINESWEEPER":
                length = 2;
                break;
            default:
                length = 0;
                break;
        }
        //save the ship string
        shipType = kind;
		occupiedSquares = new ArrayList<>();
	}

	public List<Square> getOccupiedSquares() {
	    return occupiedSquares;
	}

	public void setOccupiedSquares(List<Square> squaresIn){
		occupiedSquares = squaresIn;
    }
    public int getLength() {
	    return length;
    }
    public String getShipType() {
	    return shipType;
    }
}
