package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.Serializable;
import java.util.ArrayList;

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
            System.out.println("URIINFO ADICIONADO" + uriInfo.getUri() + "\n");
        } catch (Exception e) {
            System.out.println("Error adding URIInfo: " + e.getMessage());
        }
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
}
