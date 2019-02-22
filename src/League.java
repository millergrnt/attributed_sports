import java.util.*;

/**
 * Emulatoes the league for the program
 */
public class League {

    private ArrayList<Team> teams;
    private ArrayList<ArrayList<Game>> seasonSchedule;
    private Integer salaryCap;
    private String leagueName;
    private Random r;
    private Team user_team;
    private FreeAgencyPool freeAgencyPool;

    /**
     * League constructor
     * @param teams List of teams in the league
     * @param salaryCap Salary cap for the league
     * @param leagueName Name for this league
     * @param user_team User's team
     */
    League (ArrayList<Team> teams, Integer salaryCap, String leagueName, Team user_team, FreeAgencyPool freeAgencyPool) {
        this.teams = teams;
        this.salaryCap = salaryCap;
        this.leagueName = leagueName;
        this.seasonSchedule = new ArrayList<>();
        this.user_team = user_team;
        this.freeAgencyPool = freeAgencyPool;
        r = new Random();
    }


    /**
     * Teams getter
     * @return teams
     */
    public ArrayList<Team> getTeams() {
        return teams;
    }


    /**
     * Salary Cap getter
     * @return salary cap
     */
    public Integer getSalaryCap() {
        return salaryCap;
    }


    /**
     * League name getter
     * @return League name
     */
    public String getLeagueName() {
        return leagueName;
    }


    /**
     * Prints the current standings
     */
    public void printStandings() {

        // sort the list of teams by wins
        Collections.sort(this.teams, new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o2.getWins().compareTo(o1.getWins());
            }
        });

        System.out.println("********* Standings *********");

        // Print each team's current record then exit
        for(Team team : this.teams){

            System.out.println(String.format("%s %s\t(%d-%d-%d)",
                    team.getTeam_location(), team.getTeam_name(), team.getWins(),
                    team.getLosses(), team.getTies()));
        }

        System.out.println();
    }


    /**
     * Function that will play the game it says Season but it is
     * really SeasonS
     */
    public void playSeason(){

        // Ask user if they want to continue
        Scanner s = new Scanner(System.in);
        int season = 1;

        do {
            for(Team team : teams){
                team.resetSchedule();
                for(Player p : team.getRoster())
                    p.resetGamesSat();
            }

            this.generateSeasonSchedule();
            this.enforceSalaryCap();

            // Reset the standings
            for(Team t : this.teams){
                t.setTotal_wins(t.getTotal_wins() + t.getWins());
                t.setWins(0);

                t.setTotal_losses(t.getTotal_losses() + t.getLosses());
                t.setLosses(0);

                t.setTotal_ties(t.getTotal_ties() + t.getTies());
                t.setTies(0);
            }

            // Play each week of the season
            int weeks = 1;
            for(ArrayList<Game> week : this.seasonSchedule){

                // Ask user what to do
                System.out.println("[C]ontinue [S]tandings [R]oster/[L]ineup <team name> [E]dit Lineup");
                System.out.print("What would you like to do: ");
                String choice;

                // Until user chooses to continue keep asking
                while(!(choice = s.nextLine()).toLowerCase().equals("c")){

                    String[] choice_list = choice.split(" ");

                    if(choice_list[0].toLowerCase().equals("s"))
                        this.printStandings();
                    else if(choice_list[0].toLowerCase().equals("r")) {

                        // if the user names the team whose roster
                        // they wish to view let them view that team
                        if(choice_list.length > 1){

                            for(Team team : teams){

                                // standardize them and check
                                if(team.getTeam_name().toLowerCase().equals(choice_list[1].toLowerCase()))
                                    team.print_team();
                            }
                        } else {
                            this.user_team.print_team();
                        }
                    } else if(choice_list[0].toLowerCase().equals("l"))

                        // if the user names the team whose lineup
                        // they wish to view let them view that team
                        if(choice_list.length > 1){

                            for(Team team : teams){

                                // standardize them and check
                                if(team.getTeam_name().toLowerCase().equals(choice_list[1].toLowerCase()))
                                    team.print_lineup();
                            }
                        } else {
                            this.user_team.print_lineup();
                        }
                    else if(choice_list[0].toLowerCase().equals("e"))
                        this.user_team.setLineup();
                    else
                        System.out.println("Unknown option chosen.");

                    System.out.println("[C]ontinue [S]tandings [R]oster/[L]ineup <team name> [E]dit Lineup");
                    System.out.print("What would you like to do: ");
                }

                System.out.println(String.format("\n%s Week %d", this.leagueName, weeks));

                for(Team team : this.teams){

                    // Set up non-user teams lineups
                    if(!team.isUsers_team())
                        team.setLineup();
                }

                // Start each game
                for(Game game : week){
                    game.run();
                }

                // Join the games to wait for them all to finish
                for(Game game : week){

                    try{
                        game.join();
                    } catch (InterruptedException e) {
                        // shouldn't happen
                    }
                }

                // increment week number
                weeks += 1;
            }

            System.out.println(String.format("Season %d is over!", season));
            System.out.println(String.format("%s %s finished (%d-%d-%d)",
                    this.user_team.getTeam_location(), this.user_team.getTeam_name(),
                    this.user_team.getWins(), this.user_team.getLosses(),
                    this.user_team.getTies()));

            // Season finished

            // sort the list of teams by wins
            Collections.sort(this.teams, new Comparator<Team>() {
                @Override
                public int compare(Team o1, Team o2) {
                    return o2.getWins().compareTo(o1.getWins());
                }
            });

            ArrayList<Team> playoffTeams = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                playoffTeams.add(this.teams.get(i));
            }

            runPlayoffs(playoffTeams);

            // Get the top team as the champion
            Team champion = this.teams.get(0);
            System.out.println(String.format("%s %s won the Championship Trophy!",
                    champion.getTeam_location(), champion.getTeam_name()));
            champion.setChampionships(champion.getChampionships() + 1);


            // run offseason
            for(Team team : this.teams){

                team.run();
            }

            // Join threads to wait
            for(Team team : this.teams){

                try {
                    team.join();
                } catch (InterruptedException e) {
                    // Shouldn't happen
                }
            }

            // Age all the free agents and make them regress
            // for not playing
            this.freeAgencyPool.offseason();

            // Create draft class and run a new draft
            Draft d = new Draft(this.teams.size() + 1);
            Player undrafted = d.runDraft(this.teams);

            // Add the undrafted player to the pool
            this.freeAgencyPool.getFreeAgencyPool().add(undrafted);

            // Alert user of their new team
            user_team.print_team();

            // Ask user to continue
            System.out.print("Would you like to continue? [y/n]: ");

            // increment season number
            season += 1;

        } while(!s.nextLine().toLowerCase().equals("n"));

        // user decided to exit:
        user_team.setTotal_wins(user_team.getTotal_wins() + user_team.getWins());
        user_team.setTotal_losses(user_team.getTotal_losses() + user_team.getLosses());
        user_team.setTotal_ties(user_team.getTotal_ties() + user_team.getTies());
    }


    /**
     * Generates the season schedule
     */
    public void generateSeasonSchedule(){

        this.seasonSchedule = new ArrayList<>();

        // 8 week schedule
        for(int i = 0; i < 8; i++){

            // Get a list of teams to play
            ArrayList<Team> teams_to_play = new ArrayList<>(this.teams);
            ArrayList<Game> this_week_games = new ArrayList<>();

            // Iterate over each team and generate their schedule
            while(teams_to_play.size() > 0){

                // Get the two teams from the array and remove them from the
                // teams to play
                Team home = teams_to_play.remove(r.nextInt(teams_to_play.size()));
                Team away = teams_to_play.remove(r.nextInt(teams_to_play.size()));

                Game game = new Game(home, away);

                this_week_games.add(game);
                home.addGameToSchedule(game);
                away.addGameToSchedule(game);
            }

            this.seasonSchedule.add(this_week_games);
        }
    }


    /**
     * Simulates the playoffs for the league
     * @return returns the team that won the championship
     */
    private Team runPlayoffs(ArrayList<Team> topFourTeams){

        System.out.println("\nThe playoffs have started!");

        int i = 0;
        int j = topFourTeams.size() - 1;
        ArrayList<Team> champions = new ArrayList<>();
        while(i < j){

            // Match up opposite side teams, i.e. 1 plays 4 and 2 plays 3
            Team home = topFourTeams.get(i++);
            home.setRank(i);
            Team away = topFourTeams.get(j--);
            away.setRank(i);

            PlayoffGame game = new PlayoffGame(home, away);
            champions.add(game.playGame(false));
        }

        PlayoffGame championship = new PlayoffGame(champions.get(0), champions.get(1));
        return championship.playGame(false);
    }


    /**
     * Process undertaken by the league to enforce the salary cap
     */
    private void enforceSalaryCap(){

        for(Team team : teams){

            while(team.getCap_hit() > this.salaryCap) {

                if(team.isUsers_team()){

                    // Alert user that their team is over the cap
                    System.out.println("***** Your team is over the cap *****");

                    // List the roster
                    int i = 0;
                    for(Player p : team.getRoster())
                        System.out.println(String.format("[%d] %s", i++, p.toString()));


                    // Get which player the user wants to remove
                    Scanner s = new Scanner(System.in);
                    int to_release;
                    do {
                        System.out.print("Which player would you like to release?: ");
                        to_release = s.nextInt();
                    } while (to_release >= team.getRoster().size() || to_release < 0);

                    // Get the player to remove
                    Player p = team.getRoster().get(to_release);
                    Player fa = this.freeAgencyPool.getFreeAgent(team.isUsers_team(), p, 0);

                    // Stop users from entirely redoing team by just constantly choosing
                    // too expensive players
                    while(fa.getSalary() > p.getSalary()){

                        System.out.println("That player will not fix your cap issue try again");
                        fa = this.freeAgencyPool.getFreeAgent(team.isUsers_team(), p, 0);
                    }

                    // replace the player to remove with a free agent
                    team.replace_player(fa, p);

                } else {

                    int i = 0;

                    // Get the lowest overall player and try to replace that player
                    Player dropped = team.getLowestOverall();
                    Player fa = this.freeAgencyPool.getFreeAgent(team.isUsers_team(), dropped, i++);

                    // while the salary of that player is too high
                    while(fa.getSalary() > dropped.getSalary()){
                        fa = this.freeAgencyPool.getFreeAgent(team.isUsers_team(), dropped, i++);
                    }

                    team.replace_player(fa, dropped);

                }

                team.updateCap();
            }
        }
    }
}
