package pt.uc.dei.student.tmdbts.search_engine.gateway;

import pt.uc.dei.student.tmdbts.search_engine.client.Client;
import pt.uc.dei.student.tmdbts.search_engine.client.Monitor;
import pt.uc.dei.student.tmdbts.search_engine.client.MonitorUpdate;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.StorageBarrels;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * GatewayImpl class
 * <p>
 * The GatewayImpl class is responsible for the Gateway implementation.
 * It is responsible for managing the queue of URLs to be indexed, the barrels
 * and the callbacks.
 * It also provides methods to add URLs to the queue, get the next URL from the
 * queue, check if the queue is empty,
 * get the size of the queue, print the queue, search by a query, print the
 * information panel, register a barrel for
 * callbacks, unregister a barrel for callbacks and print a message on the
 * server.
 */
public class GatewayImpl extends UnicastRemoteObject implements Gateway {
    /**
     * Queue of URLs to be indexed. ConcurrentLinkedDeque is used to allow multiple
     * threads to access the queue.
     */
    private ConcurrentLinkedDeque<URI> queue = new ConcurrentLinkedDeque<>();

    /**
     * HashMap of barrels. The key is the barrel name and the value is the
     * StorageBarrels object.
     */
    private static HashMap<String, StorageBarrels> barrels = new HashMap<>();

    /**
     * HashMap of clients. The key is the client id and the value is the Client
     * object.
     */
    private static HashMap<Integer, Client> clients = new HashMap<>();

    /*
     * HashMap of callbacks. The key is the barrel name and the value is the
     * GatewayCallback object.
     */
    private final HashMap<String, GatewayCallback> callbacks = new HashMap<>();

    /**
     * Top ten searches
     */
    private TopTenSearches topTenSearches = new TopTenSearches();

    /**
     * Average response time
     */
    private long averageResponseTime = 0;

    /**
     * Search counter
     */
    private int searchCounter = 0;

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
     * @param name                 name of the barrel
     * @param callbackClientObject callback object
     * @throws RemoteException if there is an error registering the barrel
     */
    public void registerForCallback(String name, GatewayCallback callbackClientObject) throws RemoteException {
        System.out.println("Server " + name + " registered for callbacks.");
        callbacks.put(name, callbackClientObject);
    }

    /**
     * Unregister a barrel for callbacks
     *
     * @param name name of the barrel
     * @throws RemoteException if there is an error unregistering the barrel
     */
    public void unregisterForCallback(String name) throws RemoteException {
        if (callbacks.remove(name) != null) {
            System.out.println("Storage Barrel " + name + " unregistered for callbacks.");
        } else {
            System.out.println("No callback to unregister for " + name);
        }
    }

    /**
     * Register a client for callbacks
     *
     * @param id     client id
     * @param client client object
     * @throws RemoteException if there is an error registering the client
     */
    public void registerForCallback(int id, Client client) throws RemoteException {
        clients.put(id, client);

        System.out.println("Client " + id + " registered for callbacks.");
    }

    /**
     * Unregister a client for callbacks
     *
     * @param id client id
     * @throws RemoteException if there is an error unregistering the client
     */
    public void unregisterForCallback(int id) throws RemoteException {
        if (clients.remove(id) != null) {
            System.out.println("Client " + id + " unregistered for callbacks.");
        } else {
            System.out.println("No callback to unregister for " + id);
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

        ArrayList<String> activeBarrelsNames = new ArrayList<>(barrels.keySet());

        MonitorUpdate monitor = new MonitorUpdate(activeBarrelsNames, averageResponseTime);
    }

    /**
     * Get system information
     * <p>
     * The included information is the top 10 searches, active barrels and the average response time
     *
     * @return system information
     * @throws RemoteException if there is an error getting the system information
     */
    @Override
    public Monitor getMonitor() throws RemoteException {
        ArrayList<String> activeBarrelsNames = new ArrayList<>(barrels.keySet());

        Monitor test = new Monitor(topTenSearches, activeBarrelsNames, averageResponseTime);

        System.out.println(test);

        return test;
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
    public SearchResult searchQuery(String query) throws RemoteException {
        System.out.println("Searching results for the requested query: " + query);

        long startTime = System.nanoTime();

        SearchResult searchResult = barrels.get("test").searchQuery(query);

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1_000_000;

        searchResult.setQueryTime(duration);

        averageResponseTime = (averageResponseTime * (searchCounter++) + duration) / searchCounter;

        topTenSearches.addSearchQueryFrequency(query);

//        HashMap<Integer, String> resultTop = topTenSearches.didTopChange();
//
//        MonitorUpdate monitor = new MonitorUpdate(topTenSearches, averageResponseTime);
//
//        System.out.println("Sending monitor update to clients");
//
//        if (resultTop != null) {
//            for (Map.Entry<Integer, Client> entry : clients.entrySet()) {
//                Integer id = entry.getKey();
//                Client client = entry.getValue();
//
//                System.out.println("Sending monitor update to client " + id);
//                client.updateMonitor(monitor);
//                System.out.println("Monitor update sent to client " + id);
//            }
//        }

        return searchResult;
    }

    /**
     * Search by a query and offset
     *
     * @param query  query to search
     * @param offset offset
     * @return search result
     * @throws RemoteException if there is an error searching
     */
    public SearchResult searchQuery(String query, int offset) throws RemoteException {
        System.out.println("Searching results for the requested query: " + query + " with offset: " + offset);

        long startTime = System.nanoTime();

        SearchResult searchResult = barrels.get("test").searchQuery(query, offset);

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1_000_000;

        searchResult.setQueryTime(duration);

        averageResponseTime = (averageResponseTime * (searchCounter++) + duration) / searchCounter;

        topTenSearches.addSearchQueryFrequency(query);

//        HashMap<Integer, String> resultTop = topTenSearches.didTopChange();

//        MonitorUpdate monitor = new MonitorUpdate(topTenSearches, averageResponseTime);

//        if (resultTop != null) {
//            for (Client client : clients.values()) {
//                client.updateMonitor(monitor);
//            }
//        }

        return searchResult;
    }

    /**
     * Search by URL
     *
     * @param url URL to search
     * @return list of URIs
     * @throws RemoteException if there is an error searching
     */
    public List<URI> searchURL(URI url) throws RemoteException {
        return barrels.get("test").searchURL(url);
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
