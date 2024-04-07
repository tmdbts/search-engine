package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResult implements Serializable {
    private ArrayList<URIInfo> results = new ArrayList<>();

    private long queryTime;

    SearchResult() {
    }

    SearchResult(ArrayList<URIInfo> results, long queryTime) {
        this.results = results;
        this.queryTime = queryTime;
    }

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
}
