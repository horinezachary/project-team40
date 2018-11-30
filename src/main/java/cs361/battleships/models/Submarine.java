package cs361.battleships.models;

public class Submarine extends Ship {

    public Submarine() {
        super();
        health = 5; // health accounts for the periscope
        length = 4; // not accounting for the periscope!
        shipType = "SUBMARINE";
        armor = true;
    }

    @Override
    public void checkToMakeCaptainsQuarters(Square square, int i) {
        if(i == 3) {
            // far end square is our captain's quarters for the submarine
            square.setCaptainsQuarters(true);
        }
    }
}
