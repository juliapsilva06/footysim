/*
JavaFootball class that brings Player and Team objects into a game

Current functionalities: pick coach name;
pick a team out of the teams in the file database;
build starting xi + bench for a specific match;
allow further changes to the team;

To be implemented: create a team and players from scratch using the teamBuilder method;
simulate a match between the player's team and another one in the database;
allow 1v1 player input;
career mode vs arcade mode;

Author: Julia Silva



 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        playGame(sc);
    }

    public static void playGame(Scanner sc) throws Exception {
        System.out.println("⚽ Welcome to Julia's Java Football™ Simulator \uD83C\uDFC6!");
        System.out.print("Please type in your name to start: ");
        String coachName = sc.nextLine();

        System.out.println("\nWelcome, coach " + coachName + "! ");
        Thread.sleep(2000);
        System.out.print("Pick the club you want to play with.");
        System.out.println("YOUR OPTIONS ARE: ");
        for (String teamName: DataManager.database("database/TEAMS.txt").keySet()) {
            System.out.println(teamName);
        }
        Thread.sleep(2000);

        System.out.print("Your choice: ");

        String teamChoice = sc.nextLine();
        Team chosenTeam = DataManager.handleTeamChoice(teamChoice, coachName);
        while (chosenTeam == null) {
            System.out.println("Try again: ");
            teamChoice = sc.nextLine();
            chosenTeam = DataManager.handleTeamChoice(teamChoice, coachName);
        }


        System.out.println("Awesome! You have picked " + chosenTeam.getName() + ".");
        Thread.sleep(3000);
        Player random = chosenTeam.getRandomPlayer();
        System.out.println("\n" + random.getName() + ": Greetings, coach " + chosenTeam.getCoach() + "!");
        Thread.sleep(3000);
        System.out.println("On behalf of the team, I would like to welcome you to " + chosenTeam.getName() + ".");
        Thread.sleep(3000);
        System.out.println("We're excited to have you on your first match for the league!");
        Thread.sleep(3000);
        System.out.println("I hope I get to be on the starting XI.");
        Thread.sleep(4000);

        System.out.println("\na) Nice to meet you, " + random.getName() + "!\nI appreciate your enthusiasm.\nI haven't picked my players yet, but I will do so soon.");
        System.out.println("\nb) Uh... Starting XI?");
        System.out.println("\nc) I actually don't wanna be a coach. I'm outta here.");
        System.out.print("\nYour choice: ");

        String choice = sc.nextLine();
        switch (choice.toLowerCase()) {
            case "b":
                System.out.println("\n" + random.getName() + ": Yeah, I mean, the eleven players that start on the pitch at a football match.");
                Thread.sleep(3000);
                System.out.println("You know what that is, right?");
                Thread.sleep(3000);
                System.out.println("\n-Do you want to build your starting XI now?-");
                System.out.println("\na) Sure!");
                System.out.println("b) Nah. I don't like this game");
                System.out.print("Your choice: ");

                String choice2 = sc.nextLine();
                if (choice2.toLowerCase().equals("b")) {
                    System.out.println("I don't like you either. LOL");
                    System.out.println("bye");
                    break;
                }

            case "a":
                System.out.println("\n" + random.getName() + ": Amazing!");
                lineupBuild(chosenTeam, sc);
                break;


        }

    }

    public static void lineupBuild(Team team, Scanner sc) throws Exception {

        Thread.sleep(3000);
        System.out.println("\nIt's time to build your starting XI!");
        Thread.sleep(2000);
        System.out.println("First, you need to pick a formation.");
        Thread.sleep(2000);
        System.out.println("Which do you choose?");
        Thread.sleep(3000);

        System.out.println("\na) 4-3-3");
        System.out.println("b) 4-4-2");
        System.out.println("c) 3-5-2");
        System.out.println("d) 5-3-2");
        System.out.print("Your choice: ");

        String lineupChoice = sc.nextLine();
        int def, mid, ata;
        switch (lineupChoice.toLowerCase()) {
            case "a":
                def = 4;
                mid = 3;
                ata = 3;
                System.out.println("Amazing! You've picked the " + def + "-" + mid + "-" + ata + " formation.");
                lineupBuildHelper(sc, team, def, mid, ata);
                break;

            case "b":
                def = 4;
                mid = 4;
                ata = 2;
                System.out.println("Amazing! You've picked the " + def + "-" + mid + "-" + ata + " formation.");
                lineupBuildHelper(sc, team, def, mid, ata);
                break;

            case "c":
                def = 3;
                mid = 5;
                ata = 2;
                System.out.println("Amazing! You've picked the " + def + "-" + mid + "-" + ata + " formation.");
                lineupBuildHelper(sc, team, def, mid, ata);
                break;

            case "d":
                def = 5;
                mid = 3;
                ata = 2;
                System.out.println("Amazing! You've picked the " + def + "-" + mid + "-" + ata + " formation.");
                lineupBuildHelper(sc, team, def, mid, ata);
                break;

            default:
                System.out.println("Invalid choice! Please try again.");
                lineupBuild(team, sc);
                break;
        }



    }

    public static void lineupBuildHelper(Scanner sc, Team team, int def, int mid, int ata) throws Exception {
        ArrayList<Player> startingXI = new ArrayList<>();

        Thread.sleep(2000);
        System.out.println("\nINSTRUCTIONS: when choosing a player,");
        System.out.println("type their JERSEY NUMBER exactly as it is displayed.");
        Thread.sleep(5000);
        System.out.println("When choosing more than one player,");
        System.out.println("make sure to separate the numbers with commas and NO SPACES.");


        Thread.sleep(5000);
        System.out.println("\nAlright, it's time to pick a goalkeeper!");

        while (true) {
            System.out.println("\nYour options are:");
            for (Player p : team.getGoalkeepers()) {
                System.out.println(p + " RATING: " + p.getRating());
            }
            String gkNum = sc.nextLine();
            try {
                if (team.findPlayerByNumber(Integer.parseInt(gkNum)) != null) {
                    Player currPlayer = team.findPlayerByNumber(Integer.parseInt(gkNum));
                    startingXI.add(currPlayer);
                    System.out.println(currPlayer.getName() + " has been added to the lineup!");
                    break;
                } else {
                    System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid choice! Please try again.");
            }
        }

        Thread.sleep(3000);
        System.out.println("\nNow, you can choose your defenders. Pick " + def + ". Remember your formation!");
        Thread.sleep(5000);

        while (true) {
            System.out.println("\nYour options are:");
            for (Player p : team.getDefenders()) {
                System.out.println(p + " RATING: " + p.getRating());
            }
            String defNums = sc.nextLine();
            String[] defArray = defNums.split(",");
            if (defArray.length != def) {
                System.out.println("Invalid choice! Please try again.");
            } else {
                for (String playerNum : defArray) {
                    Player currPlayer = team.findPlayerByNumber(Integer.parseInt(playerNum));
                    startingXI.add(currPlayer);
                    System.out.println(currPlayer.getName() + " has been added to the lineup!");
                }
                break;
            }

        }

        Thread.sleep(3000);
        System.out.println("\nNeat! Time for the midfielders. Pick " + mid + ".");
        Thread.sleep(5000);

        while (true) {
            System.out.println("\nYour options are:");
            for (Player p : team.getMidfielders()) {
                System.out.println(p + " RATING: " + p.getRating());
            }

            String midNums = sc.nextLine();
            String[] midArray = midNums.split(",");
            if (midArray.length != mid) {
                System.out.println("Invalid choice! Please try again.");
            } else {
                for (String playerNum : midArray) {
                    Player currPlayer = team.findPlayerByNumber(Integer.parseInt(playerNum));
                    startingXI.add(currPlayer);
                    System.out.println(currPlayer.getName() + " has been added to the lineup!");
                }
                break;
            }
        }

        Thread.sleep(3000);
        System.out.println("\nYou're almost there - choose your forwards! Pick " + ata + ".");
        Thread.sleep(5000);

        while (true) {
            System.out.println("\nYour options are:");
            for (Player p : team.getForwards()) {
                System.out.println(p + " RATING: " + p.getRating());
            }

            String fwdNums = sc.nextLine();
            String[] fwdArray = fwdNums.split(",");
            if (fwdArray.length != ata) {
                System.out.println("Invalid choice! Please try again.");
            } else {
                for (String playerNum : fwdArray) {
                    Player currPlayer = team.findPlayerByNumber(Integer.parseInt(playerNum));
                    startingXI.add(currPlayer);
                    System.out.println(currPlayer.getName() + " has been added to the lineup!");
                }
                break;
            }
        }

        Thread.sleep(3000);
        System.out.println("\nAmazing! You have your first ever lineup!");
        Thread.sleep(5000);
        System.out.println("STARTING XI LOADING...");
        Thread.sleep(5000);
        for (Player p : startingXI) {
            System.out.println(p);
        }


    }













    }


