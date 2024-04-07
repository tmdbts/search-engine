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

/**
 * Class that represents a downloader
 * <p>
 * The downloader is responsible for downloading the content of a URL
 * It also extracts the words, metadata and URLs from the URL
 * The downloader sends the words, metadata and URLs to the server
 */
public class Downloader implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * URL to download
     */
    private final String url;

    /**
     * Communication handler to send messages
     */
    private CommunicationHandler commHandler;

    /**
     * Gateway to add URLs to the queue
     */
    private Gateway gateway;

    /**
     * Constructor
     *
     * @param url     URL to download
     * @param gateway Gateway to add URLs to the queue
     */
    public Downloader(URI url, Gateway gateway) {
        this.url = url.toString();
        this.gateway = gateway;
        this.commHandler = new CommunicationHandler();
    }

    /**
     * Constructor
     *
     * @param url         URL to download
     * @param gateway     Gateway to add URLs to the queue
     * @param commHandler Communication handler to send messages
     */
    public Downloader(String url, Gateway gateway, CommunicationHandler commHandler) {
        this.url = url;
        this.gateway = gateway;
        this.commHandler = commHandler;
    }

    /**
     * Fetch URLs from a given URL
     *
     * @param url URL to fetch URLs from
     * @return List of URLs
     */
    private ArrayList<URI> getURLs(String url) {
        List<Element> fetchedUrls = HtmlParser.getURLs(url);

        ArrayList<URI> urls = new ArrayList<>();

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

    /**
     * Generate message to send to the server with the words found in the URL
     *
     * @param words List of words found in the URL
     * @return List of messages to send
     */
    private ArrayList<String> generateIndexMessage(ArrayList<String> words) {
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("url", url);

        return Message.encode(bodyMap, RequestTypes.WORD_LIST, "word", words);
    }

    /**
     * Generate message to send to the server with the metadata found in the URL
     *
     * @param head List of metadata found in the URL
     * @return List of messages to send
     */
    private ArrayList<String> generateMetaMessage(ArrayList<String> head) {
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("url", url);

        return Message.encode(bodyMap, RequestTypes.META_DATA, "meta", head);
    }

    /**
     * Generate message to send to the server with the URLs found in the URL
     *
     * @param urls List of URLs found in the URL
     * @return List of messages to send
     */
    private ArrayList<String> generateUrlListMessage(ArrayList<URI> urls) {
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("url", url);

        ArrayList<String> urlStrings = new ArrayList<>();
        for (URI uri : urls) {
            urlStrings.add(uri.toString());
        }

        return Message.encode(bodyMap, RequestTypes.URL_LIST, "url", urlStrings);
    }

    /**
     * Run the downloader.
     */
    @Override
    public void run() {
        LOGGER.info("Starting download of " + this.url);

        ArrayList<URI> urls = getURLs(this.url);

        System.out.println("Found " + urls.size() + " URLs in " + this.url);

        try {
            gateway.addURL(urls);
        } catch (RemoteException e) {
            System.out.println("Error adding URLs to queue: " + e);
        }

        ArrayList<String> words = HtmlParser.getWords(this.url);
        ArrayList<String> head = HtmlParser.getHead(this.url);

        try {
            commHandler.sendMessage(generateIndexMessage(words));
            commHandler.sendMessage(generateUrlListMessage(urls));
            commHandler.sendMessage(generateMetaMessage(head));
        } catch (Exception e) {
            System.out.println("Error encoding message: " + e);
        } finally {
            commHandler.closeSocket();
        }
    }
}
