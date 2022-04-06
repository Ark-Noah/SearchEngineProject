import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SearchEngine {

    private HashMap<String, List<String>> documentMap = new HashMap<>();
    private HashMap<String, HashMap<String, Double>> wordIndex = new HashMap<>();

    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        searchEngine.startSearchEngine();
    }

    private double getTF(String document, String word) {
        int wordFreq = Collections.frequency(documentMap.get(document), word);
        int wordsCount = documentMap.get(document).size();
        return ((double) wordFreq / wordsCount);
    }

    private double getIDF(String word) {
        int numOfDocs = documentMap.size();
        int wordFreq = 0;
        for (String key : documentMap.keySet()) {
            if (Collections.frequency(documentMap.get(key), word) > 0) {
                wordFreq += 1;
            }
        }
        return Math.log(((double) numOfDocs / wordFreq));
    }

    //Calculate and store each word's TF-IDF
    private HashMap<String, Double> getTFIDF(String word) {
        double tf = 0, idf = 0;
        idf = getIDF(word);
        HashMap<String, Double> map = new HashMap<>();
        for (String key : documentMap.keySet()) {
            if (Collections.frequency(documentMap.get(key), word) > 0) {
                tf = getTF(key, word);
                map.put(key, tf * idf);
            }
        }
        wordIndex.put(word, sortHashMap(map));
        return map;
    }

    private void startSearchEngine() {
        Documents docs = new Documents();
        //get all documents and words from the directory
        documentMap = docs.getDocuments();
        if(documentMap.isEmpty()) {
            System.out.println("There are no documents to be searched!");
            return;
        }else {
            for (String key : documentMap.keySet()) {
                for(String word : documentMap.get(key)) {
                    getTFIDF(word);
                }
            }
        }

        //Starting user queries
        String input, quit = "";
        Scanner in = new Scanner(System.in);
        System.out.println("Search Engine started. You may press q to exit the program.\n");
        while (quit != "q") {
            System.out.print("Enter the word: ");
            input = in.nextLine();
            if (input.equals("q"))
                break;
            try{
                System.out.println(input + ": " + wordIndex.get(input).keySet());
            }catch(NullPointerException e){
                System.out.println("The word does not exist");
            }
        }
        in.close();
    }

    // This method creates list of map elements,
    // and sorts them using lambda expression.
    public static HashMap<String, Double> sortHashMap(HashMap<String, Double> map) {

        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(map.entrySet());
        Collections.sort(list, (m1, m2) -> m2.getValue().compareTo(m1.getValue()));

        HashMap<String, Double> tempMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> m : list) {
            tempMap.put(m.getKey(), m.getValue());
        }
        return tempMap;
    }
}
