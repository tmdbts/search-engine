package pt.uc.dei.student.tmdbts.search_engine.client;

import java.net.URI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Client extends Remote {
    public String handleQuery(HashMap<String, ArrayList<URI>> queryResults) throws RemoteException;
}
