package cs361.battleships.models;

public class Battleship extends Ship {
	public Battleship() {
		super();
		length = 4;
		health = length;
		shipType = "BATTLESHIP";
	}
}
