package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.protocol.Message;

import java.net.URI;
import java.nio.file.Path;
import java.util.*;

public class Index {
    private ArrayList<String> namesList;
    private HashMap<String, ArrayList<URI>> index = new HashMap<>();
    private HashMap<URI, List<String>> meta = new HashMap<>();
    private HashMap<URI, ArrayList<URI>> urls = new HashMap<>();

    private Message message;

    public HashMap<String, ArrayList<URI>> getIndex() {
        return index;
    }

    public void setIndex(HashMap<String, ArrayList<URI>> index) {
        this.index = index;
    }

    public HashMap<URI, List<String>> getMeta() {
        return meta;
    }

    public void setMeta(HashMap<URI, List<String>> meta) {
        this.meta = meta;
    }

    public void handleMessage(String inComingMessage) {
        message = new Message(inComingMessage);
        message.parseMessage(inComingMessage);
        namesList = message.getList();

        if (message.getType() == null) {
            return;
        }

        switch (message.getType()) {
            case WORD_LIST:
                handleWordList();
                System.out.println("HANDLE WORD LIST " + message.getType() + "\n\n");
                break;

            case URL_LIST:
                handleURLList();
                System.out.println("HANDLE URL LIST " + message.getType() + "\n\n");
                break;

            case META_DATA:
                handleMetaData();
                System.out.println("HANDLE META DATA " + message.getType() + "\n\n");;
                break;
            default:
                System.out.println("Invalid message type");
        }
    }

    private void handleWordList() {

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

    private void handleURLList() {
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

    private void handleMetaData (){
        for (String meta : message.getList()) {
            URI url = URI.create(message.getBodyMap().get("url"));

            System.out.println("HERE AQUI " + message.getBodyMap().get("meta_0_name"));
            URIInfo uriInfo = new URIInfo(url, message.getBodyMap().get("meta_0_name"), message.getBodyMap().get("meta_1_name"));

            System.out.println("URL " + message.getBodyMap().get("url"));
            System.out.println("Titulo " + message.getBodyMap().get("meta_o_name"));
            System.out.println("Descricao " + message.getBodyMap().get("meta_1_name"));

        }
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

        return orderedURLs;
    }

    private List<URI> verifyURLs (HashMap<String, ArrayList<URI>> verifyURL){

        List<URI> commonURLs = null;

        for (List<URI> urls : verifyURL.values()){
            if (commonURLs == null){
                commonURLs = new ArrayList<>(urls);
            }
            else {
                commonURLs.retainAll(urls);
            }
        }

        /*if (commonURLs == null || commonURLs.isEmpty()){
            return null;
        }*/

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