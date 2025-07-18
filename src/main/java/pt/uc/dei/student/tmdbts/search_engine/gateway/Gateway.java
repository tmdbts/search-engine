package pt.uc.dei.student.tmdbts.search_engine.gateway;

import pt.uc.dei.student.tmdbts.search_engine.client.Client;
import pt.uc.dei.student.tmdbts.search_engine.client.Monitor;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.StorageBarrels;

import java.net.URI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface that defines the methods that the Gateway must implement
 */
public interface Gateway extends Remote {
    void printOnServer(String s) throws RemoteException;

    void barrel(String barrelName, StorageBarrels barrel) throws RemoteException;

    void registerForCallback(String barrelName, GatewayCallback callbackClientObject) throws RemoteException;

    void unregisterForCallback(String barrelName) throws RemoteException;

    void registerForCallback(int id, Client client) throws RemoteException;

    void unregisterForCallback(int id) throws RemoteException;

    SearchResult searchQuery(String query) throws RemoteException;

    SearchResult searchQuery(String query, int offset) throws RemoteException;

    List<URI> searchURL(URI url) throws RemoteException;

    void addURL(URI url) throws RemoteException;

    void addURL(List<URI> urls) throws RemoteException;

    URI getURL() throws RemoteException;

    boolean isQueueEmpty() throws RemoteException;

    int queueSize() throws RemoteException;

    Monitor getMonitor() throws RemoteException;
}
