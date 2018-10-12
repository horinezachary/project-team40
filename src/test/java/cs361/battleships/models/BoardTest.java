package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testDoubleattack(){
        Board board = new Board();
        board.attack(5, 'A');
        Result r = board.attack(5, 'A');
        assertEquals(AtackStatus.INVALID, r.getResult());
    }

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 6, 'J', false));
    }
    @Test
    public void testPartialInvalidPlacement() { //tests if the ship will be rejected if it hangs off the edge
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 10, 'J', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 6, 'J', false));
    }
    @Test
    public void testNormalPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 6, 'D', false));
    }
    @Test
    public void testOverlapPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 6, 'D', false));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 6, 'E', false));
    }
    @Test
    public void testShipTypes() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', false));
        assertTrue(board.placeShip(new Ship("DESTROYER"), 2, 'A', false));
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 3, 'A', false));
    }

    @Test
    public void testEmptyShipsOnBoardByDefault() {
        // Verifies a board has no ships when first created
        Board board = new Board();
        assertEquals(0, board.getShips().size());
    }

    @Test
    public void testEmptyAttacksOnBoardByDefault() {
        // Verifies a board has no attacks when first created
        Board board = new Board();
        assertEquals(0, board.getAttacks().size());
    }

    @Test
    public void testAddingShip() {
        // Tests adding a single ship and getting it back from the board
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();
        ships.add(new Ship("MINESWEEPER"));
        board.setShips(ships);
        assertEquals(1, board.getShips().size());
    }

    @Test
    public void testAddingAttack() {
        // Tests adding a single attack and getting it back from the board
        Board board = new Board();
        List<Result> attacks = new ArrayList<>();
        attacks.add(new Result());
        board.setAttacks(attacks);
        assertEquals(1, board.getAttacks().size());
    }

    /**
     * Tests attacking invalid coordinates
     */
    @Test
    public void testAttackInvalid() {
        Board board = new Board();

        Result r = board.attack(-1,'A');
        assertEquals(AtackStatus.INVALID, r.getResult());

        r = board.attack(0,'A');
        assertEquals(AtackStatus.INVALID, r.getResult());

        r = board.attack(11,'A');
        assertEquals(AtackStatus.INVALID, r.getResult());
    }

    /**
     * Tests attack miss
     */
    @Test
    public void testAttackMiss() {
        Board board = new Board();
        Result r = board.attack(1,'A');
        assertEquals(AtackStatus.MISS, r.getResult());
    }

    /**
     * Tests attack hit
     */
    @Test
    public void testAttackHitAndSunk() {
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        // create a new minesweeper to set
        Ship s = new Ship("MINESWEEPER");
        List<Square> spaces = new ArrayList<>();
        spaces.add(new Square(1,'A'));
        spaces.add(new Square(1,'B'));
        s.setOccupiedSquares(spaces);

        ships.add(s);

        board.setShips(ships);
        Result r = board.attack(1,'A');
        assertEquals(AtackStatus.HIT, r.getResult());
    }

    /**
     * Tests attack sunk
     */
    @Test
    public void testAttackSunk() {
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        // add 3 ships
        for(int x = 0; x < 3; x++) {
            Ship s = new Ship("MINESWEEPER");
            List<Square> spaces = new ArrayList<>();
            spaces.add(new Square(x+1, 'A'));
            spaces.add(new Square(x+1, 'B'));
            s.setOccupiedSquares(spaces);
            ships.add(s);
        }

        board.setShips(ships);
        // sink the 2nd ship
        board.attack(1,'A');
        Result r = board.attack(1,'B');
        assertEquals(AtackStatus.SUNK, r.getResult());

        // check to see that the last 2 events were sunk, preceded by 1 hits
        // 2nd hit became a sunk automatically
        List<Result> attacks = board.getAttacks();
        assertEquals(3, attacks.size());
        assertEquals(AtackStatus.HIT, attacks.get(0).getResult());
        assertEquals(AtackStatus.SUNK, attacks.get(1).getResult());
        assertEquals(AtackStatus.SUNK, attacks.get(2).getResult());
    }

    /**
     * Tests attack surrender
     */
    @Test
    public void testAttackSurrender() {
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        // create a new minesweeper to set
        Ship s = new Ship("MINESWEEPER");
        List<Square> spaces = new ArrayList<>();
        spaces.add(new Square(1,'A'));
        spaces.add(new Square(1,'B'));
        s.setOccupiedSquares(spaces);

        ships.add(s);

        board.setShips(ships);
        board.attack(1,'A');
        Result r = board.attack(1,'B');
        assertEquals(AtackStatus.SURRENDER, r.getResult());
    }
}
