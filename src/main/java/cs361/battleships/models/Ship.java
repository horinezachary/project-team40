package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class Ship {

    int length;
    String shipType;

	@JsonProperty private List<Square> occupiedSquares;
	
	public Ship(String kind) {
		switch(kind) {
            case "Carrier":
                length = 5;
                break;
            case "Battleship":
                length = 4;
                break;
            case "Cruiser":
                length = 3;
                break;
            case "Submarine":
                length = 3;
                break;
            case "Destroyer":
                length = 2;
                break;
            default:
                length = 0;
                break;
        }
        shipType = kind;
		occupiedSquares = new ArrayList<>();
	}

	public List<Square> getOccupiedSquares() {
		//TODO implement
		return null;
	}
}
