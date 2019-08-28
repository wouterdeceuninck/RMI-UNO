package rmi.unogame.applicationServer.game.score;

import rmi.unogame.applicationServer.game.ApplicationPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EndScoreCalculator {

    public static Map<String, Integer> calculateScore(List<ApplicationPlayer> playersList) {
            Map<String, Integer> endScore = new HashMap<>();
            if (playersList.size() == 2) {
                List<ApplicationPlayer> orderedList = orderOnScore(playersList);
                endScore.put(orderedList.get(0).getUsername(), 2);
                endScore.put(orderedList.get(1).getUsername(), -2);
            }
            if (playersList.size() == 3) {
                List<ApplicationPlayer> orderedList = orderOnScore(playersList);
                endScore.put(orderedList.get(0).getUsername(), 3);
                endScore.put(orderedList.get(1).getUsername(), 0);
                endScore.put(orderedList.get(2).getUsername(), -3);
            }
            if (playersList.size() == 4) {
                List<ApplicationPlayer> orderedList = orderOnScore(playersList);
                endScore.put(orderedList.get(0).getUsername(), 4);
                endScore.put(orderedList.get(1).getUsername(), 0);
                endScore.put(orderedList.get(2).getUsername(), 0);
                endScore.put(orderedList.get(2).getUsername(), -4);
            }
            return endScore;
        }

        private static List<ApplicationPlayer> orderOnScore(List<ApplicationPlayer> applicationPlayers) {
            return applicationPlayers.stream()
                    .sorted(EndScoreCalculator::comparePlayers)
                    .collect(Collectors.toList());
        }

        private static int comparePlayers(ApplicationPlayer player1, ApplicationPlayer player2) {
            return Integer.compare(player1.getScore(), player2.getScore());
        }
    }