package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;

import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject {

    ClientImpl() throws RemoteException {
        super();
    }

    public static void main(String args[]) {
        try (Scanner sc = new Scanner(System.in)) {
            Gateway gateway = (Gateway) Naming.lookup("rmi://localhost:32450/server");

            while (true) {
                System.out.print("Welcome to Search Engine!\nEnter your query or URL to index -> ");
                String query = sc.nextLine();
                if (query.startsWith("https://")) {
                    URI url = new URI(query);
                    gateway.addURL(url);
                    System.out.println("URL requested for indexing: " + query);
                } else {
                    String result = gateway.search(query);
                    System.out.println("Search results: \n" + result);
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in main Client: " + e);
            e.printStackTrace();
        }
    }
}
