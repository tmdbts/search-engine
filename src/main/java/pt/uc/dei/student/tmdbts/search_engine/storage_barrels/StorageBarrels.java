package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.net.URI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StorageBarrels extends Remote {
    void printOnBarrel(String s) throws java.rmi.RemoteException;

    String searchQuery(String query) throws java.rmi.RemoteException;

    List<URI> searchURL(URI url) throws java.rmi.RemoteException;

    String getTopSearches() throws RemoteException;
}
