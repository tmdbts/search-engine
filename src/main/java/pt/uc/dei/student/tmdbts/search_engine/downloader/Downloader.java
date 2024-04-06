package pt.uc.dei.student.tmdbts.search_engine.downloader;

import org.jsoup.nodes.Element;
import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.protocol.CommunicationHandler;
import pt.uc.dei.student.tmdbts.search_engine.protocol.Message;
import pt.uc.dei.student.tmdbts.search_engine.protocol.RequestTypes;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Downloader implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final String url;

    private CommunicationHandler commHandler;

    private Gateway gateway;

    public Downloader(URI url, Gateway gateway) {
        this.url = url.toString();
        this.gateway = gateway;
        this.commHandler = new CommunicationHandler();
    }

    public Downloader(String url, Gateway gateway, CommunicationHandler commHandler) {
        this.url = url;
        this.gateway = gateway;
        this.commHandler = commHandler;
    }

    private List<URI> getURLs(String url) {
        List<Element> fetchedUrls = HtmlParser.getURLs(url);

        List<URI> urls = new ArrayList<>();

        for (Element element : fetchedUrls) {
            String href = element.attr("href");

            while (href.endsWith("/") || href.endsWith("#")) {
                href = href.substring(0, href.length() - 1);
            }

            try {
                URI uri = new URI(href);
                if (!uri.isAbsolute()) {
                    continue;
                }

                urls.add(uri);
            } catch (Exception e) {
                LOGGER.warning("Error parsing URL: " + e);
            }
        }

        return urls;
    }

    private ArrayList<String> generateIndexMessage(ArrayList<String> words) {
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("url", url);

        return Message.encode(bodyMap, RequestTypes.WORD_LIST, "word", words);
    }

    @Override
    public void run() {
        LOGGER.info("Starting download of " + this.url);

        List<URI> urls = getURLs(this.url);

        System.out.println("Found " + urls.size() + " URLs in " + this.url);

        try {
            gateway.addURL(urls);
        } catch (RemoteException e) {
            System.out.println("Error adding URLs to queue: " + e);
        }

        ArrayList<String> words = HtmlParser.getWords(this.url);

        try {
            commHandler.sendMessage(generateIndexMessage(words));
        } catch (Exception e) {
            System.out.println("Error encoding message: " + e);
        } finally {
            commHandler.closeSocket();
        }
    }

    /**
     * Send data to the server through multicast
     */
    private void sendData() {

    }
}
