import java.util.*;

public class WordFrequency {

    public static void main(String[] args) {

        String paragraph =
            "java is easy java is powerful java is popular";

       
        String[] words = paragraph.toLowerCase().split("\\s+");

        
        HashMap<String, Integer> frequency = new HashMap<>();

        for (String word : words) {
            frequency.put(
                word,
                frequency.getOrDefault(word, 0) + 1
            );
        }


        List<Map.Entry<String, Integer>> wordList =
            new ArrayList<>(frequency.entrySet());

        Collections.sort(wordList,
            new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(
                        Map.Entry<String, Integer> word1,
                        Map.Entry<String, Integer> word2) {

                    return word2.getValue()
                        .compareTo(word1.getValue());
                }
            });

      
        System.out.println("Word Frequency:");

        for (Map.Entry<String, Integer> entry : wordList) {
            System.out.println(
                entry.getKey() + " = " + entry.getValue()
            );
        }
    }
}