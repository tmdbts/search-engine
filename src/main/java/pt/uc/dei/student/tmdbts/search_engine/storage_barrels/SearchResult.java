package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResult implements Serializable {
    private ArrayList<URIInfo> results;

    private long queryTime;

    SearchResult() {
    }

    SearchResult(ArrayList<URIInfo> results, long queryTime) {
        this.results = results;
        this.queryTime = queryTime;
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
