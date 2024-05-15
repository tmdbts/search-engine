package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.Serializable;
import java.net.URI;

/**
 * Class that represents the information of a URI
 * <p>
 * It contains the URI, the title and the description of the URI
 */
public class URIInfo implements Serializable {
    /**
     * URI
     */
    private URI uri;

    /**
     * Title of the web page the URI represents
     */
    private String title;

    /**
     * Description of the web page the URI represents
     */
    private String description;

    /**
     * Constructor
     */
    URIInfo() {
    }

    /**
     * Constructor
     *
     * @param uri URI
     */
    URIInfo(URI uri) {
        this.uri = uri;
    }

    /**
     * Constructor
     *
     * @param uri   URI
     * @param title Title of the web page
     */
    URIInfo(URI uri, String title, String description) {
        this.uri = uri;
        this.title = title;
        this.description = description;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return uri.toString() + "\n" + title + "\n" + description;
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
