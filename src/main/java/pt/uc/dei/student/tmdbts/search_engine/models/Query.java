package pt.uc.dei.student.tmdbts.search_engine.models;


import java.io.Serializable;

public class Query implements Serializable {

    private String query;

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
