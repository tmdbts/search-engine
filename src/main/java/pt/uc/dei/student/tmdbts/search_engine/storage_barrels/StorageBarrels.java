package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
public class StorageBarrels extends UnicastRemoteObject implements Gateway {

    int port = 32451;

    public StorageBarrels() throws RemoteException{
        super();
    }

    public String startingBarrels() throws RemoteException{
        System.out.println("Starting barrels!");

        return "Starting barrels!";
    }

    public static void main(String args[]){
        try{
            
        }
    }
}
