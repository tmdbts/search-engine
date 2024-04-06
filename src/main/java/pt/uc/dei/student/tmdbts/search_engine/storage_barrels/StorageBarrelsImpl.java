package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayCallback;
import pt.uc.dei.student.tmdbts.search_engine.protocol.CommunicationHandler;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
    public void search(String query) throws RemoteException {

        return;
    }

    public void notifyNewDataAvailable(String barrelName, String message) throws RemoteException {
        System.out.println("Notificação recebida para " + barrelName + ": " + message);
    }
}
