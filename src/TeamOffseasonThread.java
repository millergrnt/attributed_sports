import java.util.Random;
import java.util.Scanner;

/**
 * Simulates the offseason for a given team
 */
public class TeamOffseasonThread extends Thread {

    // Instance variables
    private Team team;

    TeamOffseasonThread(Team team) {
        this.team = team;
    }


    /**
     * Simulates the offseason for this team, involves:
     *      Aging
     *      Retirement
     *      Salary Negotiation
     *      Getting new rookies
     */
    public void run(){

        if(this.team.isUsers_team()) {

            System.out.println();
            Scanner s = new Scanner(System.in);
            System.out.println("Checking if any players retired...");

            // reset cap
            this.team.setCap_hit(0);
            for(int i = 0; i < this.team.getRoster().size(); i++) {

                // Iterate over each player and age them
                Player player = this.team.getRoster().get(i);
                player.get_older();

                // if they sat enough games grow young players, regress older players
                if(player.getGamesSat() > (this.team.getSchedule().size() / 2)) {

                    if (player.getAge() < 24){
                        player.growAcceleration();
                        player.growSpeed();
                    } else {
                        player.regress();
                    }
                }

                // See if they retire
                if(player.did_retire()){

                    // Alert of a player retiring and then remove the player
                    System.out.println("[-] Retiring: " + player.toString());
                    this.team.getRoster().remove(player);

                    // Sign a new free agent
                    Player fa = this.team.getFreeAgentPool().getFreeAgent(
                            this.team.isUsers_team(), null, 0);
                    this.team.getRoster().add(i, fa);

                    // Add the new rookie's salary to the cap
                    this.team.setCap_hit(this.team.getCap_hit() + fa.getSalary());
                    System.out.println("[+] Free Agent Added: " + fa.toString());
                } else {

                    // Otherwise check contract situation
                    player.getContract().decrementContractLength();

                    // If their contract is up generate a new contract
                    if(player.getContract().getLength() <= 0){

                        // Ask if user wants to offer player a new contract
                        System.out.println(String.format("[+] %s would like a new contract", player.getPlayerName()));
                        System.out.println(player.toString());
                        System.out.print(String.format("[!] Would you like to re-sign %s: ", player.getPlayerName()));

                        // If they don't want to resign generate a new player that is NOT a rookie
                        if(s.next().equals("n")){

                            System.out.println("[+] Signing free agent...");
                            this.team.getRoster().remove(player);

                            Player fa = this.team.getFreeAgentPool().getFreeAgent(
                                    this.team.isUsers_team(), player, 0);

                            this.team.getRoster().add(i, fa);
                            System.out.println(String.format("[+] %s signed.\n", fa.getPlayerName()));

                        } else {

                            // Otherwise negotiate their new contract
                            player.negotiateContract();
                            System.out.println(String.format("[!] Resigned for %s\n", player.getSalaryString()));

                            // Set this player's previous speed and acceleration
                            player.setPrevAcceleration();
                            player.setPrevSpeed();
                        }
                    }

                    // Add this player's contract to cap hit
                    this.team.setCap_hit(this.team.getCap_hit() + player.getSalary());
                }
            }

            System.out.println();

        } else {

            // This is not the user's team so they we can't ask for user's input

            // reset cap
            this.team.setCap_hit(0);
            Random r = new Random();

            for(int i = 0; i < this.team.getRoster().size(); i++) {

                // Iterate over each player and age them
                Player player = this.team.getRoster().get(i);
                player.get_older();

                // Set this player's previous speed and acceleration
                player.setPrevAcceleration();
                player.setPrevSpeed();

                // See if they retire
                if(player.did_retire()){

                    // Alert of a player retiring and then remove the player
                    this.team.getRoster().remove(player);

                    // Generate a new rookie and add them to the team
                    Player fa = this.team.getFreeAgentPool().getFreeAgent(
                            this.team.isUsers_team(), null, 0);
                    this.team.getRoster().add(i, fa);

                    // Add the new rookie's salary to the cap
                    this.team.setCap_hit(this.team.getCap_hit() + fa.getSalary());
                } else {

                    // Otherwise check contract situation
                    player.getContract().decrementContractLength();

                    // If their contract is up generate a new contract
                    if(player.getContract().getLength() <= 0){

                        // If they don't want to resign generate a new player that is NOT a rookie
                        int overall = player.getOverall();
                        if(overall < 12){

                            // 13 is maximum random value
                            // if random value is less than the overall of the player
                            // then resign the player otherwise let them go
                            if(r.nextInt(14) < overall){
                                player.negotiateContract();
                            } else {

                                this.team.getFreeAgentPool().getFreeAgent(
                                        this.team.isUsers_team(), player, 0);
                            }
                        } else {

                            player.negotiateContract();
                        }
                    }

                    // Add this player's contract to cap hit
                    this.team.setCap_hit(this.team.getCap_hit() + player.getSalary());
                }
            }
        }
    }
}
