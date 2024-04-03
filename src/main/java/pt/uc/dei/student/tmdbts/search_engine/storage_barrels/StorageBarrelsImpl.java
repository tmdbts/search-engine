package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayCallback;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class StorageBarrelsImpl extends UnicastRemoteObject implements StorageBarrels, GatewayCallback {
    StorageBarrelsImpl() throws RemoteException{
        super();
    }

    public void printOnBarrel(String s) throws RemoteException{
        System.out.println("> " +  s);
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
            System.out.println("Exxeption in main Barrels: " + e);
        }
    }
}
