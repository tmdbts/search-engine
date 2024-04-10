package pt.uc.dei.student.tmdbts.search_engine.gateway;

import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.StorageBarrels;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.URIInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * GatewayImpl class
 * <p>
 * The GatewayImpl class is responsible for the Gateway implementation.
 * It is responsible for managing the queue of URLs to be indexed, the barrels and the callbacks.
 * It also provides methods to add URLs to the queue, get the next URL from the queue, check if the queue is empty,
 * get the size of the queue, print the queue, search by a query, print the information panel, register a barrel for
 * callbacks, unregister a barrel for callbacks and print a message on the server.
 */
public class GatewayImpl extends UnicastRemoteObject implements Gateway {
    /**
     * Queue of URLs to be indexed. ConcurrentLinkedDeque is used to allow multiple threads to access the queue.
     */
    private ConcurrentLinkedDeque<URI> queue = new ConcurrentLinkedDeque<>();

    /**
     * HashMap of barrels. The key is the barrel name and the value is the StorageBarrels object.
     */
    static HashMap<String, StorageBarrels> barrels = new HashMap<>();

    /**
     * HashMap of callbacks. The key is the barrel name and the value is the GatewayCallback object.
     */
    private final HashMap<String, GatewayCallback> callbacks = new HashMap<>();

    private static StorageBarrels storageBarrels;
    private SearchResult searchResult;

    /**
     * Constructor
     *
     * @throws RemoteException if there is an error creating the object
     */
    public GatewayImpl() throws RemoteException {
        super();
    }

    /**
     * Register a barrel for callbacks
     *
     * @param barrelName           name of the barrel
     * @param callbackClientObject callback object
     * @throws RemoteException if there is an error registering the barrel
     */
    public void registerForCallback(String barrelName, GatewayCallback callbackClientObject) throws RemoteException {
        System.out.println("Storage Barrel " + barrelName + " registered for callbacks.");
        callbacks.put(barrelName, callbackClientObject);
    }

    /**
     * Unregister a barrel for callbacks
     *
     * @param barrelName name of the barrel
     * @throws RemoteException if there is an error unregistering the barrel
     */
    public void unregisterForCallback(String barrelName) throws RemoteException {
        if (callbacks.remove(barrelName) != null) {
            System.out.println("Storage Barrel " + barrelName + " unregistered for callbacks.");
        } else {
            System.out.println("No callback to unregister for " + barrelName);
        }
    }

    /**
     * Print a message on the server
     *
     * @param s message to print
     * @throws RemoteException if there is an error printing the message
     */
    public void printOnServer(String s) throws RemoteException {
        System.out.print("> " + s);
    }

    /**
     * When a barrel connects, print a message with the barrel name
     *
     * @param barrelName message to print
     * @param barrel     barrel object
     * @throws RemoteException if there is an error printing the message
     */
    public void barrel(String barrelName, StorageBarrels barrel) throws RemoteException {
        System.out.println("Barrel " + barrelName + " connected!");
        System.out.println("> ");
        barrels.put(barrelName, barrel);
    }

    /**
     * Add a URL to the queue
     *
     * @param url URL to add
     */
    public void addURL(URI url) {
        try {
            queue.add(url);
        } catch (Exception e) {
            System.out.println("Exception adding to queue in GatewayImpl.main: " + e);
        }
    }

    /**
     * Add a list of URLs to the queue
     *
     * @param urls list of URLs to add
     */
    public void addURL(List<URI> urls) {
        queue.addAll(urls);
    }

    /**
     * Poll the next URL from the queue
     *
     * @return URI of the next URL
     */
    public URI getURL() {
        return queue.pollFirst();
    }

    /**
     * Check if the queue is empty
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    /**
     * Get the size of the queue
     *
     * @return size of the queue
     */
    public int queueSize() {
        return queue.size();
    }

    /**
     * Print the queue
     */
    public void printQueue() {
        for (URI url : queue) {
            System.out.println(url);
        }
    }

    /**
     * Search by a query
     *
     * @param query query to search
     * @return search result
     * @throws RemoteException if there is an error searching
     */
    public String searchQuery(String query) throws RemoteException {
        System.out.println("Searching results for the requested query: " + query);

        List<URIInfo> result;

        long startTime = System.nanoTime();

        searchResult = barrels.get("test").searchQuery(query);

        System.out.println("Search REsult test " + searchResult.getResults());

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1_000_000;

        searchResult.setQueryTime(duration);

        result = searchResult.return10(0);

        String stringResult = convertToString(result); //chega vazio

        System.out.println("AQUI " + stringResult);

        return stringResult;
    }

    public String giveMore10(int index) {
        List<URIInfo> result = searchResult.return10(index);

        return convertToString(result);
    }


    private static String convertToString(List<URIInfo> result) {

        String resultString = "";

        for (int i = 0; i < result.size(); i++) {

            resultString = "URL-> " + result.get(i).getUri().toString() + "\n" + "Title-> " + result.get(i).getTitle() + "\n" + "Description-> " + result.get(i).getDescription() + "\n";

            System.out.println("a");
        }

        return resultString;
    }

    public List<URI> searchURL(URI url) throws RemoteException {

        return barrels.get("test").searchURL(url);
    }

    /**
     * Print the information panel
     *
     * @return information panel
     */
    public String admin() {
        StringBuilder stringBuilder = new StringBuilder();

        System.out.println("B:" + getBarrels());

        stringBuilder.append(getBarrels());

        try {
            stringBuilder.append(barrels.get("test").getTopSearches());
        } catch (RemoteException e) {
            System.out.println("Error getting top searches: " + e.getMessage());
        }

        return stringBuilder.toString();
    }

    /**
     * Get the barrels information
     *
     * @return barrels information
     */
    private String getBarrels() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Active barrels:\n");

        for (String key : barrels.keySet()) {
            stringBuilder.append(key).append("\n");
        }

        return stringBuilder.toString();
    }

    public static void main(String args[]) {
        String rootPath = System.getProperty("user.dir");
        String appConfigPath = rootPath + "/app.properties";

        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            System.out.println("Error loading app properties: " + e.getMessage());
        }

        String input;
        int rmiPort = Integer.parseInt(appProps.getProperty("rmi_server_port"));

        try (Scanner sc = new Scanner(System.in)) {
            GatewayImpl gatewayImpl = new GatewayImpl();
            LocateRegistry.createRegistry(rmiPort);
            Naming.rebind("rmi://" + appProps.get("rmi_server_hostname") + ":" + rmiPort + "/server", gatewayImpl);
            System.out.println("Gateway is ready!");

            while (true) {
                System.out.println("> ");
                input = sc.nextLine();
                for (String key : barrels.keySet()) {
                    barrels.get(key).printOnBarrel(input);
                }
            }
        } catch (RemoteException re) {
            System.out.println("Exception in GatewayImpl.main: " + re);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException in GatewayImpl.main: " + e);
        }
    }
}
