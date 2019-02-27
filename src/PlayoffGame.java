public class PlayoffGame {

    private Team home_team;
    private Team away_team;

    /**
     * Game Constructor
     * @param home_team Home team for the game
     * @param away_team Away team for the game
     */
    PlayoffGame (Team home_team, Team away_team) {

        this.home_team = home_team;
        this.away_team = away_team;
    }

    /**
     * Plays a game between two teams
     *
     * Games are a 6 race match between the players on each team
     * whichever team wins more races wins 3 each ends in a tie
     * @param is_overtime If this match is overtime don't present welcome string
     */
    public Team playGame(boolean is_overtime){


        if(!is_overtime){
            // Greet user with the teams and their records
            String greeting = String.format("Welcome to today's matchup:\n" +
                            "Home: %s %s [%d] (%d-%d-%d)\n" +
                            "Away: %s %s [%d] (%d-%d-%d)",
                    this.home_team.getTeam_location(), this.home_team.getTeam_name(), this.home_team.getRank(),
                    this.home_team.getWins(), this.home_team.getLosses(), this.home_team.getTies(),
                    this.away_team.getTeam_location(), this.away_team.getTeam_name(), this.away_team.getRank(),
                    this.away_team.getWins(), this.away_team.getLosses(), this.away_team.getTies());

            System.out.println(greeting);
        }

        int home_wins = 0, away_wins = 0;

        // Iterate over each player on both teams lineup
        // have them race
        for (int i = 0; i < this.home_team.getLineup().size(); i++) {

            // Get the two racers
            Player home_player = this.home_team.getLineup().get(i);
            Player away_player = this.away_team.getLineup().get(i);

            // Run the race
            // Create the necessary player threads
            PlayerThread home = home_player.getPlayerThread();
            PlayerThread away = away_player.getPlayerThread();

            // start the threads
            home.start();
            away.start();

            try {

                // join the threads
                home.join();
                away.join();
            } catch (InterruptedException e) {
                // shouldn't happen
            }

            // Save the result of the race
            home_player.setLast_race(home.getLast_race());
            away_player.setLast_race(away.getLast_race());

            if (home_player.getLast_race() > away_player.getLast_race()) {

                // Home player wins race and their stats grow
                home_wins++;
                System.out.println(String.format("%s won race %d. Score is %d-%d",
                        this.home_team.getTeam_name(), i, home_wins, away_wins));
                home_player.growAcceleration();
                home_player.growSpeed();
            } else {

                // Away player wins race and their stats grow
                away_wins++;
                System.out.println(String.format("%s won race %d. Score is %d-%d",
                        this.away_team.getTeam_name(), i, home_wins, away_wins));
                away_player.growSpeed();
                away_player.growAcceleration();
            }

            home_player.regress();
            away_player.regress();

        }

        // If the home team wins the game change their stats
        if (home_wins > away_wins) {
            String message = String.format("%s %s won the game %d-%d\n",
                    this.home_team.getTeam_location(), this.home_team.getTeam_name(), home_wins, away_wins);
            System.out.println(message);
            return this.home_team;

        } else if (away_wins > home_wins) {

            // If the away team wins change the stats
            String message = String.format("%s %s won the game %d-%d\n",
                    this.away_team.getTeam_location(), this.away_team.getTeam_name(), away_wins, home_wins);
            System.out.println(message);
            return this.away_team;
        } else {

            // Game was a tie
            System.out.println("The game finished in a tie!!\n");
            System.out.println("The game is entering overtime!");

            playGame(true);
        }

        return null;
    }
}
