import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Runs the drafting process for each team
 */
public class Draft {

    ArrayList<Player> draft;
    Names names;


    /**
     * Draft constructor
     * @param num_players Number of players to have in the draft
     */
    Draft(int num_players) {

        this.draft = new ArrayList<>();
        this.names = new Names();

        // Create the necessary number of rookies
        int i = 0;
        while(i++ < num_players)
            draft.add(new Player(names.generateName(), true));

        // sort the players by overall
        Collections.sort(this.draft, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.getOverall().compareTo(o1.getOverall());
            }
        });
    }

    /**
     * Runs the draft for the league
     * @param teams Teams in the draft
     * @return The last player that was not picked to join Free Agency
     */
    public Player runDraft(ArrayList<Team> teams){

        // sort the list of teams by least wins
        Collections.sort(teams, new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o1.getWins().compareTo(o2.getWins());
            }
        });

        System.out.println("********* Welcome to this Season's Draft *********");

        // Iterate over each team in the draft
        int pick_num = 1;
        for(Team team : teams) {

            Player rookie;
            // Handle user's team case
            if(team.isUsers_team()) {

                Scanner s = new Scanner(System.in);

                // Print out the rookies
                for(int i = 0; i < this.draft.size(); i++) {

                    System.out.println(String.format("[%d] %s", i, this.draft.get(i).toStringRookie()));
                }


                // Until their choice is within the draft pool keep asking
                int rookie_choice;
                do {
                    System.out.print("Please select draft choice: ");
                    rookie_choice = s.nextInt();
                } while (rookie_choice >= this.draft.size() || rookie_choice < 0);

                // Get the rookie to add
                rookie = this.draft.remove(rookie_choice);

                // Show user their own team to get their removal choice
                for(int i = 0; i < team.getRoster().size(); i++) {

                    System.out.println(String.format("[%d] %s", i, team.getRoster().get(i).toString()));
                }

                int old_choice;
                do {
                    System.out.print("Please select player to release: ");
                    old_choice = s.nextInt();
                } while (old_choice >= team.getRoster().size() || old_choice < 0);

                Player old_player = team.getRoster().get(old_choice);
                team.replace_player(rookie, old_player);

            } else {

                rookie = this.draft.remove(0);
                team.replace_player(rookie, team.getLowestOverall());
            }

            System.out.println(String.format("With the %d pick of the draft the %s %s have selected %s",
                    pick_num++, team.getTeam_location(), team.getTeam_name(), rookie.toString()));
        }

        // Return the last rookie to add to the free agency pool
        return this.draft.get(0);
    }
}
