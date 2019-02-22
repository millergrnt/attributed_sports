import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    /**
     * Generates a season for a team
     * @return The list of games
     */
    public static ArrayList<Game> generateSeason(Team team, ArrayList<Team> teams) {

        ArrayList<Game> season = new ArrayList<>();
        for(int i = 0; i < 8; i++){

            // Move i into the arraylist
            int location = i % teams.size();

            // Can't play yourself
            if(!teams.get(location).getTeam_name().equals(team.getTeam_name())) {

                // Alternate between home and away
                if(i % 2 == 0)
                    season.add(new Game(teams.get(location), team));
                else
                    season.add(new Game(team, teams.get(location)));
            }
        }

        return season;
    }


    public static void main(String[] args) {

        // Create the names generator
        Names names = new Names();

        // Create the teams
        Team giants = new Team("New York", "Big Guys", names);
        Team cowboys = new Team("Dallas", "Cowgirls", names);
        Team redskins = new Team("Washington", "Native Americans", names);

        // Add the teams to the team list
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(giants);
        teams.add(cowboys);
        teams.add(redskins);

        String user_team_location;
        String user_team_name;
        Scanner scanner = new Scanner(System.in);

        if(args.length < 2){

            // No name and location provided on command line, ask user for information
            System.out.print("Please enter team location: ");
            user_team_location = scanner.next();

            System.out.print("Please enter team name: ");
            user_team_name = scanner.next();
        } else {

            // If the program is started with a team name and location use that
            user_team_location = args[0];
            user_team_name = args[1];
        }

        // Create the user's team and add it to the list of teams
        Team user_team = new Team(user_team_location, user_team_name, names);
        user_team.setUsers_team(true);
        teams.add(user_team);

        // Create the Free Agents pool and the league
        FreeAgencyPool freeAgencyPool = new FreeAgencyPool(teams.size());
        League nfl = new League(teams, 14000000, "National Racing League", user_team, freeAgencyPool);

        // Give each team the free agency pool
        for(Team team : teams){
            team.setFreeAgencyPool(freeAgencyPool);
        }

        // Generate a season and then alert the user of their team
        nfl.generateSeasonSchedule();
        user_team.print_team();

        // Play the game
        nfl.playSeason();

        // Tell the user about their career totals
        System.out.println(String.format("%s %s finished: (%d-%d-%d)",
                user_team.getTeam_location(), user_team.getTeam_name(), user_team.getTotal_wins(),
                user_team.getTotal_losses(), user_team.getTotal_ties()));
        System.out.println(String.format("%s %s won %d championships",
                user_team.getTeam_location(), user_team.getTeam_name(), user_team.getChampionships()));

    }
}
