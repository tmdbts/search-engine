package pt.uc.dei.student.tmdbts.search_engine.gateway;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.StorageBarrels;
import java.rmi.*;

public interface Gateway extends Remote {
    void printOnServer(String s) throws RemoteException;
    void barrel(String barrelName, StorageBarrels barrel) throws RemoteException;
    String startingServer() throws RemoteException;
    void registerForCallback(String barrelName, GatewayCallback callbackClientObject) throws RemoteException;
    void unregisterForCallback(String barrelName) throws RemoteException;
    void indexURL(String url) throws RemoteException;
    String search(String query) throws RemoteException;
}
