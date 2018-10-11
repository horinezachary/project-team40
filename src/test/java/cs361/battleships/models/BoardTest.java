package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

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
}
