package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayCallback;
import pt.uc.dei.student.tmdbts.search_engine.protocol.SEProtocol;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class StorageBarrelsImpl extends UnicastRemoteObject implements StorageBarrels, GatewayCallback {

    private SEProtocol protocol;

    StorageBarrelsImpl() throws RemoteException{
        super();
        try {
            protocol = new SEProtocol();
            startListening();
        } catch (Exception e){
            System.err.println("Error initializing multicast protocol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startListening(){
        Thread listenerThread = new Thread(new MessageListener());
        listenerThread.start();
    }

    private class MessageListener implements Runnable{
        public void run(){
            try {
                while (true){
                    String message = protocol.receiveMessage();
                    System.out.println("Recived message: " + message);
                }
            } catch (Exception e){
                System.out.println("Error while listening for multicast messages: " + e);
                e.printStackTrace();
            }
        }
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

    public static void main(String args[]){
        String a;

        try(Scanner sc = new Scanner(System.in)){

            Gateway gateway = (Gateway) Naming.lookup("rmi://localhost:32450/server");
            StorageBarrelsImpl storageBarrelsImpl = new StorageBarrelsImpl();
            gateway.barrel(args[0], storageBarrelsImpl);
            System.out.println("Barrel " + args[0] + " sent a connection to server");
            gateway.registerForCallback(args[0], storageBarrelsImpl);

            while (true){
                System.out.println("> ");
                a = sc.nextLine();
                gateway.printOnServer(a);
            }

        }
        catch (Exception e){
            System.out.println("Exception in main Barrels: " + e);
        }
    }
}
