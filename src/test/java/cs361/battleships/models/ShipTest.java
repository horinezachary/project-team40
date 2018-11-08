package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShipTest {

    @Test
    public void testHealth() {

        Ship s1 = new Battleship();
        assertEquals(4, s1.getHealth());

        Ship s2 = new Destroyer();
        assertEquals(3, s2.getHealth());

        Ship s3 = new Minesweeper();
        assertEquals(2, s3.getHealth());

        // test decrementing health
        s3.decrementHealth();
        assertEquals(1, s3.getHealth());
    }
}
