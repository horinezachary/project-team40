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
        ships.add(new Ship("Minesweeper"));
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
}
