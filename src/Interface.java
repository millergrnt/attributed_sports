import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Interface {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Attributed Sports");
        frame.setMinimumSize(new Dimension(1600, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel myLabel = new JLabel("Philadelphia Eagles", SwingConstants.LEFT);
        myLabel.setFont(new Font("Serif", Font.BOLD, 36));
        myLabel.setPreferredSize(new Dimension(100, 80));

        ArrayList<Player> example_roster = new ArrayList<>();
        Object[][] data = new Object[4][];

        example_roster.add(new Player("G.Miller", false));
        example_roster.add(new Player("A.Taffe", false));
        example_roster.add(new Player("C.Louria", false));
        example_roster.add(new Player("C.Braverman", false));

        int i = 0;
        for(Player p : example_roster){
            Object[] player_data = {
                    p.getPlayerName(),
                    p.getSpeed(),
                    p.getAcceleration(),
                    p.getAge(),
                    p.getSalaryString()
            };

            data[i++] = player_data;
        }

        Object[] Column_Titles = {
                "Name",
                "Speed",
                "Acceleration",
                "Age",
                "Contract"
        };

        JTable roster = new JTable(data, Column_Titles);
        //roster.setBounds(0, 500, 300, frame.getHeight() - 200);

        JScrollPane sp = new JScrollPane(roster);
        sp.setBounds(0, 200, 300, frame.getHeight() - 200);

        frame.getContentPane().add(myLabel, BorderLayout.NORTH);
        frame.add(sp);

        frame.pack();
        frame.setVisible(true);
    }
}