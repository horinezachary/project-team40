package cs361.battleships.models;

import java.util.List;

public class SpaceLaser extends Weapon {
    public SpaceLaser(){
        WeaponName = "SpaceLaser";
    }

    public Result Fire(Board board, int x, char y, Result r) {
        boolean subhit = false;
        if(x >= 1 && x <= 10 && y >= 'A' && y <= 'J') {
            // loop over each ship to check for a hit
            for(Ship ship: board.getShips()) {
                // get this ship's squares
                boolean didHit = false;
                List<Square> squares = ship.getOccupiedSquares();
                for(Square shipSquare: squares) {
                    if(shipSquare.getRow() == x && shipSquare.getColumn() == y) {
                        // Hit! mark this ship down health
                        //if()
                        if(ship.getShipType() == "SUBMARINE"){  //This should be changed to an underwater property, but for now this is the logic
                            // Call new function for submarine?
                            // Given the logic of game, it returns "true" when something is hit, but THESE functions are the ones that add the hit to the board's attack array.
                            checkAndUpdateForHit(r, ship, shipSquare);
                            r = HitandRemoval(ship, board, r);
                            subhit = true;
                            break;
                        }
                        didHit = checkAndUpdateForHit(r, ship, shipSquare);
                        break;

                    }
                }
                if(didHit) {
                    r = HitandRemoval(ship, board, r);
                    return r;
                }

            }

            // valid coordinates, determine if there is a ship here
            if(subhit){
                return r;
            }
            r.setResult(AttackStatus.MISS);

        } else {
            // invalid coordinates, return early without adding this result
            r.setResult(AttackStatus.INVALID);
            return r;

        }
        board.addAttack(r); // This one adds the attackStatus MISS, the rest are added by the "HitandRemoval" function/
        return r;
    }

}
