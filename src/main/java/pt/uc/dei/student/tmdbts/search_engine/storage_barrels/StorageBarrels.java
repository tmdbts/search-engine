package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.rmi.*;

public interface StorageBarrels extends Remote {
    public void printOnBarrel(String s) throws java.rmi.RemoteException;

    public void search(String query) throws java.rmi.RemoteException;
}
