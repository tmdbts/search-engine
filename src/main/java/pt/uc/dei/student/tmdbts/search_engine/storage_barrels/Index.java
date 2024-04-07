package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.protocol.Message;

import java.net.URI;
import java.nio.file.Path;
import java.util.*;

public class Index {

    private HashMap<String, ArrayList<URI>> index = new HashMap<>();

    private HashMap<URI, ArrayList<URI>> urls = new HashMap<>();

    private Message message;

    public HashMap<String, ArrayList<URI>> getIndex() {
        return index;
    }

    public void setIndex(HashMap<String, ArrayList<URI>> index) {
        this.index = index;
    }

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

    public URIInfo handleMetaData(Message message) {
        URI url = URI.create(message.getBodyMap().get("url"));

        return new URIInfo(url, message.getBodyMap().get("meta_0_name"), message.getBodyMap().get("meta_1_name"));

    }

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

}