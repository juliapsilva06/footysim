import java.util.*;

public class Team {
    protected String name;
    protected List<Player> roster;
    protected String coach;
    protected Graph<Player, String> rosterRelationship;

    public Team(String name, String coach){
        this.roster = new ArrayList<>();
        this.rosterRelationship = new AdjacencyMapGraph<>();
        this.coach = coach;
        this.name = name;

    }

    public List<Player> getRoster() {
        roster.sort(Comparator.comparingInt(p -> p.number));
        return roster;
        }

    public List<Player> getGoalkeepers() {
        ArrayList<Player> gks = new ArrayList<>();
        for (Player p : roster) {
            if (p.getPosition().equals("Goalkeeper")) {
                gks.add(p);
            }
        }
        gks.sort(Comparator.comparingInt(p -> p.rating));
        return gks;
    }

    public List<Player> getDefenders() {
        ArrayList<Player> def = new ArrayList<>();
        for (Player p : roster) {
            if (p.getPosition().equals("Defender")) {
                def.add(p);
            }
        }
        def.sort(Comparator.comparingInt(p -> p.rating));
        return def;
    }

    public List<Player> getMidfielders() {
        ArrayList<Player> mid = new ArrayList<>();
        for (Player p : roster) {
            if (p.getPosition().equals("Midfielder")) {
                mid.add(p);
            }
        }
        mid.sort(Comparator.comparingInt(p -> p.rating));
        return mid;
    }

    public List<Player> getForwards() {
        ArrayList<Player> fwd = new ArrayList<>();
        for (Player p : roster) {
            if (p.getPosition().equals("Forward")) {
                fwd.add(p);
            }
        }
        fwd.sort(Comparator.comparingInt(p -> p.rating));
        return fwd;
    }

    public String getCoach() {
        return coach;
    }

    public String getName() {
        return name;
    }

    public Graph<Player, String> getRosterRelationship() {
        return rosterRelationship;
    }

    public void addPlayer(Player player) {
        roster.add(player);
        rosterRelationship.insertVertex(player);
    }

    public void removePlayer(Player player) {
        roster.remove(player);
        rosterRelationship.removeVertex(player);
    }

    public Player findPlayerByName(String name) {
        for (Player p : roster) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        System.out.println("Player not found");
        return null;



    }

    public Player findPlayerByNumber(int num) {
        for (Player p : roster) {
            if (p.getNumber() == num) {
                return p;
            }
        }
        System.out.println("Player " + num + " not found");
        return null;



    }

    public List<Player> getStartingXI(int def, int mid, int ata) throws Exception {
        if (roster.size() < 11)
            throw new Exception("Error: not enough players to form a starting XI");
        if ((def + mid + ata) != 10 || def > 5 || mid > 5 || ata > 5)
            throw new Exception("Invalid formation");

        List<Player> startingXI = new ArrayList<>();

        // goalkeeper
        List<Player> goalkeepers = getGoalkeepers();
        if (goalkeepers.isEmpty()) throw new Exception("No goalkeepers available");
        Player gk = goalkeepers.get(0);
        startingXI.add(gk);

        // defenders
        List<Player> defenders = getDefenders();
        defenders.sort((a, b) -> Integer.compare(b.getRating(), a.getRating()));
        startingXI.addAll(defenders.subList(0, Math.min(def, defenders.size())));

        // midfielders
        List<Player> midfielders = getMidfielders();
        midfielders.sort((a, b) -> Integer.compare(b.getRating(), a.getRating()));
        startingXI.addAll(midfielders.subList(0, Math.min(mid, midfielders.size())));

        // forwards
        List<Player> forwards = getForwards();
        forwards.sort((a, b) -> Integer.compare(b.getRating(), a.getRating()));
        startingXI.addAll(forwards.subList(0, Math.min(ata, forwards.size())));

        return startingXI;
    }


    public void setRelationship(Player p1, Player p2, String rel) throws Exception {
        if (!roster.contains(p1) || !roster.contains(p2)) throw new Exception("Error: one or both players are not in the roster");

        rosterRelationship.insertUndirected(p1, p2, rel);

    }

    public void getRelationship(Player p1, Player p2) {
        System.out.println(p1.getName() + " is " + p2.getName() + "'s " + rosterRelationship.getLabel(p1, p2) + ".");
    }



    public Player getRandomPlayer() {
        Random rand = new Random();
        Player randomPlayer = roster.get(rand.nextInt(roster.size()));
        return randomPlayer;
    }

    public int poissonGenerator(double lambda) { // helper for gameSim
        Random random = new Random();

        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        while (p > L) {
            k++;
            p *= random.nextDouble();
        }

        return k - 1;
        }


    public void gameSim(Team opp, List<Player> teamLineup, List<Player> oppLineup) throws Exception {
        if (teamLineup.size() != 11 || oppLineup.size() != 11)
            throw new Exception("Error: starting XI should have 11 players");

        final int count = 11, sigmoidScale = 5, goalScale = 2;
        final double baseGoals = 1.5; // average goals in a football match
        int ratingSum = 0, ratingOppSum = 0, teamRating, oppRating, strengthDiff;
        double sigmoid, exponent, teamProb, oppProb, positionWeight = 1;

        // find ratings
        for (Player player : teamLineup) {
            if (player.getPosition().equals("Goalkeeper")) {
                positionWeight = 0.8;
            }
            if (player.getPosition().equals("Defender")) {
                positionWeight = 1.1;
            }
            if (player.getPosition().equals("Midfielder")) {
                positionWeight = 1.5;
            }
            if (player.getPosition().equals("Forward")) {
                positionWeight = 1.4;
            }

            ratingSum += Math.round(player.getRating() * positionWeight);

        }
        teamRating = ratingSum / count;

        for (Player player : oppLineup) {
            if (player.getPosition().equals("Goalkeeper")) {
                positionWeight = 0.8;
            }
            if (player.getPosition().equals("Defender")) {
                positionWeight = 1.1;
            }
            if (player.getPosition().equals("Midfielder")) {
                positionWeight = 1.5;
            }
            if (player.getPosition().equals("Forward")) {
                positionWeight = 1.4;
            }

            ratingOppSum += Math.round(player.getRating() * positionWeight);
        }
        oppRating = ratingOppSum / count;

        // compute expected win probability (Helper: Sigmoid function 1/1+e^-diff/scale)
        strengthDiff = teamRating - oppRating;
        exponent = Double.valueOf(-strengthDiff) / sigmoidScale;
        sigmoid = 1 / (1 + Math.pow(Math.E, exponent));

        // compute expected goals per team
        if (strengthDiff > 0) { // team most likely to win
            teamProb = baseGoals + (sigmoid - 0.5) * goalScale;
            oppProb = baseGoals - (sigmoid - 0.5) * goalScale;
        } else if (strengthDiff < 0) { // opp most likely to win
            teamProb = baseGoals - (sigmoid - 0.5) * goalScale;
            oppProb = baseGoals + (sigmoid - 0.5) * goalScale;
        } else { // tie most likely
            teamProb = oppProb = baseGoals - (sigmoid - 0.5) * goalScale;
        }

        // compute actual match goals using Poisson distribution
        int teamGoals = poissonGenerator(teamProb), oppGoals = poissonGenerator(oppProb);

        // match display
        System.out.println("Match has started!");
        Random random = new Random();

        // EVENTS
        Map<Integer, Map<Player, String>> events = new HashMap<>(); // maps a minute to a player and the corresponding event

        // goals
        int [] teamGoalTimes = new int[teamGoals];
        if (teamGoals > 0) {
            for (int i = 0; i < teamGoals;) {
                int minute = random.nextInt(91); // random minute between 0–90
                if (!events.containsKey(minute)) {
                    Map<Player, String> currGoal = new HashMap<>();
                    currGoal.put(teamLineup.get(random.nextInt(11)), "GOAL! ");
                    events.put(minute, currGoal);
                    teamGoalTimes[i] = minute;
                    i++;
                }
            }
        }
        Arrays.sort(teamGoalTimes);

        int [] oppGoalTimes = new int[oppGoals];
        if (oppGoals > 0) {
            for (int i = 0; i < oppGoals;) {
                int minute = random.nextInt(91); // random minute between 0–90
                if (!events.containsKey(minute)) {
                    Map<Player, String> currGoal = new HashMap<>();
                    currGoal.put(oppLineup.get(random.nextInt(11)), "GOAL! ");
                    events.put(minute, currGoal);
                    oppGoalTimes[i] = minute;
                    i++;
                }
            }
        }
        Arrays.sort(oppGoalTimes);

        // yellow and red cards
        int avgYellows = 5, yellowsThisMatch = avgYellows + random.nextInt(5), teamYellows = yellowsThisMatch - random.nextInt(5), oppYellows = yellowsThisMatch - teamYellows;

        List<Player> yellowed = new ArrayList<>(); // keeps count of yellow players so reds can be awarded

        for (int i = 0; i < teamYellows; i++) {
            int minute = random.nextInt(91); // random minute between 0–90
            if (!events.containsKey(minute)) {
                Map<Player, String> currFoul = new HashMap<>();
                Player currYellow = teamLineup.get(random.nextInt(11));
                if (yellowed.contains(currYellow)) {
                    currFoul.put(currYellow, "Red card to ");
                    events.put(minute, currFoul);
                    teamLineup.remove(currYellow);
                } else {
                    currFoul.put(currYellow, "Yellow card to ");
                    events.put(minute, currFoul);
                    yellowed.add(currYellow);

                }
            }
        }

        for (int i = 0; i < oppYellows; i++) {
            int minute = random.nextInt(91); // random minute between 0–90
            if (!events.containsKey(minute)) {
                Map<Player, String> currFoul = new HashMap<>();
                Player currYellow = opp.getRandomPlayer();
                if (yellowed.contains(currYellow)) {
                    currFoul.put(currYellow, "Red card to ");
                    events.put(minute, currFoul);
                } else {
                    currFoul.put(currYellow, "Yellow card to ");
                    events.put(minute, currFoul);
                    yellowed.add(currYellow);

                }
            }
        }


        // match display
        for (int i = 0; i < 91; i++) {
            Thread.sleep(500);
            if (events.containsKey(i)) {
                Map<Player, String> innerMap = events.get(i);

                for (Map.Entry<Player, String> entry : innerMap.entrySet()) {
                    Player player = entry.getKey();
                    String event = entry.getValue();
                    System.out.println(event + player.getName() + " at " + i + "'");
                }
            } else {
                System.out.println(i + "'");
            }
        }

        System.out.println("END OF MATCH!");
        Thread.sleep(1000);
        if (teamGoals > oppGoals) {
        System.out.println(this.getName() + " " + teamGoals + " x " + oppGoals + " " + opp.getName());
        System.out.println(this.getName() + " has won the match!");
        } else if (teamGoals < oppGoals) {
            System.out.println(opp.getName() + " " + oppGoals + " x " + teamGoals + " " + this.getName());
            System.out.println(opp.getName() + " has won the match!");
        } else {
            System.out.println(this.getName() + " " + teamGoals + " x " + oppGoals + " " + opp.getName());
            System.out.println("It's a tie!");
        }









    }






}
