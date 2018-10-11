package cs361.battleships.models;
import org.junit.Test;

import static org.junit.Assert.*;


public class GameTest {

    Game testgame = new Game();
    Game gametwo = new Game();


    @Test
    public void testBadplaceship(){ //
        String shipname = "Battleship";
        Ship shippy = new Ship(shipname);
        assertEquals(false,testgame.placeShip(shippy, 12, 'A', true));
    }

    @Test
    public void testGoodplaceship(){
        String shipname2 = "Battleship";
        Ship shipper = new Ship(shipname2);
        assertEquals(true, gametwo.placeShip(shipper, 3, 'A', true));
    }

    @Test
    public void testBadattack(){
        Game game = new Game();
        assertFalse(game.attack(11, 'C'));

    }

    @Test
    public void testGoodattack(){
        Game game = new Game();
        assertTrue(game.attack(9, 'A'));
    }


}
