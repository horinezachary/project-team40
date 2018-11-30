package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SubmarineTest {

    @Test
    public void testSubmarine() {
        Submarine s = new Submarine();
        assertFalse(s.getSubmerged());
        s.setSubmerged(true);
        assertTrue(s.getSubmerged());
    }
}
