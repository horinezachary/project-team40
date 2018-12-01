package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testDoubleattack(){
        Board board = new Board();
        board.attack(5, 'A');
        Result r = board.attack(5, 'A');
        assertEquals(AttackStatus.MISS, r.getResult());
    }

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Minesweeper(), 11, 'C', true));
        assertFalse(board.placeShip(new Minesweeper(), 6, 'J', false));
    }
    @Test
    public void testPartialInvalidPlacement() { //tests if the ship will be rejected if it hangs off the edge
        Board board = new Board();
        assertFalse(board.placeShip(new Minesweeper(), 10, 'J', true));
        assertFalse(board.placeShip(new Minesweeper(), 6, 'J', false));
    }
    @Test
    public void testNormalPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Minesweeper(), 6, 'D', false));
    }
    @Test
    public void testOverlapPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Minesweeper(), 6, 'D', false));
        assertFalse(board.placeShip(new Destroyer(), 6, 'E', false));
    }
    @Test
    public void testShipTypes() {
        Board board = new Board();
        assertTrue(board.placeShip(new Minesweeper(), 1, 'A', false));
        assertTrue(board.placeShip(new Destroyer(), 2, 'A', false));
        assertTrue(board.placeShip(new Battleship(), 3, 'A', false));
        assertTrue(board.placeShip(new Submarine(), 5, 'A', false));
    }

    @Test
    public void testEmptyShipsOnBoardByDefault() {
        // Verifies a board has no ships when first created
        Board board = new Board();
        assertEquals(0, board.getShips().size());
    }

    @Test
    public void testEmptyAttacksOnBoardByDefault() {
        // Verifies a board has no attacks when first created
        Board board = new Board();
        assertEquals(0, board.getAttacks().size());
    }

    @Test
    public void testAddingShip() {
        // Tests adding a single ship and getting it back from the board
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();
        ships.add(new Minesweeper());
        board.setShips(ships);
        assertEquals(1, board.getShips().size());
    }

    @Test
    public void testAddingDuplicateShips() {
        // Tests adding a duplicate ship, which should return false
        Board b = new Board();
        // setup initial ships
        assertTrue(b.placeShip(new Minesweeper(), 1, 'A', false));
        assertTrue(b.placeShip(new Destroyer(), 2, 'A', false));
        assertTrue(b.placeShip(new Battleship(), 3, 'A', false));

        // test duplicating them, should all return false
        assertFalse(b.placeShip(new Minesweeper(), 4, 'A', false));
        assertFalse(b.placeShip(new Destroyer(), 5, 'A', false));
        assertFalse(b.placeShip(new Battleship(), 6, 'A', false));
    }

    /*@Test
    public void testbuildWeapon() {
        Board boar
    }*/

    @Test
    public void testAddingAttack() {
        // Tests adding a single attack and getting it back from the board
        Board board = new Board();
        List<Result> attacks = new ArrayList<>();
        attacks.add(new Result());
        board.setAttacks(attacks);
        assertEquals(1, board.getAttacks().size());
    }

    /**
     * Tests attacking invalid coordinates
     */
    @Test
    public void testAttackInvalid() {
        Board board = new Board();

        Result r = board.attack(-1,'A');
        assertEquals(AttackStatus.INVALID, r.getResult());

        r = board.attack(0,'A');
        assertEquals(AttackStatus.INVALID, r.getResult());

        r = board.attack(11,'A');
        assertEquals(AttackStatus.INVALID, r.getResult());
    }


    /**
     * Tests attack miss
     */
    @Test
    public void testAttackMiss() {
        Board board = new Board();
        Result r = board.attack(1,'A');
        assertEquals(AttackStatus.MISS, r.getResult());
    }

    /**
     * Tests attack hit
     */
    @Test
    public void testAttackHitAndSunk() {
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        // create a new minesweeper to set
        Ship s = new Minesweeper();
        List<Square> spaces = new ArrayList<>();
        spaces.add(new Square(1,'A'));
        spaces.add(new Square(1,'B'));
        s.setOccupiedSquares(spaces);

        ships.add(s);

        board.setShips(ships);
        Result r = board.attack(1,'A');
        assertEquals(AttackStatus.HIT, r.getResult());
    }

    /**
     * Tests attack sunk
     */
    @Test
    public void testAttackSunk() {
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        // add 3 ships
        for(int x = 0; x < 3; x++) {
            Ship s = new Minesweeper();
            List<Square> spaces = new ArrayList<>();
            spaces.add(new Square(x+1, 'A'));
            spaces.add(new Square(x+1, 'B'));
            s.setOccupiedSquares(spaces);
            ships.add(s);
        }

        board.setShips(ships);
        // sink the 2nd ship
        board.attack(1,'A');
        Result r = board.attack(1,'B');
        assertEquals(AttackStatus.SUNK, r.getResult());

        // check to see that the last 2 events were sunk, preceded by 1 hits
        // 2nd hit became a sunk automatically
        List<Result> attacks = board.getAttacks();
        assertEquals(3, attacks.size());
        assertEquals(AttackStatus.HIT, attacks.get(0).getResult());
        assertEquals(AttackStatus.SUNK, attacks.get(1).getResult());
        assertEquals(AttackStatus.SUNK, attacks.get(2).getResult());
    }

    @Test
    public void testCaptainwithArmor(){
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        for(int x = 0; x < 3; x++){
            Ship s = new Battleship();
            Square captain = new Square(x+1, 'C');
            captain.setCaptainsQuarters(true);
            List<Square> spaces = new ArrayList<>();
            spaces.add(new Square(x+1, 'A'));
            spaces.add(new Square(x+1, 'B'));
            spaces.add(captain);
            spaces.add(new Square(x+1, 'D'));
            s.setOccupiedSquares(spaces);
            ships.add(s);
        }

        board.setShips(ships);
        // Attack the captain's quarters of the 2nd ship
        board.attack(1, 'C');
        Result r = board.attack(1, 'C');
        assertEquals(AttackStatus.SUNK, r.getResult());
    }

    @Test
    public void TestCaptainwithoutArmor(){
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        for(int x = 0; x < 3; x++){
            Ship s = new Minesweeper();
            Square captain = new Square(x+1, 'A');
            captain.setCaptainsQuarters(true);
            List<Square> spaces = new ArrayList<>();
            spaces.add(new Square(x+1, 'B'));
            spaces.add(captain);
            s.setOccupiedSquares(spaces);
            ships.add(s);
        }

        board.setShips(ships);
        // Attack the captain's quarters of the 2nd ship
        Result r = board.attack(1, 'A');
        //Result r = board.attack(1, 'C');
        assertEquals(AttackStatus.SUNK, r.getResult());
    }

    @Test
    // Attacks a portion of the ship that was auto-sunk by the captain method
    public void TestAttackonCaptainSunk(){
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        for(int x = 0; x < 3; x++){
            Ship s = new Minesweeper();
            Square captain = new Square(x+1, 'A');
            captain.setCaptainsQuarters(true);
            List<Square> spaces = new ArrayList<>();
            spaces.add(new Square(x+1, 'B'));
            spaces.add(captain);
            s.setOccupiedSquares(spaces);
            ships.add(s);
        }

        board.setShips(ships);
        // Attack the captain's quarters of the 2nd ship
        board.attack(1, 'A');
        Result r = board.attack(1, 'B');
        //Result r = board.attack(1, 'C');
        assertEquals(AttackStatus.MISS, r.getResult());
    }

    /**
     * Tests attack surrender
     */
    @Test
    public void testAttackSurrender() {
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        // create a new minesweeper to set
        Ship s = new Minesweeper();
        List<Square> spaces = new ArrayList<>();
        spaces.add(new Square(1,'A'));
        spaces.add(new Square(1,'B'));
        s.setOccupiedSquares(spaces);

        ships.add(s);

        board.setShips(ships);
        board.attack(1,'A');
        Result r = board.attack(1,'B');
        assertEquals(AttackStatus.SURRENDER, r.getResult());
    }

    /**
     * Tests default sonar values when a new board is created
     */
    @Test
    public void testSonarDefaults() {
        Board b = new Board();
        assertFalse(b.getSonarEnabled());
        assertEquals(2, b.getSonarCount());

        b.setSonarEnabled(true);
        b.setSonarCount(1);

        assertTrue(b.getSonarEnabled());
        assertEquals(1, b.getSonarCount());

    }

    @Test
    public void testSonarPulses() {
        Board b = new Board();
        b.setSonarEnabled(true);
        assertFalse(b.sonar(0,1));
        assertFalse(b.sonar(1,0));
        assertFalse(b.sonar(1,11));
        assertFalse(b.sonar(11,1));
        assertFalse(b.sonar(0,0));
        assertFalse(b.sonar(11,11));

        assertTrue(b.sonar(4, 3)); // 4,C

        // check that we were given 13 results
        List<Result> results = b.getAttacks();

        assertEquals(13, results.size());

        // check each set
        int x = 0;
        assertEquals(2, results.get(x).getLocation().getRow());
        assertEquals('C', results.get(x++).getLocation().getColumn());

        assertEquals(6, results.get(x).getLocation().getRow());
        assertEquals('C', results.get(x++).getLocation().getColumn());

        assertEquals(4, results.get(x).getLocation().getRow());
        assertEquals('A', results.get(x++).getLocation().getColumn());

        assertEquals(4, results.get(x).getLocation().getRow());
        assertEquals('E', results.get(x++).getLocation().getColumn());

        assertEquals(3, results.get(x).getLocation().getRow());
        assertEquals('B', results.get(x++).getLocation().getColumn());

        assertEquals(4, results.get(x).getLocation().getRow());
        assertEquals('B', results.get(x++).getLocation().getColumn());

        assertEquals(5, results.get(x).getLocation().getRow());
        assertEquals('B', results.get(x++).getLocation().getColumn());

        assertEquals(3, results.get(x).getLocation().getRow());
        assertEquals('C', results.get(x++).getLocation().getColumn());

        assertEquals(4, results.get(x).getLocation().getRow());
        assertEquals('C', results.get(x++).getLocation().getColumn());

        assertEquals(5, results.get(x).getLocation().getRow());
        assertEquals('C', results.get(x++).getLocation().getColumn());

        assertEquals(3, results.get(x).getLocation().getRow());
        assertEquals('D', results.get(x++).getLocation().getColumn());

        assertEquals(4, results.get(x).getLocation().getRow());
        assertEquals('D', results.get(x++).getLocation().getColumn());

        assertEquals(5, results.get(x).getLocation().getRow());
        assertEquals('D', results.get(x).getLocation().getColumn());
    }

    @Test
    public void testMoveShips(){
        Board b = new Board();
        List<Ship> ships = new ArrayList<>();

        // create a new minesweeper to set
        Ship m = new Minesweeper();
        List<Square> spaces = new ArrayList<>();
        spaces.add(new Square(1,'A'));
        spaces.add(new Square(1,'B'));
        m.setOccupiedSquares(spaces);

        ships.add(m);
        b.setShips(ships);

        assertEquals(2, b.getFleetMovecount());
        assertEquals(false, b.moveShips(-1, 0));
        printBoard(b);
        assertEquals(false, b.moveShips( 0,-1));
        printBoard(b);
        assertEquals(true,  b.moveShips( 1, 0));
        assertEquals(1, b.getFleetMovecount());
        printBoard(b);
        assertEquals(true,  b.moveShips( 0, 1));
        assertEquals(0, b.getFleetMovecount());
        printBoard(b);

        //reset location
        assertEquals(true,  b.moveShips(0,-1));
        assertEquals(-1, b.getFleetMovecount());
        //assertEquals(true,  b.moveShips(-1,0));
        printBoard(b);

        Ship d = new Destroyer();
        spaces = new ArrayList<>();
        spaces.add(new Square(5,'C'));
        spaces.add(new Square(5,'D'));
        spaces.add(new Square(5,'E'));
        d.setOccupiedSquares(spaces);
        b.addShip(d);

        assertEquals(true, b.moveShips( 1, 0));
        assertEquals(true, b.moveShips(-1, 0));
        assertEquals(true, b.moveShips( 0,-1));
        assertEquals(true, b.moveShips( 0, 1));
        printBoard(b);

        Ship battle = new Battleship();
        spaces = new ArrayList<>();
        spaces.add(new Square(1,'C'));
        spaces.add(new Square(1,'D'));
        spaces.add(new Square(1,'E'));
        spaces.add(new Square(1,'F'));
        battle.setOccupiedSquares(spaces);
        b.addShip(battle);

        assertEquals(false, b.moveShips(-1,0));
        printBoard(b);

    }

    public void printBoard(Board b){
        b.setOccupied(true);
        for (int i = 0; i < b.BOARDSIZE_X; i++){
            System.out.print("|");
            for (int j = 0; j < b.BOARDSIZE_Y; j++){
                if(b.isOccupied(i,j)){System.out.print("X|");}
                else{System.out.print(" |");}
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    @Test
    public void testLaserCreation(){
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        // add 3 ships
        for(int x = 0; x < 3; x++) {
            Ship s = new Minesweeper();
            List<Square> spaces = new ArrayList<>();
            spaces.add(new Square(x+1, 'A'));
            spaces.add(new Square(x+1, 'B'));
            s.setOccupiedSquares(spaces);
            ships.add(s);
        }

        board.setShips(ships);
        // sink the 2nd ship
        board.attack(1,'A');
        Result r = board.attack(1,'B');
        //assertEquals(AttackStatus.SUNK, r.getResult());
        assertEquals("SpaceLaser", board.getWeapon(board));

    }

    @Test
    public void testLaserFire(){
        Board board = new Board();
        List<Ship> ships = new ArrayList<>();

        // add 3 ships
        for(int x = 0; x < 3; x++) {
            Ship s = new Minesweeper();
            List<Square> spaces = new ArrayList<>();
            spaces.add(new Square(x+1, 'A'));
            spaces.add(new Square(x+1, 'B'));
            s.setOccupiedSquares(spaces);
            ships.add(s);
        }

        board.setShips(ships);
        // sink the 2nd ship
        board.attack(1,'A');
        Result r = board.attack(1,'B');
        //assertEquals(AttackStatus.SUNK, r.getResult());
        assertEquals("SpaceLaser", board.getWeapon(board));

        board.attack(2, 'A');
        Result d = board.attack(2, 'B');
        assertEquals(AttackStatus.SUNK, d.getResult());

        Result x = board.attack(5, 'A');
        assertEquals(AttackStatus.MISS, x.getResult());

        Result y = board.attack(11, 'A');
        assertEquals(AttackStatus.INVALID, y.getResult());
    }

    @Test
    public void testPlaceMinesweeper() {
        Board b = new Board();
        // vertical
        Minesweeper m = new Minesweeper();
        assertTrue(b.placeShip(m, 1, 'A', true));
        assertTrue(m.occupiesSpace(1,'A'));
        assertTrue(m.occupiesSpace(2,'A'));

        // horizontal
        b = new Board();
        m = new Minesweeper();
        assertTrue(b.placeShip(m, 1, 'A', false));
        assertTrue(m.occupiesSpace(1,'A'));
        assertTrue(m.occupiesSpace(1,'B'));
    }



    @Test
    public void testPlaceDestroyer() {
        Board b = new Board();
        // vertical
        Destroyer d = new Destroyer();
        assertTrue(b.placeShip(d, 1, 'A', true));
        assertTrue(d.occupiesSpace(1,'A'));
        assertTrue(d.occupiesSpace(2,'A'));
        assertTrue(d.occupiesSpace(3,'A'));
        assertTrue(d.getOccupiedSquares().get(1).getCaptain());

        // horizontal
        b = new Board();
        d = new Destroyer();
        assertTrue(b.placeShip(d, 1, 'A', false));
        assertTrue(d.occupiesSpace(1,'A'));
        assertTrue(d.occupiesSpace(1,'B'));
        assertTrue(d.occupiesSpace(1,'C'));
        assertTrue(d.getOccupiedSquares().get(1).getCaptain());
    }

    @Test
    public void testPlaceBattleship() {
        Board b = new Board();
        // vertical
        Battleship bs = new Battleship();
        assertTrue(b.placeShip(bs, 1, 'A', true));
        assertTrue(bs.occupiesSpace(1,'A'));
        assertTrue(bs.occupiesSpace(2,'A'));
        assertTrue(bs.occupiesSpace(3,'A'));
        assertTrue(bs.occupiesSpace(4,'A'));
        assertTrue(bs.getOccupiedSquares().get(2).getCaptain());

        // horizontal
        b = new Board();
        bs = new Battleship();
        assertTrue(b.placeShip(bs, 1, 'A', false));
        assertTrue(bs.occupiesSpace(1,'A'));
        assertTrue(bs.occupiesSpace(1,'B'));
        assertTrue(bs.occupiesSpace(1,'C'));
        assertTrue(bs.occupiesSpace(1,'D'));
        assertTrue(bs.getOccupiedSquares().get(2).getCaptain());
    }

    @Test
    public void testPlaceSubmarine() {
        Board b = new Board();
        // vertical
        Submarine s = new Submarine();
        assertTrue(b.placeShip(s, 1, 'A', true));
        assertTrue(s.occupiesSpace(1,'A'));
        assertTrue(s.occupiesSpace(2,'A'));
        assertTrue(s.occupiesSpace(3,'A'));
        assertTrue(s.occupiesSpace(3,'B'));
        assertTrue(s.occupiesSpace(4,'A'));
        assertTrue(s.getOccupiedSquares().get(3).getCaptain());

        // horizontal
        b = new Board();
        s = new Submarine();
        assertTrue(b.placeShip(s, 2, 'A', false));
        assertTrue(s.occupiesSpace(2,'A'));
        assertTrue(s.occupiesSpace(2,'B'));
        assertTrue(s.occupiesSpace(2,'C'));
        assertTrue(s.occupiesSpace(1,'C'));
        assertTrue(s.occupiesSpace(2,'D'));
        assertTrue(s.getOccupiedSquares().get(3).getCaptain());

        // test place un-submerged underlying, should not work
        b = new Board();
        s = new Submarine();
        s.setSubmerged(false);
        Destroyer d = new Destroyer();
        assertTrue(b.placeShip(d, 1, 'A', true));
        assertFalse(b.placeShip(s, 1, 'A', true));

        // test place submerged underlying, should work
        s.setSubmerged(true);
        assertTrue(b.placeShip(s, 1, 'A', true));

        // reverse order & attempt, should also work
        b = new Board();
        s = new Submarine();
        s.setSubmerged(true);
        d = new Destroyer();
        assertTrue(b.placeShip(s, 1, 'A', true));
        assertTrue(b.placeShip(d, 1, 'A', true));
    }

    @Test
    public void testAttackSubmarine() {
        Board b = new Board();
        Submarine s = new Submarine();
        assertTrue(b.placeShip(s, 1, 'A', true));

        // test attacking un-submerged w/ default weapon
        assertEquals(5, s.getHealth());
        Result r1 = b.attack(1,'A');
        assertEquals(4, s.getHealth());

        // test attacking submerged w/ default weapon
        s.setSubmerged(true);
        Result r2 = b.attack(2,'A');
        assertEquals(4, s.getHealth());

        // switch to space laser
        b.setWeapon(1);

        // attack submerged
        Result r3 = b.attack(3,'A');
        assertEquals(3, s.getHealth());

        // attack submerged (armored position)
        Result r4 = b.attack(4,'A');
        assertEquals(3, s.getHealth());

        // attack again, should work
        Result r44 = b.attack(3,'B');
        assertEquals(2, s.getHealth());

        // setup with board, destroyer & submerged sub now
        b = new Board();
        s = new Submarine();
        s.setSubmerged(true);
        Destroyer d = new Destroyer();
        assertTrue(b.placeShip(d, 1, 'A', true));
        assertTrue(b.placeShip(s, 1, 'A', true));
        assertEquals(3, d.getHealth());
        assertEquals(5, s.getHealth());

        // test attacking w/ default weapon
        Result r5 = b.attack(1,'A');
        assertEquals(5, s.getHealth());
        assertEquals(2, d.getHealth());

        // test attacking w/ laser, should penetrate
        b.setWeapon(1);
        Result r6 = b.attack(3,'A');
        assertEquals(AttackStatus.HIT, r6.getResult());
        assertEquals(1, d.getHealth());
        assertEquals(4, s.getHealth());
    }

    @Test
    public void testSinkSubmarine() {
        Board b = new Board();
        Submarine s = new Submarine();
        b.placeShip(s, 1, 'C', true);
        // 1st attack
        Result r = b.attack(1,'C');
        assertEquals(AttackStatus.HIT, r.getResult());
        assertEquals(4, s.getHealth());

        // 2nd attack
        r = b.attack(2,'C');
        assertEquals(AttackStatus.HIT, r.getResult());
        assertEquals(3, s.getHealth());

        // 3rd attack
        r = b.attack(3,'C');
        assertEquals(AttackStatus.HIT, r.getResult());
        assertEquals(2, s.getHealth());

        // 3rd attack again (just to affirm health does not change when attacking the same spot twice)
        r = b.attack(3,'C');
        assertEquals(AttackStatus.MISS, r.getResult());
        assertEquals(2, s.getHealth());

        // 4th attack (should be a miss since it's an armored CQ)
        r = b.attack(4,'C');
        assertEquals(AttackStatus.MISS, r.getResult());
        assertEquals(2, s.getHealth());

        // 5th attack on the periscope
        r = b.attack(3,'D');
        assertEquals(AttackStatus.HIT, r.getResult());
        assertEquals(1, s.getHealth());

        // 6th attack on unarmored CQ, should sink (returns surrender since it's the only board on the ship)
        r = b.attack(4,'C');
        assertEquals(AttackStatus.SURRENDER, r.getResult());
        assertEquals(0, s.getHealth());
    }

    @Test
    public void testSinkSubmergedSubmarineLaser() {
        Board b = new Board();
        Submarine s = new Submarine();
        s.setSubmerged(true);
        b.setWeapon(1);

        b.placeShip(s, 1, 'C', true);
        // 1st attack
        Result r = b.attack(1,'C');
        assertEquals(AttackStatus.HIT, r.getResult());
        assertEquals(4, s.getHealth());

        // 2nd attack
        r = b.attack(2,'C');
        assertEquals(AttackStatus.HIT, r.getResult());
        assertEquals(3, s.getHealth());

        // 3rd attack
        r = b.attack(3,'C');
        assertEquals(AttackStatus.HIT, r.getResult());
        assertEquals(2, s.getHealth());

        // 4th attack (should be a miss since it's an armored CQ)
        r = b.attack(4,'C');
        assertEquals(AttackStatus.MISS, r.getResult());
        assertEquals(2, s.getHealth());

        // 5th attack on the periscope
        r = b.attack(3,'D');
        assertEquals(AttackStatus.HIT, r.getResult());
        assertEquals(1, s.getHealth());

        // 6th attack on unarmored CQ, should sink (returns surrender since it's the only board on the ship)
        r = b.attack(4,'C');
        assertEquals(AttackStatus.SURRENDER, r.getResult());
        assertEquals(0, s.getHealth());
    }
}
