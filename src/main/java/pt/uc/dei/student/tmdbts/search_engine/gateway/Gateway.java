package pt.uc.dei.student.tmdbts.search_engine.gateway;

import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.StorageBarrels;

import java.net.URI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Gateway extends Remote {
    void printOnServer(String s) throws RemoteException;

    void barrel(String barrelName, StorageBarrels barrel) throws RemoteException;

    void registerForCallback(String barrelName, GatewayCallback callbackClientObject) throws RemoteException;

    void unregisterForCallback(String barrelName) throws RemoteException;

    HashMap<String, ArrayList<URI>> search(String query) throws RemoteException;

    void addURL(URI url) throws RemoteException;

    void addURL(List<URI> urls) throws RemoteException;

    URI getURL() throws RemoteException;

    boolean isQueueEmpty() throws RemoteException;

    int size() throws RemoteException;
}
