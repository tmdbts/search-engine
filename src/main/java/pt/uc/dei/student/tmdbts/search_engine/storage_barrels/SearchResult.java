package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the search results
 * <p>
 * The search results are a list of URIInfo that represent the search results and the time it took to perform the search
 */
public class SearchResult implements Serializable {
    /**
     * List of URIInfo that represent the search results
     */
    private ArrayList<URIInfo> results = new ArrayList<>();

    /**
     * Time it took to perform the search
     */
    private long queryTime;

    /**
     * Constructor
     */
    SearchResult() {
    }

    /**
     * Constructor
     *
     * @param results   List of URIInfo that represent the search results
     * @param queryTime Time it took to perform the search
     */
    SearchResult(ArrayList<URIInfo> results, long queryTime) {
        this.results = results;
        this.queryTime = queryTime;
    }

    /**
     * Add a URIInfo to the search results
     *
     * @param uriInfo URIInfo to add
     */
    public void addInfo(URIInfo uriInfo) {
        try {
            results.add(uriInfo);
        } catch (Exception e) {
            System.out.println("Error adding URIInfo: " + e.getMessage());
        }


        System.out.println(results.size());
    }

    public ArrayList<URIInfo> getResults() {
        return results;
    }

    public void setResults(ArrayList<URIInfo> results) {
        this.results = results;
    }

    public long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(long queryTime) {
        this.queryTime = queryTime;
    }

    public int getTotal_results() {
        return results.size();
    }

    public List<URIInfo> return10(int startIndex) {
        List<URIInfo> return10URLs = new ArrayList<>();

        int lastIndex = startIndex + 10;

        if (lastIndex > results.size()) {
            lastIndex = results.size() - 1;
        }

        for (int i = startIndex; i < lastIndex; i++) {
            return10URLs.add(results.get(i));

            System.out.println("RESULTADOS DIGNOs " + results.get(i).getUri());
        }

        return return10URLs;
    }
}
