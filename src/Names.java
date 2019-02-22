import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random name generator for the program
 */
public class Names {

    private ArrayList<String> names;
    private Random random;

    /**
     * Names constructor
     */
    Names () {

        random = new Random();

        // Generate names
        ArrayList<String> names = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/names.txt"))) {
            String line;

            while((line = br.readLine()) != null) {
                names.add(line);
            }
        } catch (IOException e) {

            System.err.println("Error while reading file...");
            e.printStackTrace();
            System.exit(1);
        }

        this.names = names;
    }


    /**
     *
     * @return
     */
    public String generateName(){

        // List of first name initials
        String first_inits_str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // Get a random first name character
        String name = "" + first_inits_str.charAt(random.nextInt(26));

        // Get the last name from the array
        String last_name = names.get(random.nextInt(names.size() - 1));

        // pullout the first character
        String last_name_first_char = last_name.substring(0,1);

        // put first character back in with an upper case
        last_name = last_name_first_char.toUpperCase() + last_name.substring(1);

        // Finally create racer with the name
        name += "." + last_name;

        return name;
    }
}
