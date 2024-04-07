package pt.uc.dei.student.tmdbts.search_engine.gateway;

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

public class GatewayImpl extends UnicastRemoteObject implements Gateway {
    private ConcurrentLinkedDeque<URI> queue = new ConcurrentLinkedDeque<>();
    static HashMap<String, StorageBarrels> barrels = new HashMap<>();
    private final HashMap<String, GatewayCallback> callbacks = new HashMap<>();
    private static StorageBarrels storageBarrels;

    public GatewayImpl() throws RemoteException {
        super();
    }

    public void registerForCallback(String barrelName, GatewayCallback callbackClientObject) throws RemoteException {
        System.out.println("Storage Barrel " + barrelName + " registered for callbacks.");
        callbacks.put(barrelName, callbackClientObject);
    }

    public void unregisterForCallback(String barrelName) throws RemoteException {
        if (callbacks.remove(barrelName) != null) {
            System.out.println("Storage Barrel " + barrelName + " unregistered for callbacks.");
        } else {
            System.out.println("No callback to unregister for " + barrelName);
        }
    }

    public void printOnServer(String s) throws RemoteException {
        System.out.println("> " + s);
    }

    public void barrel(String barrelName, StorageBarrels barrel) throws RemoteException {
        System.out.println("Barrel " + barrelName + " connected!");
        System.out.println("> ");
        barrels.put(barrelName, barrel);
    }

    public void addURL(URI url) {
        try {
            queue.add(url);
        } catch (Exception e) {
            System.out.println("Exception adding to queue in GatewayImpl.main: " + e);
        }
    }

    public void addURL(List<URI> urls) {
        queue.addAll(urls);
    }

    public URI getURL() {
        return queue.pollFirst();
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void printQueue() {
        for (URI url : queue) {
            System.out.println(url);
        }
    }

    public SearchResult search(String query) throws RemoteException {

        System.out.println("Searching results for the requested query: " + query);

        return barrels.get("teste").search(query);
    }

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
