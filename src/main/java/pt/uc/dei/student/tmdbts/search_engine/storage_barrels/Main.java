package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {
        try {
            StorageBarrelsImpl storageBarrels = new StorageBarrelsImpl(args[0]);
        } catch (Exception e){
            System.out.println("Error in main: " + e.getMessage());
        }
    }
}
