import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    static String playerFile = "src/player_data.txt";
    static String matchData = "src/match_data.txt";

    public static void main(String[] args) {
        ArrayList<Match> matches = parseMatchData();
        HashMap<String, Player> players = parsePlayerData(matches);
        formatOutputData(players);
    }

    static HashMap<String, Player>  parsePlayerData(ArrayList<Match> matches) {
        HashMap<String, Player> playerHashMap = new HashMap<>();

        try {
            File inputFile = new File(playerFile);
            Scanner fileReader = new Scanner(inputFile);

            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                String[] separatedData = data.split(",", -1);
                String playerUUID = separatedData[0];
                String playerOperation = separatedData[1];
                String matchID = separatedData[2];
                Float coinNumber = Float.parseFloat(separatedData[3]);
                String betOnWhichSide = separatedData[4];

                if (!playerHashMap.containsKey(playerUUID)) {
                    playerHashMap.put(playerUUID, new Player(playerUUID));
                }

                switch (playerOperation) {
                    case "BET":
                        playerHashMap.get(playerUUID).placedABet();

                        if(playerHashMap.get(playerUUID).getCoin() < coinNumber){
                            playerHashMap.get(playerUUID).setIllegalMoveStatusToTrue();
                            playerHashMap.get(playerUUID).setFirstIllegalMove(playerOperation,matchID,
                                    (int)Math.floor(coinNumber), betOnWhichSide);
                        }

                        for (Match match : matches) {
                            if (Objects.equals(match.getUuid(), matchID)) {
                                if (Objects.equals(match.getMatchResult(), betOnWhichSide)) {
                                    playerHashMap.get(playerUUID).addCoin(coinNumber * match.getReturnRate());
                                    playerHashMap.get(playerUUID).addCoinWins(coinNumber * match.getReturnRate());
                                    playerHashMap.get(playerUUID).wonABet();
                                } else if (!Objects.equals(match.getMatchResult(), betOnWhichSide) &&
                                        !Objects.equals(match.getMatchResult(), "DRAW")) {
                                    playerHashMap.get(playerUUID).removeCoin(coinNumber);
                                    playerHashMap.get(playerUUID).addCoinLosses(coinNumber);
                                }
                            }
                        }
                        break;

                    case "DEPOSIT":
                        playerHashMap.get(playerUUID).addCoin(coinNumber);
                        break;

                    case "WITHDRAW":
                        if(playerHashMap.get(playerUUID).getCoin() > coinNumber) {
                            playerHashMap.get(playerUUID).removeCoin(coinNumber);
                        } else {
                            playerHashMap.get(playerUUID).setIllegalMoveStatusToTrue();
                            playerHashMap.get(playerUUID).setFirstIllegalMove(playerOperation,
                                    "null",(int)Math.floor(coinNumber), "null");
                        }
                        break;
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.getCause();
        }
    return playerHashMap;
    }

    static ArrayList<Match> parseMatchData() {
        ArrayList<Match> matches = new ArrayList<>();
        try {
            File inputFile = new File(matchData);
            Scanner fileReader = new Scanner(inputFile);

            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                String[] separatedData = data.split(",", -1);
                Match match = new Match(separatedData[0],separatedData[1],separatedData[2],separatedData[3]);
                matches.add(match);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.getCause();
        }
        return matches;
    }

    static void formatOutputData(HashMap<String, Player> players) {
        Casino casino = new Casino();
        ArrayList<String> legitPlayers = new ArrayList<>();
        ArrayList<String> illegitimatePlayers = new ArrayList<>();
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/out/production/BettingSystem/results.txt";

        for(Map.Entry<String, Player> entry : players.entrySet()){
            Player player = entry.getValue();

            if(!player.checkIfMadeIllegalMove()){
                casino.addBalance(player.getLosses());
                casino.removeBalance(player.getWins());
                legitPlayers.add(player.getUUID() + " " + player.getCoin() + " "
                        + String.format("%.2f",(player.getWinRate())));
            } else {
                illegitimatePlayers.add(player.getIllegalMove());
            }
        }
        prepareForWriting(legitPlayers, illegitimatePlayers, casino, filePath);

        // This is just in case because i didn't know in the instructions did you mean next to main class.
        // Was it meant for src folder or out folder?
        // im pretty sure it was the out folder but just in case I decided to print to src folder also:
        filePath = currentWorkingDirectory + "/src/results.txt";
        prepareForWriting(legitPlayers, illegitimatePlayers, casino, filePath);
    }

    private static void prepareForWriting(ArrayList<String> legitPlayers, ArrayList<String> illegitimatePlayers,
                                          Casino casino, String filePath) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writeDataToFile(legitPlayers, writer);
            writer.newLine();
            writeDataToFile(illegitimatePlayers, writer);
            writer.newLine();
            writer.write(Integer.toString(casino.getBalance()));
        } catch (IOException e) {
            e.getCause();
        }
    }

    private static void writeDataToFile(ArrayList<String> arrayList, BufferedWriter writer) throws IOException {
        for (String element : arrayList) {
            writer.write(element);
            writer.newLine();
        }
    }
}