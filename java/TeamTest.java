import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

public class TeamTest {
    public static void main(String[] args) throws Exception {

        Team spfc = new Team("Sao Paulo FC", "Hernan Crespo");
        BufferedReader saoPaulo = new BufferedReader(new FileReader("database/spfc.txt"));
        String line;
        while ((line = saoPaulo.readLine()) != null) {
            String[] data = line.split(",");
            //String name, String country, int age, String position, int number, int rating
            Player player = new Player(data[0], data[1], Integer.parseInt(data[2]), data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]));
            spfc.addPlayer(player);
        }
//        Map<Player, Integer> sortedRoster = new TreeMap<>(Player::compareTo);
//        for (Player player : spfc.getRoster()) {
//            sortedRoster.put(player, player.getRating());
//        }
//        System.out.println(sortedRoster);


        // sort by jersey number
        Map<Player, Integer> jerseyNum = new TreeMap<>((p1, p2) -> p1.getNumber() - p2.getNumber());
        for (Player player : spfc.getRoster()) {
            jerseyNum.put(player, player.getNumber());
        }
        System.out.println(jerseyNum);



    }
}
