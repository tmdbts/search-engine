package pt.uc.dei.student.tmdbts.search_engine.gateway;

import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.StorageBarrels;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Scanner;

public class GatewayImpl extends UnicastRemoteObject implements Gateway{

    static HashMap<String, StorageBarrels> barrels = new HashMap<>();
    private final HashMap<String, GatewayCallback> callbacks = new HashMap<>();

    public GatewayImpl() throws RemoteException {
        super();
    }

    public void registerForCallback(String barrelName, GatewayCallback callbackClientObject) throws RemoteException {
        System.out.println("Storage Barrel " + barrelName + " registered for callbacks.");
        callbacks.put(barrelName, callbackClientObject);
    }

    public void unregisterForCallback(String barrelName) throws RemoteException {
        if(callbacks.remove(barrelName) != null) {
            System.out.println("Storage Barrel " + barrelName + " unregistered for callbacks.");
        } else {
            System.out.println("No callback to unregister for " + barrelName);
        }
    }

    public void printOnServer(String s) throws RemoteException{
        System.out.println("> " + s);
    }

    public void barrel(String barrelName, StorageBarrels barrel) throws RemoteException{
        System.out.println("Barrel " + barrelName + " connected!");
        System.out.println("> ");
        barrels.put(barrelName, barrel);
    }

    public String startingServer() throws RemoteException{
        System.out.println("Server On!");

        return "Server On!";
    }

    public void indexURL(String url) throws RemoteException {
        System.out.println("Recebido pedido para indexar o URL: " + url);
    }

    public String search(String query) throws RemoteException {
        System.out.println("Search requested for query: " + query);

        String mockResult = "Resultados para '" + query + "': [resultado1, resultado2]";
        return mockResult;
    }


    public static void main(String args[]){
        String a;
        int rmiPort = 32450;

        try (Scanner sc = new Scanner(System.in)){
            GatewayImpl gatewayImpl = new GatewayImpl();
            LocateRegistry.createRegistry(rmiPort);
            Naming.rebind("rmi://localhost:32450/server", gatewayImpl);
            System.out.println("Gateway is ready!");

            while (true){
                System.out.println("> ");
                a = sc.nextLine();
                for (String key : barrels.keySet()){
                    barrels.get(key).printOnBarrel(a);
                }
            }

        } catch (RemoteException re){
            System.out.println("Exception in GatewayImpl.main: " + re);
        } catch (MalformedURLException e){
            System.out.println("MalformedURLException in GatewayImpl.main: " + e);
        }
    }
}
