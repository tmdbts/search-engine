package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.Serializable;
import java.net.URI;

public class URIInfo implements Serializable {
    private URI uri;

    private String title;

    private String description;

    URIInfo() {
    }

    URIInfo(URI uri) {
        this.uri = uri;
    }

    URIInfo(URI uri, String title, String description) {
        this.uri = uri;
        this.title = title;
        this.description = description;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
