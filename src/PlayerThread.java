import java.util.Random;

/**
 * This class runs the actual races. The as threads cannot be started
 * more than once we need new player threads each time so this will
 * just get each player's acceleration, speed, and random value and start
 */
public class PlayerThread extends Thread {

    private int acceleration;
    private int speed;
    private Random random;
    private int last_race;

    /**
     * Player thread constructor
     * @param acceleration Acceleration of this player
     * @param speed Speed of this player
     * @param random This player's random object
     */
    PlayerThread(int acceleration, int speed, Random random) {
        this.acceleration = acceleration;
        this.speed = speed;
        this.random = random;
        this.last_race = 0;
    }


    /**
     * Last race getter
     * @return Results of this player's last race
     */
    public int getLast_race() { return this.last_race; }

    /**
     * Runs the race for this thread
     */
    @Override
    public void run() {
        int i = 1;
        this.last_race = 0;
        while(i <= 5) {

            if(i == 1) {

                // First leg is all acceleration
                // minimum is 2 if 20 acceleration
                // minimum is 1 if 10> acceleration
                // minimum is 0 otherwise
                this.last_race += this.random.nextInt(this.acceleration) + (this.acceleration / 10);
            } else if (i == 2) {

                this.last_race += (this.random.nextInt(this.acceleration) + (this.acceleration / 10)) / 2 ;
                this.last_race += (this.random.nextInt(this.speed) + (this.speed / 10)) / 2;
            } else {

                // Legs 3,4,5 are all speed based
                this.last_race += this.random.nextInt(this.speed) + (this.speed / 10);
            }

            // Sleep one 50th of a second
            try {
                sleep(50);
            } catch (InterruptedException e) {
                // shouldn't happen
            }

            i++;
        }
    }
}
