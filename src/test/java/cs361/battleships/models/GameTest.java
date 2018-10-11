package cs361.battleships.models;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GameTest {

    Game testgame = new Game();
    Game gametwo = new Game();


    @Test
    public void testBadplaceship(){ //
        String shipname = "BATTLESHIP";
        Ship shippy = new Ship(shipname);
        assertEquals(false,testgame.placeShip(shippy, 12, 'A', true));
    }

    @Test
    public void testGoodplaceship(){
        String shipname2 = "BATTLESHIP";
        Ship shipper = new Ship(shipname2);
        assertEquals(true, gametwo.placeShip(shipper, 5, 'A', true));
        assertEquals(true, gametwo.placeShip(shipper, 1, 'A', true));
    }

    @Test
    public void testBadattack(){
        Game game = new Game();
        assertFalse(game.attack(11, 'C'));
        // test bad, with low X
        assertFalse(game.attack(0,'K'));
        // test bad, with high X
        assertFalse(game.attack(10,'J'));
        // test bad, with high Y
        assertFalse(game.attack(9,'K'));
        // test bad with both high
        assertFalse(game.attack(10,'K'));

    }

    @Test
    public void testGoodattack(){
        Game game = new Game();
        assertTrue(game.attack(9, 'A'));
        assertTrue(game.attack(0,'J'));
    }

    /**
     * Attempts to attack a space with a ship on it
     */
    @Test
    public void testAttackOnShip() {
        // setup a new game & add a ship
        Game g = new Game();
        Ship s = new Ship("DESTROYER");

        // attempt to place the ship
        assertTrue(g.placeShip(s,1,'A',false));

        // attack the space with this ship now
        assertTrue(g.attack(1,'A'));

    }

}
