import java.util.*;

/**
 * Emulates the teams involved
 */
public class Team extends Thread{

    private ArrayList<Player> roster;
    private ArrayList<Player> lineup;
    private String team_name;
    private String team_location;
    private Integer wins;
    private Integer losses;
    private Integer ties;
    private Names names;
    private Integer total_wins;
    private Integer total_losses;
    private Integer total_ties;
    private Integer cap_hit;
    private boolean users_team;
    private Integer championships;
    private ArrayList<Game> schedule;
    private FreeAgencyPool freeAgencyPool;
    private Integer rank;
    private TeamOffseasonThread thread;


    /**
     * Roster getter
     * @return this team's roster
     */
    public ArrayList<Player> getRoster() {
        return this.roster;
    }

    /**
     * Team name
     * @return this team's name
     */
    public String getTeam_name() {
        return this.team_name;
    }

    /**
     * Team location
     * @return Location of this team
     */
    public String getTeam_location() {
        return this.team_location;
    }

    /**
     * Wins getter
     * @return number of wins this team has in the current season
     */
    public Integer getWins() {
        return this.wins;
    }

    /**
     * Losses getter
     * @return number of losses this team has in the current season
     */
    public Integer getLosses() {
        return this.losses;
    }

    /**
     * Ties getter
     * @return number of ties this team has in the current season
     */
    public Integer getTies() {
        return this.ties;
    }

    /**
     * Total wins getter
     * @return number of wins this team has over the life of the program
     */
    public Integer getTotal_wins() {
        return this.total_wins;
    }

    /**
     * Total losses getter
     * @return number of losses this team has over the life of the program
     */
    public Integer getTotal_losses() {
        return this.total_losses;
    }

    /**
     * Total ties getter
     * @return number of ties this team has over the life of the program
     */
    public Integer getTotal_ties() {
        return this.total_ties;
    }

    /**
     * Cap hit getter
     * @return This team's cap total
     */
    public Integer getCap_hit() {
        return this.cap_hit;
    }

    /**
     * Users team getter
     * @return true if this is the user's team
     *          false otherwise
     */
    public boolean isUsers_team() {
        return this.users_team;
    }

    /**
     * Championships getter
     * @return number of championships this team has won
     */
    public Integer getChampionships() { return this.championships; }

    /**
     * Rank getter
     * @return This team's rank
     */
    public Integer getRank(){ return this.rank; }


    /**
     * Users team setter
     * @param users_team Whether or not this is the user's team
     */
    public void setUsers_team(boolean users_team) {
        this.users_team = users_team;
    }

    /**
     * Wins setter
     * @param wins number of wins to set to
     */
    public void setWins(Integer wins) {
        this.wins = wins;
    }

    /**
     * Sets this team's rank
     * @param rank rank to set
     */
    public void setRank(Integer rank){
        this.rank = rank;
    }

    /**
     * Losses setter
     * @param losses number of losses to set to
     */
    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    /**
     * Ties setter
     * @param ties number of ties to set to
     */
    public void setTies(Integer ties) {
        this.ties = ties;
    }

    /**
     * Total wins setter
     * @param total_wins amount of wins to update total wins by
     */
    public void setTotal_wins(Integer total_wins) {
        this.total_wins = total_wins;
    }

    /**
     * Total losses setter
     * @param total_losses amount of losses to update total losses by
     */
    public void setTotal_losses(Integer total_losses) {
        this.total_losses = total_losses;
    }

    /**
     * Total ties setter
     * @param total_ties amount of ties to update total ties by
     */
    public void setTotal_ties(Integer total_ties) {
        this.total_ties = total_ties;
    }

    /**
     * Championships setter
     * @param championships amount of championships to update total by
     */
    public void setChampionships(Integer championships) {
        this.championships = championships;
    }

    /**
     * Team constructor
     * @param location Team location i.e. Philadelphia, New York, etc.
     * @param name Team name
     * @param names names object
     *
     */
    Team(String location, String name, Names names) {

        this.team_name = name;
        this.team_location = location;
        this.roster = new ArrayList<>();
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.total_wins = 0;
        this.total_losses = 0;
        this.total_ties = 0;
        this.cap_hit = 0;
        this.names = names;
        this.users_team = false;
        this.championships = 0;
        this.schedule = new ArrayList<>();
        this.lineup = new ArrayList<>();
        this.thread = null;

        while(roster.size() < 6){

            Player p = new Player(names.generateName(), false);
            roster.add(p);
            lineup.add(p);
            p.setInLineUp(true);
            this.cap_hit += p.getSalary();
        }

        // Add 2 extra players to the roster that are not playing
        for(int i = 0; i < 2; i++){
            Player p = new Player(names.generateName(), false);
            roster.add(p);
            this.cap_hit += p.getSalary();
            p.setInLineUp(false);
        }
    }


    /**
     * Sets this team's free agency pool
     * @param freeAgencyPool Free agency pool object to use as the free agency pool
     */
    public void setFreeAgencyPool(FreeAgencyPool freeAgencyPool){
        this.freeAgencyPool = freeAgencyPool;
    }


    /**
     * Generates a cap string in the $X.XXM format
     * @return Cap string
     */
    private String getCapString(){
        int tens, hundreds, millions;

        tens = (this.cap_hit / 10000) % 10;
        hundreds = (this.cap_hit / 100000) % 10;
        millions = (this.cap_hit / 1000000) % 10;

        return "$" + millions + "." + hundreds + tens + "M";
    }


    /**
     * Sets the schedule to empty for the new year
     */
    public void resetSchedule(){
        this.schedule = new ArrayList<>();
    }


    /**
     * Gets the lineup
     * @return the lineup
     */
    public ArrayList<Player> getLineup(){ return this.lineup; }


    /**
     * Prints the lineup of the team
     */
    public void print_lineup(){

        // Prints the Team name and record
        System.out.println();
        String team_banner = String.format("\n*******%s %s (%d-%d-%d)*******",
                this.team_location, this.team_name, this.wins, this.losses, this.ties);
        System.out.println(team_banner);

        // Prints the team's roster
        for(Player player : this.lineup){
            System.out.println(player.toString());
        }

        System.out.println("\n\n");
    }


    /**
     * Sets the cap hit for the team
     * @param cap_hit New cap hit for the team
     */
    public void setCap_hit(int cap_hit) { this.cap_hit = cap_hit; }


    /**
     * Adds a game to the schedule
     * @param game Game to add to the schedule
     */
    public void addGameToSchedule(Game game){
        this.schedule.add(game);
    }


    /**
     * Free agency getter
     * @return Free Agency pool for this team
     */
    public FreeAgencyPool getFreeAgentPool() { return this.freeAgencyPool; }


    /**
     * Schedule getter
     * @return Schedule for this team
     */
    public ArrayList<Game> getSchedule() {
        return schedule;
    }

    /**
     * Gets the worst player on the team
     * @return Worst player on the team
     */
    public Player getLowestOverall(){

        // Start with the first player as the worst player
        Player lowest = this.roster.get(0);
        for(Player p : this.roster){

            // if this player's overall is the worst set the lowest
            // to this player
            if(p.getOverall() < lowest.getOverall())
                lowest = p;
        }

        return lowest;
    }


    /**
     * Replaces a player on the team with a new one
     * @param newPlayer Player to add
     * @param oldPlayer Player to remove
     */
    public void replace_player(Player newPlayer, Player oldPlayer){

        // Puts the new player in the slot of the old player
        this.roster.add(this.roster.indexOf(oldPlayer), newPlayer);

        // Java ArrayLists just put the old item at the end of the list
        // remove the old player
        this.roster.remove(oldPlayer);

        System.out.println("You must update your lineup");
        setLineup();

        this.updateCap();
    }


    /**
     * Updates the team's cap hit
     */
    public void updateCap(){
        this.cap_hit = 0;
        for(Player p : this.roster){
            cap_hit += p.getSalary();
        }
    }


    /**
     * Prints this team
     */
    public void print_team(){

        System.out.println();
        // Prints the Team name and record
        String team_banner = String.format("\n*******%s %s (%d-%d-%d)*******",
                this.team_location, this.team_name, this.wins, this.losses, this.ties);
        System.out.println(team_banner);

        // Prints the team's roster
        for(Player player : this.roster){
            System.out.println(player.toString());
        }

        System.out.println("Cap hit: " + getCapString());

        System.out.println("\n\n");
    }


    /**
     * Sets the lineup for this team
     */
    public void setLineup(){

        if(this.users_team) {

            // let user decide
            Scanner s = new Scanner(System.in);

            do {
                System.out.println();
                // List the whole team
                for(int i = 0; i < this.roster.size(); i++){

                    System.out.println(String.format("[%d] %s", i, this.roster.get(i).toString()));
                }

                // Ask who to move and where they will go
                System.out.println(String.format("The top %d players will be in the lineup.", this.lineup.size() - 1));

                // until the player to move choice is in the team's roster keep asking
                int to_move;
                do {
                    System.out.print("Player to move: ");
                    to_move = s.nextInt();
                } while (to_move >= this.roster.size() || to_move < 0);

                // until their new place is in the team's roster keep asking
                int destination;
                do {
                    System.out.print("Destination: ");
                    destination = s.nextInt();
                } while (destination >= this.roster.size() || destination < 0);

                // Swap the players
                Player to_move_p = this.roster.get(to_move);
                this.roster.set(to_move, this.roster.get(destination));
                this.roster.set(destination, to_move_p);

                System.out.print("Would you like to continue editing the lineup? [y/n]: ");
            } while(!s.next().toLowerCase().equals("n"));

            // Save the lineup
            for(int i = 0; i < this.roster.size(); i++){

                // Until the lineup is full just add in top overall players
                if(i < this.lineup.size()) {
                    this.lineup.set(i, this.roster.get(i));
                    this.roster.get(i).setInLineUp(true);
                } else
                    this.roster.get(i).setInLineUp(false);

            }

            this.print_lineup();

        } else {

            // Computer will set their lineup
            // sort the players by overall
            Collections.sort(this.roster, new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return o2.getOverall().compareTo(o1.getOverall());
                }
            });

            for(int i = 0; i < this.roster.size(); i++){

                // Until the lineup is full just add in top overall players
                if(i < this.lineup.size()) {
                    this.lineup.set(i, this.roster.get(i));
                    this.roster.get(i).setInLineUp(true);
                } else
                    this.roster.get(i).setInLineUp(false);

            }
        }
    }


    /**
     * Gets the offseason thread for this team
     * @return thread running the offseason for this team
     */
    public TeamOffseasonThread getThread() { return this.getThread(); }


    /**
     * Runs the offseason for this team
     */
    public void runOffseason() {

        TeamOffseasonThread teamOffseasonThread = new TeamOffseasonThread(this);
        this.thread = teamOffseasonThread;
        teamOffseasonThread.start();
    }
}
