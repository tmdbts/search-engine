package pt.uc.dei.student.tmdbts.search_engine;

import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

        if (fullURL.split("://")[1].split("/").length == 1) {
            this.path = "";
            this.query = "";

            return;
        }

        this.path = fullURL.split("://")[1].split("/")[1].split("\\?")[0];

        if (fullURL.split("://")[1].split("/")[1].split("\\?").length == 1) {
            this.query = "";

            return;
        }

        this.query = fullURL.split("://")[1].split("/")[1].split("\\?")[1];
    }

    public static List<URL> parse(List<Element> urls) {
        List<URL> parsedURLs = new ArrayList<>();

        for (Element url : urls) {
            parsedURLs.add(new URL(url.attr("abs:href")));
        }

        return parsedURLs;
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

    @Override
    public String toString() {
        return fullURL;
    }
}
