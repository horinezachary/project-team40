package cs361.battleships.models;


import java.util.List;

public class SpaceLaser extends Weapon {

    public SpaceLaser(){
        WeaponName = "SpaceLaser";
    }

    public Result Fire(Board board, int x, char y, Result r) {

        // 'highest' (most damaging) result to return
        Result highestResult = null;

        if(x >= 1 && x <= 10 && y >= 'A' && y <= 'J') {
            // loop over each ship to check for a hit
            for(int index = 0; index < board.getShips().size(); index++) {
                Ship ship = board.getShips().get(index);
                // get this ship's squares
                boolean didHit = false;
                List<Square> squares = ship.getOccupiedSquares();
                for(Square shipSquare: squares) {
                    if(shipSquare.getRow() == x && shipSquare.getColumn() == y) {
                        // Hit! mark this ship down health
                        /*
                        if(ship.getShipType().equals("SUBMARINE")){  //This should be changed to an underwater property, but for now this is the logic
                            // Call new function for submarine?
                            // Given the logic of game, it returns "true" when something is hit, but THESE functions are the ones that add the hit to the board's attack array.
                            checkAndUpdateForHit(r, ship, shipSquare);
                            r = HitandRemoval(ship, board, r);
                            subhit = true;
                            break;
                        }
                        */
                        didHit = checkAndUpdateForHit(r, ship, shipSquare);
                        break;

                    }
                }
                if(didHit) {
                    r = HitandRemoval(ship, board, r);
                    // store this result if it's the new highest result
                    // the space laser can hit multiple targets, and we want to return
                    // the highest damaging result specifically
                    if(r.getResult() == AttackStatus.SURRENDER) {
                        // highest precedence
                        highestResult = r;

                    } else if (r.getResult() == AttackStatus.HIT && highestResult == null) {
                        highestResult = r;

                    } else if(r.getResult() == AttackStatus.SUNK && r.getResult() != AttackStatus.SURRENDER) {
                        // override for SUNK in other cases
                        highestResult = r;

                    }
                }

            }

            if(highestResult == null) {
                // valid coordinates, determine if there is a ship here
                r.setResult(AttackStatus.MISS);

            } else {
                // set back our highest result
                r = highestResult;

            }

        } else {
            // invalid coordinates, return early without adding this result
            r.setResult(AttackStatus.INVALID);
            return r;

        }
        board.addAttack(r); // This one adds the attackStatus MISS, the rest are added by the "HitandRemoval" function/
        return r;
    }

}
