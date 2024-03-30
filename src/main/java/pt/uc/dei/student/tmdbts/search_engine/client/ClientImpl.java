package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject implements  Client{

    ClientImpl() throws RemoteException{
        super();
    }

    public void printOnClient(String s) throws RemoteException{
        System.out.println("> " + s);
    }

    public static void main(String args[]) {
        try (Scanner sc = new Scanner(System.in)) {
            Gateway gateway = (Gateway) Naming.lookup("rmi://localhost:32450/Server");
            System.out.println(gateway.startingServer());

            while (true) {
                System.out.println("Enter command ('index' or 'search'): ");
                String command = sc.nextLine();
                if ("index".equalsIgnoreCase(command)) {
                    System.out.println("Enter URL to index: ");
                    String url = sc.nextLine();
                    gateway.indexURL(url);
                    System.out.println("URL requested for indexing: " + url);
                } else if ("search".equalsIgnoreCase(command)) {
                    System.out.println("Enter search query: ");
                    String query = sc.nextLine();
                    String result = gateway.search(query);
                    System.out.println("Search results: \n" + result);
                } else {
                    System.out.println("Unknown command.");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in main Client: " + e);
            e.printStackTrace();
        }
    }
}
