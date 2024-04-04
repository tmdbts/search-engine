package pt.uc.dei.student.tmdbts.search_engine.downloader;

import org.jsoup.nodes.Element;
import pt.uc.dei.student.tmdbts.search_engine.protocol.Message;
import pt.uc.dei.student.tmdbts.search_engine.protocol.RequestTypes;
import pt.uc.dei.student.tmdbts.search_engine.protocol.SEProtocol;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Downloader implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final String url;

    private SEProtocol protocol;

    public Downloader(URI url) {
        this.url = url.toString();
        this.protocol = new SEProtocol();
    }

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

        System.out.println("Found " + urls.size() + " URLs");

        ArrayList<String> words = new ArrayList<>();
        words.add("word1");
        words.add("word2");

        Message message = new Message();
        message.setType(RequestTypes.WORD_LIST);
        message.setList(words);
        message.setListName("words");

        // TODO: send found URLs to URLQueue

        // TODO: get words

        // TODO: send data to inverted index
        try {
            protocol.sendMessage(message.encode());
        } catch (Exception e) {
            System.out.println("Error encoding message: " + e);
        }

//        notify();
    }

    /**
     * Send data to the server through multicast
     */
    private void sendData() {

    }
}
