package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
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
    }

    /**
     * Tests attack miss
     */
    @Test
    public void testAttackMiss() {
        Board board = new Board();
        Result r = board.attack(0,'A');
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
        spaces.add(new Square(0,'A'));
        spaces.add(new Square(0,'B'));
        s.setOccupiedSquares(spaces);

        ships.add(s);

        board.setShips(ships);
        Result r = board.attack(0,'A');
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
            spaces.add(new Square(x, 'A'));
            spaces.add(new Square(x, 'B'));
            s.setOccupiedSquares(spaces);
            ships.add(s);
        }

        board.setShips(ships);
        // sink the 2nd ship
        board.attack(1,'A');
        Result r = board.attack(1,'B');
        assertEquals(AtackStatus.SUNK, r.getResult());
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
        spaces.add(new Square(0,'A'));
        spaces.add(new Square(0,'B'));
        s.setOccupiedSquares(spaces);

        ships.add(s);

        board.setShips(ships);
        board.attack(0,'A');
        Result r = board.attack(0,'B');
        assertEquals(AtackStatus.SURRENDER, r.getResult());
    }
}
