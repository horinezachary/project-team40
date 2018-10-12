package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SquareTest {

    @Test
    public void testSquareSetAndGet() {
        Square s = new Square();
        s.setRow(7);
        s.setColumn('C');
        assertEquals(7, s.getRow());
        assertEquals('C', s.getColumn());
    }

}
