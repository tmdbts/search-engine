package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayCallback;
import pt.uc.dei.student.tmdbts.search_engine.protocol.CommunicationHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class StorageBarrelsImpl extends UnicastRemoteObject implements StorageBarrels, GatewayCallback {
    private Thread listenerThread;
    private MessageListener messageListener;
    private CommunicationHandler commHandler;
    private Index index;
    private String message;

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

    void sendMessage(String message) {
        index.handleMessage(message);
    }

    public void printOnBarrel(String s) throws RemoteException {
        System.out.println("> " + s);
    }

    @Override
    public HashMap<String, ArrayList<URI>> search(String query) throws RemoteException {
        HashMap<String, ArrayList<URI>> indexResults = index.handleQuery(query);

        System.out.println("AQUI " + indexResults);

        return indexResults;
    }

    public void notifyNewDataAvailable(String barrelName, String message) throws RemoteException {
        System.out.println("Notificação recebida para " + barrelName + ": " + message);
    }

    /**
     * TODO
     * Gets the top 10 searches
     *
     * @return the top 10 searches
     */
    public String getTopSearches() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Top 10 searches:\n");

        return stringBuilder.toString();
    }
}
