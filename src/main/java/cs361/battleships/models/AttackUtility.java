package cs361.battleships.models;

import java.util.List;

/**
 * Handles the action of attacking a space on the board
 */
public class AttackUtility {

    public static Result attack(Board board, int x, char y) {
        Result r = new Result();
        r.setLocation(new Square(x,y));

        for(Result check: board.getAttacks()) { //begin iterating through attack list
            if(isResultSpaceAlreadyAttacked(check, x, y)) {
                // already attacked this space, return invalid
                r.setResult(AttackStatus.INVALID);
                return r;

            } else if(wasSpaceFormerlyArmoredCaptain(check, x, y)) {
                // FOR SPRINT #3
                // attacking a previously 'captained' space that was missed
                // we're going to update the result so it registers as a SINK properly
                // If this change is NOT made the previously 'missed' captain's quarters space
                // does not register a sink or hit, it remains missed while the rest is updated visually.
                check.setResult(AttackStatus.SUNK);

            }
        }
        r = board.useWeapon(board, x, y, r);

        /*if(x >= 1 && x <= 10 && y >= 'A' && y <= 'J') {
            // loop over each ship to check for a hit
            for(Ship ship: board.getShips()) {
                // get this ship's squares
                boolean didHit = false;
                List<Square> squares = ship.getOccupiedSquares();
                for(Square shipSquare: squares) {
                    if(shipSquare.getRow() == x && shipSquare.getColumn() == y) {
                        // Hit! mark this ship down health
                        //if()
                        didHit = checkAndUpdateForHit(r, ship, shipSquare);
                        break;

                    }
                }

                if(didHit && ship.getHealth() == 0) {
                    // hit and SUNK, remove this ship
                    board.removeShip(ship);

                    // mark each part of this ship as SUNK
                    // CSS of 'sink' class will take precedence over hit
                    // turning all affected pieces red
                    Result rr = new Result();
                    for(Square sq: ship.getOccupiedSquares()) {
                        rr = new Result();
                        rr.setLocation(sq);
                        rr.setShip(ship);
                        rr.setResult(AttackStatus.SUNK);
                      //  board.setLaserEnabled(true);
                        board.setSonarEnabled(true);
                        board.addAttack(rr);
                    }

                    // check to see if the game is over
                    if(board.getShips().size() > 0) {
                        // at least one more ship, return our last sunken result
                        return rr;

                    } else {
                        // this was the last ship, game over!
                        r.setResult(AttackStatus.SURRENDER);
                        board.addAttack(r);
                        return r;

                    }

                } else if(didHit) {
                    // just a hit, not sunk
                    r.setResult(AttackStatus.HIT);
                    board.addAttack(r);
                    return r;

                }
            }

            // valid coordinates, determine if there is a ship here
            r.setResult(AttackStatus.MISS);

        } else {
            // invalid coordinates, return early without adding this result
            r.setResult(AttackStatus.INVALID);
            return r;

        }

        board.addAttack(r);*/
        return r;
    }


    /**
     * Returns whether a space was already attacked
     *
     * @param check	Result to check
     * @param x		X to compare for row
     * @param y		Y to compare for column
     * @return	Whether the space was already attacked
     */
    private static boolean isResultSpaceAlreadyAttacked(Result check, int x, char y) {
        return check.getLocation() != null &&
                check.getLocation().getRow() == x &&
                check.getLocation().getColumn() == y &&
                check.getResult() != AttackStatus.SONAR_EMPTY &&
                check.getResult() != AttackStatus.SONAR_OCCUPIED &&
                !check.getLocation().getCaptain();
    }


    /**
     * Returns whether a space was a formerly armored captain's square, and is a valid attack again
     *
     * @param check	Result to check
     * @param x		X to compare for row
     * @param y		Y to compare for col
     * @return	Whether the space was a formerly armored captain, and is a valid space to attack again
     */
    private static boolean wasSpaceFormerlyArmoredCaptain(Result check, int x, char y) {
        return check.getLocation() != null &&
                check.getLocation().getCaptain() &&
                check.getLocation().getRow() == x &&
                check.getLocation().getColumn() == y &&
                check.getResult() == AttackStatus.MISS;
    }


    /**
     * Checks and updates a ship based on whether it was hit or not
     *
     * @param r				Result to update with the location of a hit (if any)
     * @param ship			Ship that should be checked/updated for hits
     * @param shipSquare	Square to check for a hit on
     * @return	Whether the ship was hit or not
     */
    private static boolean checkAndUpdateForHit(Result r, Ship ship, Square shipSquare) {
        boolean didHit = false;
        // Hit! mark this ship down health
        if(shipSquare.getCaptain() && ship.getArmor()) {
            // hit captain's quarters but counts as a miss, was still armored
            ship.setArmor(false);

            // readjust 'Result' location to show it was a captain's quarter
            Square lloc = r.getLocation();
            lloc.setCaptainsQuarters(true);
            r.setLocation(lloc);

        } else if(shipSquare.getCaptain() && !ship.getArmor()) {
            // hit unarmored captain's quarters, sink the ship
            ship.forceSink();
            didHit = true;
            r.setShip(ship);

        } else {
            // normal hit
            ship.decrementHealth();
            didHit = true;
            r.setShip(ship);

        }

        return didHit;

    }
}
