package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.protocol.Message;

import java.net.URI;
import java.nio.file.Path;
import java.util.*;

/**
 * Class that represents the index
 * <p>
 * The index is a data structure that stores the words and the URLs that contain them. Its format is:
 * <p>
 * word -> [url1, url2, url3]
 */
public class Index {
    /**
     * Index
     */
    private HashMap<String, ArrayList<URI>> index = new HashMap<>();

    /**
     * TODO: check
     * List of URLs information
     */
    private HashMap<URI, ArrayList<URI>> urls = new HashMap<>();

    /**
     * Handle a word list request. Add or update the words to the index.
     * <p>
     * For each word in the list, a URI is created from the URL in the message.
     * If the word is already in the index, the URI is added to the list of URLs that contain the word.
     * If the word is not in the index, a new list is created with the URI.
     *
     * @param message The message with the word list
     */
    public void handleWordList(Message message) {
        for (String item : message.getList()) {
            URI current = URI.create(message.getBodyMap().get("url"));

            ArrayList<URI> currentURL = new ArrayList<>();

            if (index.get(item) != null && index.get(item).toString().equals(current.toString())) {
                continue;
            } else if (index.get(item) != null && !index.get(item).contains(current)) {
                index.get(item).add(current);
            } else {
                currentURL.add(current);
                index.put(item, currentURL);
            }

            Path path = Path.of("./persistence/index.txt");

            try {
                FileReadWriter.writeIndexData(index, path.toString());
            } catch (Exception e) {
                System.err.println("Error writing to file in Index: " + e.getMessage());
            }
        }
    }

    /**
     * Handle a URL list request. Add or update the URLs to the list.
     * <p>
     * For each URL in the list, a URI is created from the URL in the message.
     * If the URL is already in the list, the URI is added to the list of URLs that contain the URL.
     * If the URL is not in the list, a new list is created with the URI.
     *
     * @param message The message with the URL list
     */
    public void handleURLList(Message message) {
        for (String url : message.getList()) {
            URI current = URI.create(message.getBodyMap().get("url"));
            URI urlsCurrent = URI.create(url);

            ArrayList<URI> currentURL = new ArrayList<>();

            if (urls.get(urlsCurrent) != null && urls.get(urlsCurrent).contains(current)) {
                continue;
            } else if (urls.get(urlsCurrent) != null && !urls.get(urlsCurrent).contains(current)) {
                urls.get(urlsCurrent).add(current);
            } else {
                currentURL.add(current);
                urls.put(urlsCurrent, currentURL);
            }

            Path path = Path.of("./persistence/url_list.txt");

            FileReadWriter.writeUrlsData(urls, path.toString());
        }
    }

    /**
     * Handle a metadata request.
     * <p>
     * Creates a URIInfo object with the URL and the metadata.
     *
     * @param message The message with the metadata
     */
    public URIInfo handleMetaData(Message message) {
        URI url = URI.create(message.getBodyMap().get("url"));

        return new URIInfo(url, message.getBodyMap().get("meta_0_name"), message.getBodyMap().get("meta_1_name"));

    }

    /**
     * Handle a search request. Return the URLs that contain the words in the query.
     * <p>
     * The query is split into words. For each word, the URLs that contain the word are fetched from the index.
     * The URLs that contain all the words in the query are fetched.
     * The URLs are ordered by the number of words they contain.
     *
     * @param query The query
     * @return The URLs that match the query
     */
    public List<URI> handleQuery(String query) {
        String[] splitedQuery = query.split(" ");

        HashMap<String, ArrayList<URI>> results = new HashMap<>();
        for (String word : splitedQuery) {
            if (index.containsKey(word)) {
                results.put(word, index.get(word));
            }
        }

        List<URI> orderedURLs;

        orderedURLs = verifyURLs(results);
        orderedURLs = orderURLs(orderedURLs);

        System.out.println("ORDERED URLS: " + orderedURLs);

        return orderedURLs;
    }

    public List<URI> handleURL(URI url) {

        List<URI> resultURLs = new ArrayList<>();

        resultURLs = urls.get(url);

        return resultURLs;
    }

    /**
     * Check if the URLs contain all the words in the query.
     * <p>
     * The URLs are fetched from the index. The URLs that contain all the words in the query are fetched.
     *
     * @param verifyURL The URLs to verify
     * @return The URLs that contain all the words in the query
     */
    private List<URI> verifyURLs(HashMap<String, ArrayList<URI>> verifyURL) {

        List<URI> commonURLs = null;

        for (List<URI> urls : verifyURL.values()) {
            if (commonURLs == null) {
                commonURLs = new ArrayList<>(urls);
            } else {
                commonURLs.retainAll(urls);
            }
        }

        return commonURLs;
    }

    /**
     * Order the URLs by the number of words they contain.
     * <p>
     * The URLs are ordered by the number of words they contain.
     *
     * @param urlsToOrder The URLs to order
     * @return The ordered URLs
     */
    public List<URI> orderURLs(List<URI> urlsToOrder) {
        Comparator<URI> comparator = new Comparator<URI>() {

            public int compare(URI uri1, URI uri2) {

                int size1 = 0;
                int size2 = 0;

                if (urls.containsKey(uri1)) {
                    size1 = urls.get(uri1).size();
                }

                if (urls.containsKey(uri2)) {
                    size2 = urls.get(uri2).size();
                }

                return Integer.compare(size1, size2);

            }

        };

        List<URI> sortedURLs = new ArrayList<>(urlsToOrder);

        Collections.sort(sortedURLs, comparator);

        return sortedURLs;
    }

    public HashMap<String, ArrayList<URI>> getIndex() {
        return index;
    }

    public void setIndex(HashMap<String, ArrayList<URI>> index) {
        this.index = index;
    }
}