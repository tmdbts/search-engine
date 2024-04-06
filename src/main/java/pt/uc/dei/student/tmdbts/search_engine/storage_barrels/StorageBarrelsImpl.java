package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayCallback;
import pt.uc.dei.student.tmdbts.search_engine.protocol.CommunicationHandler;

import java.net.URI;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class StorageBarrelsImpl extends UnicastRemoteObject implements StorageBarrels, GatewayCallback {
    private Thread listenerThread;
    private MessageListener messageListener;
    private CommunicationHandler commHandler;
    private Index index;
    private String message;

    StorageBarrelsImpl(String barrelName) throws RemoteException {
        super();
        try {
            this.index = new Index();
            Gateway gateway = (Gateway) Naming.lookup("rmi://localhost:32450/server");
            gateway.barrel(barrelName, this);
            System.out.println("Barrel " + barrelName + " sent a connection to server");
            gateway.registerForCallback(barrelName, this);

            commHandler = new CommunicationHandler();

            Path path = Path.of("./persistence/index.txt");
            index.setIndex(FileReadWriter.readData(path.toString()));

            startListening();
        } catch (Exception e) {
            System.err.println("Error initializing multicast protocol: " + e.getMessage());
            e.printStackTrace();
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

        HashMap<String, ArrayList<URI>> indexResults = new HashMap<>();

        indexResults = index.handleQuery(query);

        System.out.println("AQUI " + indexResults);

        return indexResults;
    }

    public void notifyNewDataAvailable(String barrelName, String message) throws RemoteException {
        System.out.println("Notificação recebida para " + barrelName + ": " + message);
    }
}
