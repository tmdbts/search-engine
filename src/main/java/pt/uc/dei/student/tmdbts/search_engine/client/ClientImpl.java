package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.Scanner;

public class ClientImpl extends UnicastRemoteObject {

    ClientImpl() throws RemoteException {
        super();
    }


    public static void main(String args[]) {
        String rootPath = System.getProperty("user.dir");
        String appConfigPath = rootPath + "/app.properties";

        int location = 0;

        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            System.out.println("Error loading app properties: " + e.getMessage());
        }


        try (Scanner scanner = new Scanner(System.in)) {
            Gateway gateway = (Gateway) Naming.lookup("rmi://" + appProps.get("rmi_server_hostname") + ":" + appProps.get("rmi_server_port") + "/server");

            System.out.println("Welcome to Search Engine!");

            while (true) {
                System.out.print("Enter your query '->', '>' to get more results\nURL to index '!',\nlist URL's that point to a specifi URL '$' or\nopen Admin page 'search' :");

                String query = scanner.nextLine();

                if (query.startsWith("!")) {
                    query = query.substring(1);
                    URI url = new URI(query);
                    gateway.addURL(url);
                    System.out.println("URL requested for indexing: " + query);

                } else if (query.startsWith("search")) {

                    admin(gateway);

                } else if (query.startsWith("->")) {
                    location = 0;
                    
                    String result = "";

                    result = gateway.searchQuery(query.substring(2));

                    System.out.println("Search results: \n" + result);

                } else if (query.startsWith("$")) {
                    URI url = new URI(query.substring(1));

                    System.out.println("URL results: " + gateway.searchURL(url).toString());
                } else if (query.startsWith(">")) {
                    location += 10;
                    gateway.giveMore10(location);
                } else {
                    System.out.println("Unknown query: " + query);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void admin(Gateway gateway) {
        try {
            String info = gateway.admin();

            System.out.println(info);
        } catch (RemoteException e) {
            System.out.println("Error getting admin info: " + e.getMessage());
        }
    }
}
