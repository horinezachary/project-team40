package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ResultTest {

    @Test
    public void testResultSet(){
        Result res = new Result();

        Ship testShip = new Ship("MINESWEEPER");
        res.setShip(testShip);
        assertEquals(res.getShip(), testShip);

        Square testSquare = new Square(3, 'D');
        res.setLocation(testSquare);
        assertEquals(res.getLocation(), testSquare);
    }
}

