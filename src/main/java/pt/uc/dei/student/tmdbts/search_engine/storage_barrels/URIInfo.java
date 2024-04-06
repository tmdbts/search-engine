package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.Serializable;

public class URIInfo implements Serializable {
    private String uri;

    private String title;

    private String description;

    URIInfo() {
    }

    URIInfo(String uri) {
        this.uri = uri;
    }

    URIInfo(String uri, String title, String description) {
        this.uri = uri;
        this.title = title;
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
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
