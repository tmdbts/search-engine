package pt.uc.dei.student.tmdbts.search_engine.downloader;

import org.jsoup.nodes.Element;
import pt.uc.dei.student.tmdbts.search_engine.protocol.Message;
import pt.uc.dei.student.tmdbts.search_engine.protocol.RequestTypes;
import pt.uc.dei.student.tmdbts.search_engine.protocol.SEProtocol;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class Downloader implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final String url;

    private SEProtocol protocol;

    public Downloader(String url, SEProtocol protocol) {
        this.url = url;
        this.protocol = protocol;
    }

    private List<URI> getURLs(String url) {
        List<Element> fetchedUrls = HtmlParser.getURLs(url);

        List<URI> urls = new ArrayList<>();

        for (Element element : fetchedUrls) {
            try {
                urls.add(new URI(element.attr("href")));
            } catch (Exception e) {
                LOGGER.warning("Error parsing URL: " + e);
            }
        }

        return urls;
    }

    @Override
    public void run() {
        LOGGER.info("Starting download of " + this.url);

        List<URI> urls = getURLs(this.url);
        // TODO: send found URLs to URLQueue

        // TODO: get words

        // TODO: send data to inverted index

        notify();
    }

    /**
     * Send data to the server through multicast
     */
    private void sendData() {

    }
}
