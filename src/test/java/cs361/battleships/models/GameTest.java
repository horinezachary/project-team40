package cs361.battleships.models;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameTest {

    /**
     * Tests an attack on an invalid space
     */
    @Test
    public void testInvalidAttacks() {
        Game g = new Game();
        // test bad, with low Y
        assertFalse(g.attack(-1,'A'));
        // test bad, with low X
        assertFalse(g.attack(0,'K'));
        // test bad, with high Y
        assertFalse(g.attack(10,'J'));
        // test bad, with high X
        assertFalse(g.attack(9,'K'));
        // test bad with both high
        assertFalse(g.attack(10,'K'));
    }

    /**
     * Tests an attack on a space that exists
     */
    @Test
    public void testValidAttacks() {
        Game g = new Game();
        // test upper & lower limits of good coordinates
        // note there are not ships here
        assertTrue(g.attack(0,'A'));
        assertTrue(g.attack(9,'J'));
    }

    /**
     * Attempts to attack a space with a ship on it
     */
    @Test
    public void testAttackOnShip() {
        // setup a new game & add a ship
        Game g = new Game();
        Ship s = new Ship("Destroyer");

        // attempt to place the ship
        assertTrue(g.placeShip(s,0,'A',false));

        // attack the space with this ship now
        assertTrue(g.attack(0,'A'));

    }

}
