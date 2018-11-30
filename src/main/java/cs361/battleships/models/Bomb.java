package cs361.battleships.models;

import java.util.List;

public class Bomb extends Weapon{
    public Bomb(){
        WeaponName = "Bomb";
    }

    public Result Fire(Board board, int x, char y, Result r) {
        if(x >= 1 && x <= 10 && y >= 'A' && y <= 'J') {
            // loop over each ship to check for a hit
            for(Ship ship: board.getShips()) {
                // get this ship's squares
                boolean didHit = false;
                List<Square> squares = ship.getOccupiedSquares();
                for(Square shipSquare: squares) {
                    if(shipSquare.getRow() == x && shipSquare.getColumn() == y && !ship.getSubmerged()) { //  Need to add an "underwater property" denial
                        // Hit! mark this ship down health
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

