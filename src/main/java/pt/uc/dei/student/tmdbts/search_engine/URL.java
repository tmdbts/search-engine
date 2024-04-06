package pt.uc.dei.student.tmdbts.search_engine;

import java.io.Serializable;

/**
 * URL
 */
public class URL implements Serializable {
    /**
     * The complete URL
     */
    private final String fullURL;

    /**
     * The protocol of the URL
     */
    private final String protocol;

    /**
     * The domain of the URL
     */
    private final String domain;

    /**
     * The path of the URL
     */
    private final String path;

    /**
     * The query of the URL
     */
    private final String query;

    public URL(String fullURL) {
        this.fullURL = fullURL;
        this.protocol = fullURL.split("://")[0];
        this.domain = fullURL.split("://")[1].split("/")[0];
        this.path = fullURL.split("://")[1].split("/")[1].split("\\?")[0];
        this.query = fullURL.split("://")[1].split("/")[1].split("\\?")[1];
    }

    /**
     * Get the full URL
     *
     * @return the full URL
     */
    public String getFullURL() {
        return fullURL;
    }

    /**
     * Get the protocol of the URL
     *
     * @return the protocol of the URL
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Get the domain of the URL
     *
     * @return the domain of the URL
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Get the path of the URL
     *
     * @return the path of the URL
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the query of the URL
     *
     * @return the query of the URL
     */
    public String getQuery() {
        return query;
    }
}
