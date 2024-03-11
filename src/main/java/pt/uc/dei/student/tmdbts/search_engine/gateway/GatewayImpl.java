package pt.uc.dei.student.tmdbts.search_engine.gateway;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class Server extends UnicastRemoteObject implements Gateway{

    int port = 32450;

    public Server() throws RemoteException {
        super();
    }

    public String startingServer() throws RemoteException{
        System.out.println("Server On!");
        return "Server On!";
    }

    public static void main(String args[]){
        try {
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(server.port);
            registry.rebind("Server", server);
            System.out.println(server.startingServer());
        } catch (RemoteException re){
            System.out.println("Exception in GatewayImpl.main: " + re);
        }
    }
}
