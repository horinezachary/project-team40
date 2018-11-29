package cs361.battleships.models;

import java.util.List;

public class SpaceLaser extends Weapon {
    public SpaceLaser(){
        Pierce = true;
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
                        if(ship.getShipType() == "SUBMARINE"){
                            // Call new function for submarine?
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
        board.addAttack(r);
        return r;
    }

}
