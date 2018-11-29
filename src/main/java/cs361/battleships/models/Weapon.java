package cs361.battleships.models;

public abstract class Weapon {
    protected boolean Pierce;
    public Weapon(){
        Pierce = false;
    }

    public abstract Result Fire(Board board, int x, char y, Result r);

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
