import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Simulates the Free Agent pool for the league
 */
public class FreeAgencyPool {

    private ArrayList<Player> freeAgencyPool;
    private Names names;

    /**
     * Free agency constructor
     * @param num_teams Number of teams in the league
     */
    FreeAgencyPool(int num_teams){
        this.names = new Names();
        this.freeAgencyPool = new ArrayList<>();

        int i = 0;
        while(i++ < (num_teams * 3)){

            this.freeAgencyPool.add(new Player(names.generateName(), false));
        }
    }


    /**
     * Gets the free agent pool
     * @return Free agent pool
     */
    public ArrayList<Player> getFreeAgencyPool() {
        return freeAgencyPool;
    }

    /**
     * Offers the free agents to the player and then replaces the chosen player
     * with the player that was just released
     * @param is_users_team Used to determine if to ask user to choose or automatically choose
     * @param old_player Player that will become a free agent
     * @param selection Selection of a non computer team
     * @return The player selected
     */
    public synchronized Player getFreeAgent(boolean is_users_team, Player old_player, int selection){

        Player p;

        // sort the list of players by cost they want
        Collections.sort(this.freeAgencyPool, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.getSalary().compareTo(o1.getSalary());
            }
        });

        if(is_users_team){

            int i = 0;
            Scanner s = new Scanner(System.in);
            for(Player player : this.freeAgencyPool){

                System.out.println(String.format("[%d] %s", i++, player.toStringRookie()));
            }

            System.out.print("Please select Free Agent to sign: ");
            p = this.freeAgencyPool.remove(s.nextInt());

            if(old_player != null)
                this.freeAgencyPool.add(old_player);
            else
                this.freeAgencyPool.add(new Player(this.names.generateName(), false));

            return p;
        } else {

            p = this.freeAgencyPool.remove(selection);

            if(old_player != null)
                this.freeAgencyPool.add(old_player);
            else
                this.freeAgencyPool.add(new Player(this.names.generateName(), false));

            return p;
        }
    }


    /**
     * Runs the off-season for all the Free Agents
     */
    public void offseason(){

        // Make all the players get older and regress for not playing
        for(int i = 0; i < this.freeAgencyPool.size(); i++){
            Player p = this.freeAgencyPool.get(i);
            p.get_older();
            p.regress();

            // give them a new contract to give them a chance to play
            p.negotiateContract();
            if(p.did_retire()){
                // If the player retired replace it with a new free agent
                this.freeAgencyPool.set(i, new Player(this.names.generateName(), false));
            }
        }


    }

}
