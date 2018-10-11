package cs361.battleships.models;

@SuppressWarnings("unused")
public class Square {

	private int row;
	private char column;
	private boolean occupied;

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
		this.occupied = false;
	}

	public char getColumn() {
		return column;
	}

	public void setColumn(char column) {
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setOccupied(boolean oc){
		occupied = oc;
	}
	public boolean getOccupied(){
		return occupied;
	}
}
