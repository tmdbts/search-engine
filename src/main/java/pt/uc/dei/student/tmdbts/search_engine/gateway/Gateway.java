package pt.uc.dei.student.tmdbts.search_engine.gateway;
import pt.uc.dei.student.tmdbts.search_engine.URL;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.StorageBarrels;
import java.rmi.*;

public interface Gateway extends Remote {
    void printOnServer(String s) throws RemoteException;
    void barrel(String barrelName, StorageBarrels barrel) throws RemoteException;
    void registerForCallback(String barrelName, GatewayCallback callbackClientObject) throws RemoteException;
    void unregisterForCallback(String barrelName) throws RemoteException;
    String search(String query) throws RemoteException;
    void addURL(URL url) throws RemoteException;
    URL getURL() throws RemoteException;
    boolean isEmpty() throws RemoteException;
    int size() throws RemoteException;
}
