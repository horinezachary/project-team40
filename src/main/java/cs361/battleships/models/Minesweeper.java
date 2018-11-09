package cs361.battleships.models;

public class Minesweeper extends Ship {
	public Minesweeper(){
		super();
		length = 2;
		health = length;
		shipType = "MINESWEEPER";
	}
}
