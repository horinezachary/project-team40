package cs361.battleships.models;

public abstract class Weapon {
    protected boolean Pierce;
    public Weapon(){
        Pierce = false;
    }

    public abstract Result Fire(Board board, int x, char y, Result r);

    public Result HitandRemoval(Ship ship, boolean didHit, Board board, Result r){
        if(ship.getHealth() == 0) {
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
                //board.setLaserEnabled(true);
                //board.setSonarEnabled(true);
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

        } else{
            // just a hit, not sunk
            r.setResult(AttackStatus.HIT);
            board.addAttack(r);
            return r;

        }
       // r.setResult(AttackStatus.MISS);
        //return r;
    }

    /**
     * Checks and updates a ship based on whether it was hit or not
     *
     * @param r				Result to update with the location of a hit (if any)
     * @param ship			Ship that should be checked/updated for hits
     * @param shipSquare	Square to check for a hit on
     * @return	Whether the ship was hit or not
     */
    public static boolean checkAndUpdateForHit(Result r, Ship ship, Square shipSquare) {
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
