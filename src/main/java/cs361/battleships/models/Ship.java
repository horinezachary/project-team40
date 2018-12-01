package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class Ship {

    int health;
    int length;
    boolean armor;
    String shipType;
    boolean submerged;
    List<Square> attackedAt;

	@JsonProperty private List<Square> occupiedSquares;

	public Ship(){
        occupiedSquares = new ArrayList<>();
    }
	
	public Ship(String kind) {
        //save the ship string
        shipType = kind;
		occupiedSquares = new ArrayList<>();
		armor = false;
        submerged = false;
        attackedAt = new ArrayList<>();
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
  
    /**
     * Moves the ship by the values in x and y
     * @param x
     * @param y
     */
    public void move(int x, int y){
        //System.out.print("MOVE:");
	    for (int i = 0; i < occupiedSquares.size(); i++){
	        Square square = occupiedSquares.get(i);
	        //System.out.print((char)((int)square.getColumn()+x) + "," + (square.getRow()+y) + " : ");
	        square.setColumn((char)((int)square.getColumn()+x));
            square.setRow(square.getRow()+y);
        }
	    //System.out.print("\n");

        // adjust priorly attacked at positions
        if(attackedAt != null) {
            for (int q = 0; q < attackedAt.size(); q++) {
                Square s = attackedAt.get(q);
                s.setColumn((char) ((int) s.getColumn() + x));
                s.setRow(s.getRow() + y);
            }
        }
    }
    public void setSubmerged(boolean s) {
	    submerged = s;
    }

    public boolean getSubmerged() {
	    return submerged;
    }

    public List<Square> getAttackedAt() {
	    return attackedAt;
    }

    public void setAttackedAt(List<Square> attackedAt) {
	    this.attackedAt = attackedAt;
    }

    public void addAttackedAt(Square s) {
	    if(attackedAt == null) {
	        attackedAt = new ArrayList<Square>();
        }
	    attackedAt.add(s);
    }

    public boolean wasAttackedAt(Square ns) {
	    if(attackedAt != null) {
            for (Square as : attackedAt) {
                if (as.getRow() == ns.getRow() && as.getColumn() == ns.getColumn()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks to make a given square the captain's quarters
     *
     * @param square    Square to check to make a captain's quarters
     * @param i		    Index along length of ship
     */
    public void checkToMakeCaptainsQuarters(Square square, int i) {
        if (i == (getLength() - 2)) {
            // make this square a captain's quarters
            square.setCaptainsQuarters(true);

        }
    }

    /**
     * Returns whether a ship occupies a space matching the given coordinates
     * @param x X coordinate to check
     * @param y Y coordinate to check
     * @return  Whether the space is occupied by this shape at the given coords
     */
    public boolean occupiesSpace(int x, char y) {
        for (Square s: occupiedSquares) {
            if(s.getRow() == x && s.getColumn() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether this ship is a duplicate of an existing one
     *
     * @param ship  Ship to check for duplication
     * @param ships List of ships to check against
     * @return whether or not the ship is a duplicate
     */
    public static boolean is_duplicate_ship(Ship ship, List<Ship> ships) {
        for(Ship s: ships) {
            if(s.getShipType().equals(ship.getShipType())) {
                // same type
                return true;
            }
        }
        return false;
    }
}
