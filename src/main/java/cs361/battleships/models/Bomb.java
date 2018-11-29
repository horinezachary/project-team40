package cs361.battleships.models;

import java.util.List;

public class Bomb extends Weapon{
    public Bomb(){
    }

    public Result Fire(Board board, int x, char y, Result r) {
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
                        didHit = checkAndUpdateForHit(r, ship, shipSquare);
                        break;

                    }
                }
                if(didHit) {
                    r = HitandRemoval(ship, true, board, r);
                    return r;
                }
               /*if(didHit && ship.getHealth() == 0) {
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

                } else if(didHit) {
                    // just a hit, not sunk
                    r.setResult(AttackStatus.HIT);
                    board.addAttack(r);
                    return r;

                }*/
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

