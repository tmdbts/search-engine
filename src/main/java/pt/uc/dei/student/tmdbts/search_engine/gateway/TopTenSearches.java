package pt.uc.dei.student.tmdbts.search_engine.gateway;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents the top 10 searches of the system
 */
public class TopTenSearches implements Serializable {
    /**
     * Search query frequency
     */
    private HashMap<String, Integer> searchQueryFrequency = new HashMap<>();

    /**
     * Top 10 searches
     */
    private HashMap<Integer, String> top10Searches = new HashMap<>();

    /**
     * Add a search query to the searchQueryFrequency map
     * <p>
     * If the query already exists, increment the frequency.
     * If the top10 searches change, update the top10Searches map
     *
     * @param query the search query
     */
    public void addSearchQueryFrequency(String query) {
        if (searchQueryFrequency.containsKey(query)) {
            searchQueryFrequency.put(query, searchQueryFrequency.get(query) + 1);

            return;
        }

        searchQueryFrequency.put(query, 1);
    }

    /**
     * Get the top 10 searches
     *
     * @return the top 10 searches
     */
    public HashMap<Integer, String> getTop10Searches() {
        HashMap<Integer, String> newTop = new HashMap<>();

        searchQueryFrequency.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(10)
                .forEach(entry -> newTop.put(entry.getValue(), entry.getKey()));

        return newTop;
    }

    /**
     * Check if the top 10 searches have changed
     *
     * @return the new top 10 searches
     */
    public HashMap<Integer, String> didTopChange() {
        HashMap<Integer, String> newTop = getTop10Searches();

        if (newTop.equals(top10Searches)) {
            return null;
        }

        top10Searches = newTop;
        return getTopDifference(newTop);
    }

    /**
     * Get the difference between the new top 10 searches and the old top 10 searches
     *
     * @param newTop the new top 10 searches
     * @return the difference between the new top 10 searches and the old top 10 searches
     */
    private HashMap<Integer, String> getTopDifference(HashMap<Integer, String> newTop) {
        HashMap<Integer, String> difference = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            if (!newTop.containsKey(i) || !top10Searches.containsKey(i) || !newTop.get(i).equals(top10Searches.get(i))) {
                difference.put(i, newTop.get(i));
            }
        }

        return difference;
    }
}
