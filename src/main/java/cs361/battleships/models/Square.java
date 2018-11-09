package cs361.battleships.models;

@SuppressWarnings("unused")
public class Square {

	private int row;
    private char column;
    private boolean captain;

	public Square() {}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
		this.captain = false;
	}

	public char getColumn() {
		return column;
	}

	public void setColumn(char column) {
		this.column = column;
	}

    /**
     * Sets whether this square is a captain's quarters
     *
     * @param isCaptainQuarters Whether this square should be a captain's quarters
     */
	public void setCaptainsQuarters(boolean isCaptainQuarters) {
	    this.captain = isCaptainQuarters;
    }

	public boolean getCaptain() {
		return captain;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
}
