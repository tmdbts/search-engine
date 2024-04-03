package pt.uc.dei.student.tmdbts.search_engine.downloader;

import org.jsoup.nodes.Element;
import pt.uc.dei.student.tmdbts.search_engine.URL;

import java.util.List;
import java.util.logging.Logger;

public class Downloader implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final String url;

    private final String multicastAddress = "";

    public Downloader(String url) {
        this.url = url;
//        TODO: Initialise multicast address
        String MULTICAST_ADDRESS = "224.3.2.1";
        int PORT = 4321;
    }

    private List<URL> getURLs(String url) {
        List<Element> fetchedUrls = HtmlParser.getURLs(url);

        return URL.parse(fetchedUrls);
    }

    @Override
    public void run() {
        LOGGER.info("Starting download of " + this.url);

        List<URL> urls = getURLs(this.url);
//        TODO: send found URLs to URLQueue

//        TODO: get words

//        TODO: send data to inverted index
    }

    /**
     * Send data to the server through multicast
     */
    private void sendData() {

    }
}