package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class Ship {

    int health;
    int length;
    String shipType;

	@JsonProperty private List<Square> occupiedSquares;

	public Ship(){
        occupiedSquares = new ArrayList<>();
    }
	
	public Ship(String kind) {
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

    public void decrementHealth() {
	    health--;
    }

    public int getHealth() {
	    return health;
    }
}
