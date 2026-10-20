import java.util.*;

public class Leaderboard {

    public static void main(String[] args) {

        Map<String, Integer> scores = new HashMap<>();

        scores.put("Rahul", 85);
        scores.put("Priya", 95);
        scores.put("Amit", 90);
        scores.put("Neha", 95);
        scores.put("Karan", 80);

        
        List<Map.Entry<String, Integer>> players =
            new ArrayList<>(scores.entrySet());

       
        Collections.sort(players,
            new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(
                        Map.Entry<String, Integer> player1,
                        Map.Entry<String, Integer> player2) {

                    int scoreComparison =
                        player2.getValue().compareTo(
                            player1.getValue());

                    if (scoreComparison != 0) {
                        return scoreComparison;
                    }

                    return player1.getKey().compareTo(
                        player2.getKey());
                }
            });

        
        int topN = 3;

        System.out.println("Top " + topN + " Players:");

        for (int i = 0; i < Math.min(topN, players.size()); i++) {

            Map.Entry<String, Integer> player = players.get(i);

            System.out.println(
                player.getKey() + " - " + player.getValue()
            );
        }
    }
}