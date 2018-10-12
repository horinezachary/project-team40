package cs361.battleships.models;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;


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
        assertFalse(game.attack(11,'J'));
        // test bad, with high Y
        assertFalse(game.attack(9,'K'));
        // test bad with both high
        assertFalse(game.attack(10,'K'));

    }

    @Test
    public void testGoodattack(){
        Game game = new Game();
        assertTrue(game.attack(10, 'A'));
        assertTrue(game.attack(1,'J'));
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

    /**
     * Ensures player & bot ships are not accidentally the same
     */
    @Test
    public void testPlayerAndBotShipsIndependent() {
        Game g = new Game();
        g.placeShip(new Ship("MINESWEEPER"), 5,'B',true);

        // get the player's ship
        Board b1 = g.getPlayersBoard();
        List<Ship> playerShips = b1.getShips();
        Ship playerShip = playerShips.get(0);

        // get the bot's ship
        Board b2 = g.getOpponentsBoard();
        List<Ship> botShips = b2.getShips();
        Ship botShip = botShips.get(0);

        assertNotEquals(playerShip, botShip);
    }

}
