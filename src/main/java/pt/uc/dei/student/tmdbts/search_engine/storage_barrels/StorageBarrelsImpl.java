package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayCallback;
import pt.uc.dei.student.tmdbts.search_engine.protocol.CommunicationHandler;
import pt.uc.dei.student.tmdbts.search_engine.protocol.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Class that represents the storage barrels
 * <p>
 * The storage barrels are the components that store the index and the URLs.
 * They also handle the search requests.
 * The storage barrels are connected to the server through the Gateway.
 * They also listen for messages that come from the multicast protocol.
 * The storage barrels are also responsible for storing the search frequency of a term.
 * The storage barrels also store the recent searches.
 * The storage barrels are also responsible for storing the URL information.
 */
public class StorageBarrelsImpl extends UnicastRemoteObject implements StorageBarrels, GatewayCallback {
    /**
     * Thread that listens for messages that come from the multicast protocol
     */
    private Thread listenerThread;

    /**
     * Communication handler to send messages
     */
    private CommunicationHandler commHandler;

    /**
     * Index of the storage barrel
     */
    private Index index;
    /**
     * Url List of storage barrel
     */
    private HashMap<URI, List<URI>> urlList;

    /**
     * List of URL information
     */
    private ArrayList<URIInfo> urlInformation = new ArrayList<>();

    /**
     * Map that stores the search frequency of a term
     */
    private HashMap<String, Integer> termSearchFrequency = new HashMap<>();


    /**
     * Constructor
     *
     * @param barrelName Name of the barrel
     * @throws RemoteException If there is an error creating the object
     */

    StorageBarrelsImpl(String barrelName) throws RemoteException {
        super();

        String rootPath = System.getProperty("user.dir");
        String appConfigPath = rootPath + "/app.properties";

        Properties appProps = new Properties();

        try {
            appProps.load(new FileInputStream(appConfigPath));

            this.index = new Index();
            Gateway gateway = (Gateway) Naming.lookup("rmi://" + appProps.get("rmi_server_hostname") + ":" + appProps.get("rmi_server_port") + "/server");
            gateway.barrel(barrelName, this);
            System.out.println("Barrel " + barrelName + " sent a connection to server");
            gateway.registerForCallback(barrelName, this);

            commHandler = new CommunicationHandler();

            Path pathIndex = Path.of("./persistence/index.txt");
            index.setIndex(FileReadWriter.readData(pathIndex.toString()));

            urlList = new HashMap<>();

            Path pathUrlList = Path.of("./persistence/url_list.txt");

            urlList = FileReadWriter.readUrls(pathUrlList.toString());

            startListening();

            listenerThread.join();
        } catch (IOException e) {
            System.out.println("Error loading app properties: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error initializing multicast protocol: " + e.getMessage());
        }
    }

    /**
     * Start listening for messages
     */
    private void startListening() {
        listenerThread = new Thread(new MessageListener(commHandler, this));
        listenerThread.start();
    }

    public void handleMessage(String inComingMessage) {
        Message message = new Message(inComingMessage);

        message.parseMessage(inComingMessage);

        if (message.getType() == null) {
            System.out.println("Invalid message type");
        }

        switch (message.getType()) {
            case WORD_LIST:
                index.handleWordList(message);

                break;

            case URL_LIST:
                index.handleURLList(message);

                break;

            case META_DATA:
                urlInformation.add(index.handleMetaData(message));

                break;
            default:
                System.out.println("Invalid message type");
        }

    }

    /**
     * Print a message on the barrels
     *
     * @param s Term to add
     */
    public void printOnBarrel(String s) throws RemoteException {
        System.out.println("> " + s);
    }

    /**
     * Search by query
     *
     * @param query Term to add
     */
    @Override
    public SearchResult searchQuery(String query) throws RemoteException {
        List<URI> indexResults = index.handleQuery(query);
        SearchResult searchResult = new SearchResult();

        for (URI result : indexResults) {
            if (searchResult.getResults().size() >= 10) {
                break;
            }

            for (URIInfo uriInfo : urlInformation) {
                if (uriInfo.getUri().equals(result)) {
                    searchResult.addInfo(uriInfo);

                    break;
                }
            }
        }

        return searchResult;
    }

    /**
     * Search by query
     *
     * @param query  Term to add
     * @param offset Offset
     */
    public SearchResult searchQuery(String query, int offset) throws RemoteException {
        List<URI> indexResults = index.handleQuery(query);
        SearchResult searchResult = new SearchResult();

        for (URI result : indexResults.subList(offset, indexResults.size())) {
            if (searchResult.getResults().size() >= 10) {
                break;
            }

            for (URIInfo uriInfo : urlInformation) {
                if (uriInfo.getUri().equals(result)) {
                    searchResult.addInfo(uriInfo);

                    break;
                }
            }
        }

        return searchResult;
    }

    public List<URI> searchURL(URI url) throws RemoteException {
        return urlList.get(url);
    }

    public void notifyNewDataAvailable(String barrelName, String message) throws RemoteException {
        System.out.println("Notificação recebida para " + barrelName + ": " + message);
    }

    /**
     * Get the top 10 searches
     */
    public String getTopSearches() {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(termSearchFrequency.entrySet());

        entries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        StringBuilder result = new StringBuilder("Top 10 keys with highest values:\n");

        int count = 0;
        for (Map.Entry<String, Integer> entry : entries) {
            if (count >= 10) {
                break;
            }

            result.append(entry.getKey()).append("\n");
            count++;
        }

        return result.toString();
    }
}
