package pt.uc.dei.student.tmdbts.search_engine.webserver;

import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WebServer extends Remote {

    public SearchResult searchQuery(String query) throws RemoteException;

}
