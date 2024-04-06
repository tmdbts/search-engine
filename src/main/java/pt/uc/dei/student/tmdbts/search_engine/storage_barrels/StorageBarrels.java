package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.net.URI;
import java.rmi.*;
import java.util.ArrayList;
import java.util.HashMap;

public interface StorageBarrels extends Remote {
    public void printOnBarrel(String s) throws java.rmi.RemoteException;

    public HashMap<String, ArrayList<URI>> search(String query) throws java.rmi.RemoteException;
}
