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

public class StorageBarrelsImpl extends UnicastRemoteObject implements StorageBarrels, GatewayCallback {
    private Thread listenerThread;

    private CommunicationHandler commHandler;

    private Index index;

    private List<URIInfo> urlInformation = new LinkedList<>();

    private HashMap<String, Integer> termSearchFrequency = new HashMap<>();

    private ArrayList<String> recentSearches = new ArrayList<>();

    private Message message;

    private ArrayList<String> namesList;

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

            Path path = Path.of("./persistence/index.txt");
            index.setIndex(FileReadWriter.readData(path.toString()));

            startListening();
        } catch (IOException e) {
            System.out.println("Error loading app properties: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error initializing multicast protocol: " + e.getMessage());
        }
    }

    private void startListening() {
        listenerThread = new Thread(new MessageListener(commHandler, this));
        listenerThread.start();
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
                index.handleWordList(message);
                System.out.println("HANDLE WORD LIST " + message.getType() + "\n\n");
                break;

            case URL_LIST:
                index.handleURLList(message);
                System.out.println("HANDLE URL LIST " + message.getType() + "\n\n");
                break;

            case META_DATA:
                urlInformation.add(index.handleMetaData(message));
                System.out.println("AQUUIII " + index.handleMetaData(message).toString());
                System.out.println("HANDLE META DATA " + message.getType() + "\n\n");
                ;
                break;
            default:
                System.out.println("Invalid message type");
        }

    }


    public void printOnBarrel(String s) throws RemoteException {
        System.out.println("> " + s);
    }

    @Override
    public SearchResult search(String query) throws RemoteException {
        List<URI> indexResults = index.handleQuery(query);
        SearchResult searchResult = new SearchResult();

        for (URI uri : indexResults) {
            for (URIInfo uriInfo : urlInformation) {
                if (uriInfo.getUri().compareTo(uri)>=0) {
                    System.out.println("Bota carvao");
                    searchResult.addInfo(uriInfo);
                    System.out.println("botou lhe");
                }
                System.out.println("1\n");
            }
            System.out.println("2\n");
        }

        System.out.println("Here " + searchResult.getResults().toString());

        return searchResult;
    }

    public void notifyNewDataAvailable(String barrelName, String message) throws RemoteException {
        System.out.println("Notificação recebida para " + barrelName + ": " + message);
    }

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
