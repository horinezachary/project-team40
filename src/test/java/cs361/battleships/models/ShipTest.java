package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShipTest {

    @Test
    public void testHealth() {
        // test default healths
        Ship s = new Ship("NotAShip");
        assertEquals(0, s.getHealth());

        s = new Ship("BATTLESHIP");
        assertEquals(4, s.getHealth());

        s = new Ship("DESTROYER");
        assertEquals(3, s.getHealth());

        s = new Ship("MINESWEEPER");
        assertEquals(2, s.getHealth());

        // test decrementing health
        s.decrementHealth();
        assertEquals(1, s.getHealth());
    }
}
