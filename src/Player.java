import java.util.Random;

/**
 * Emulates players within the program
 */
public class Player extends Thread {

    private Integer speed;
    private Integer acceleration;
    private Integer age;
    private Integer last_race;
    private Integer overall;
    private Integer games_sat;
    private Random random;
    private String name;
    private Contract contract;
    private boolean inLineUp;

    /**
     * Player constructor. Randomly generates acceleration
     * speed and age and then returns this racer.
     * @param name This player's name
     * @param rookie Declares if this racer is a rookie
     */
    Player (String name, boolean rookie) {

        // Create the random object this racer will use
        this.random = new Random();

        // Create the speed, acceleration, and age of this racer
        this.speed = random.nextInt(9) + 1;
        this.acceleration = random.nextInt(9) + 1;

        if(rookie)
            this.age = random.nextInt(4) + 16;
        else
            this.age = random.nextInt(13) + 20;

        this.overall = (this.speed + this.acceleration) / 2;
        this.name = name;

        // $600,000   minimum
        // $4,600,000 maximum

        float bound = ((float)this.acceleration + (float)this.speed) / 10;
        int bound_int = Math.round(bound * 1000000);

        int length = random.nextInt(5) + 1;
        int salary = random.nextInt(bound_int) + 600000;

        this.contract = new Contract(salary, length);
        this.games_sat = 0;
    }


    /**
     * Sets if this player is in the lineup or not
     * @param inLineUp Determines if player is in the lineup or not
     */
    public void setInLineUp(boolean inLineUp){
        this.inLineUp = inLineUp;
    }


    /**
     * Gets the overall of this player
     * @return Overall of the player
     */
    public Integer getOverall(){ return this.overall; }


    /**
     * Sets the length of the contract
     * @param length New length of the contract
     */
    public void setContractLength(Integer length){
        this.contract.setLength(length);
    }


    /**
     * Checks if this player's contract expired
     * @return true if the contract length is now 0
     *         false otherwise
     */
    public boolean contractExpired(){
        return this.contract.getLength() == 0;
    }


    /**
     * Sets this player's contract's salary to salary
     * @param salary New salary
     */
    public void setContractSalary(Integer salary){
        this.contract.setSalary(salary);
    }


    /**
     * Speed getter
     * @return  speed of the racer
     */
    public Integer getSpeed() {
        return this.speed;
    }


    /**
     * Name getter
     * @return  Name of this racer
     */
    public String getPlayerName() {
        return this.name;
    }


    /**
     * Acceleration getter
     * @return  Acceleration of the racer
     */
    public Integer getAcceleration() {
        return this.acceleration;
    }


    /**
     * Returns the last race this.race_this of this racer
     * @return Last race this.race_this for this racer
     */
    public Integer getLast_race() {
        return this.last_race;
    }


    /**
     * Age getter
     * @return  Returns age of the racer
     */
    public Integer getAge() {
        return this.age;
    }


    /**
     * After a racer wins the race their stats will grow
     */
    public void growSpeed() {

        if(this.speed == 20)
            return;

        this.speed += random.nextInt(2);

        while(this.speed > 20)
            this.speed--;
    }


    /**
     * After a racer wins the race their stats will grow
     */
    public void growAcceleration() {

        if(this.acceleration == 20)
            return;

        this.acceleration += random.nextInt(2);

        while(this.acceleration > 20)
            this.acceleration--;
    }


    /**
     * Sets the games sat back to 0
     */
    public void resetGamesSat(){ this.games_sat = 0; }


    /**
     * Increments games sat by 1
     */
    public void incrementGamesSat(){ this.games_sat++; }


    /**
     * Games sat getter
     * @return
     */
    public Integer getGamesSat(){ return this.games_sat; }


    /**
     * After age of 33 players will regress
     */
    public void regress(){

        if(this.age > 32){

            this.acceleration -= this.random.nextInt(2);
            this.speed -= this.random.nextInt(2);

            if(this.acceleration < 1)
                this.acceleration = 1;
            if(this.speed < 1)
                this.speed = 1;
        }
    }


    /**
     * Ages this player by one
     */
    public void get_older(){
        this.age++;
    }


    /**
     * Attempts to see if this racer retires
     * @return Returns true if the racer retires, false otherwise
     */
    public boolean did_retire() {

        if(this.age == 45)
            return true;

        if(this.age > 34) {
            if(random.nextInt(2) == 1)
                return true;
            else
                return false;
        }

        return false;
    }


    /**
     * Player toString
     * @return Player's string
     */
    public String toString(){

        String racer_str = "";

        if(this.inLineUp)
            racer_str += "Name: " + this.name;
        else
            racer_str += "*Name: " + this.name;

        racer_str += "\t\tSpeed: " + this.speed;
        racer_str += "\tAcceleration: " + this.acceleration;
        racer_str += "\tAge: " + this.age;
        racer_str += "\tSalary: " + getSalaryString();

        return racer_str;
    }

    /**
     * Player draft toString which blocks out their speed and acceleration
     * @return Player's draft string
     */
    public String toStringRookie(){

        String racer_str = "Name: " + this.name;

        racer_str += "\t\tSpeed: X";
        racer_str += "\tAcceleration: X";
        racer_str += "\tAge: " + this.age;
        racer_str += "\tSalary: " + getSalaryString();

        return racer_str;
    }


    /**
     * Gets the salary in $X.XX format as opposed to
     * full integer
     * @return
     */
    public String getSalaryString(){

        int tens, hundreds, millions;

        tens = (this.contract.getSalary() / 10000) % 10;
        hundreds = (this.contract.getSalary() / 100000) % 10;
        millions = (this.contract.getSalary() / 1000000) % 10;

        return "$" + millions + "." + hundreds + tens + "M/" + this.contract.getLength() + " yrs";
    }


    /**
     * Salary getter
     * @return this player's salary
     */
    public Integer getSalary(){
        return this.contract.getSalary();
    }


    /**
     * Gets this player's contract
     * @return this player's contract
     */
    public Contract getContract(){ return this.contract; }


    /**
     * Gives this player a new contract
     */
    public void negotiateContract(){

        // Set a bound based on speed and acceleration
        float bound = ((float)this.acceleration + (float)this.speed) / 10;
        int bound_int = Math.round(bound * 1000000);

        // Generate random length and salary
        int length = random.nextInt(5) + 1;
        int salary = random.nextInt(bound_int) + 600000;

        this.contract = new Contract(salary, length);
    }


    @Override
    /**
     * Runs the race for this thread
     */
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
