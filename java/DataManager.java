import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    public static Map<String, String> database(String filePath) {
        Map<String, String> database = new HashMap<>(); // pairs the name with its filepath for easy database access
        try {
            BufferedReader databaseReader = new BufferedReader(new FileReader(filePath));
            String databaseLine;
            while ((databaseLine = databaseReader.readLine()) != null) {
                String[] data = databaseLine.split(","); // name,filepath
                database.put(data[0], data[1]);
            }
            return database;
        }
        catch(Exception e){
                System.err.println(e);
                return null;
            }
    }

    public static Team handleTeamChoice(String chosenTeam, String coachName) {
        try {
            Map<String, String> database = database("database/TEAMS.txt");
            Team team = new Team(chosenTeam, coachName);
            String filePath = "database/" + database.get(chosenTeam) + ".txt";
            BufferedReader teamChoice = new BufferedReader(new FileReader(filePath));

            String line1;
            while ((line1 = teamChoice.readLine()) != null) {
                String[] data = line1.split(",");
                //String name, String country, int age, String position, int number, int rating
                Player player = new Player(data[0], data[1], Integer.parseInt(data[2]), data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                team.addPlayer(player);
            }
            return team;

        }
        catch (Exception e) {
            System.out.print("Selected team does not exist! Maybe check for a typo?");
            return null;
        }



    }
}
