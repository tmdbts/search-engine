package pt.uc.dei.student.tmdbts.search_engine.models;


import java.io.Serializable;
import java.net.URI;

/**
 * This represents a query. It is a model class.
 */
public class Query implements Serializable {

    private String query;
    private URI url;

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public URI getUrl() {
        return url;
    }
}
