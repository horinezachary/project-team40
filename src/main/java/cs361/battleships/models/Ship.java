package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;
	
	public Ship(String kind) {
		//TODO implement
		occupiedSquares = new ArrayList<>();
	}

	public List<Square> getOccupiedSquares() {
		//TODO implement
		return null;
	}
}
