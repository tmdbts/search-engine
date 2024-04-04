package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayCallback;
import pt.uc.dei.student.tmdbts.search_engine.protocol.SEProtocol;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StorageBarrelsImpl extends UnicastRemoteObject implements StorageBarrels, GatewayCallback {
    private Thread listenerThread;
    private SEProtocol protocol;

    StorageBarrelsImpl(String barrelName) throws RemoteException{
        super();
        try {
            Gateway gateway = (Gateway) Naming.lookup("rmi://localhost:32450/server");
            gateway.barrel(barrelName, this);
            System.out.println("Barrel " + barrelName + " sent a connection to server");
            gateway.registerForCallback(barrelName, this);

            protocol = new SEProtocol();
            startListening();
        } catch (Exception e){
            System.err.println("Error initializing multicast protocol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startListening(){
        listenerThread = new Thread(new MessageListener());
        listenerThread.start();
    }


    public void printOnBarrel(String s) throws RemoteException{
        System.out.println("> " +  s);
    }

    public void writeOnFile() throws RemoteException{

    }

    @Override
    public void search(String query) throws RemoteException {

        return;
    }

    public void notifyNewDataAvailable(String barrelName, String message) throws RemoteException {
        System.out.println("Notificação recebida para " + barrelName + ": " + message);
    }
}
