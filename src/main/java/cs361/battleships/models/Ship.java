package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class Ship {

    int health;
    int length;
    boolean armor;
    String shipType;

	@JsonProperty private List<Square> occupiedSquares;

	public Ship(){
        occupiedSquares = new ArrayList<>();
    }
	
	public Ship(String kind) {
        //save the ship string
        shipType = kind;
		occupiedSquares = new ArrayList<>();
		armor = false;
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

    public void forceSink(){
        health = 0;
	}

    public int getHealth() {
	    return health;
    }

    public void setArmor(boolean armor) {
	    this.armor = armor;
    }

    public boolean getArmor() {
	    return armor;
    }

}
