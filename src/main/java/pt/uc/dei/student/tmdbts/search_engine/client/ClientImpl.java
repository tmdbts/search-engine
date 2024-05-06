package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.URIInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.Scanner;

/**
 * Client implementation
 */
public class ClientImpl extends UnicastRemoteObject {

    ClientImpl() throws RemoteException {
        super();
    }

    /**
     * Converts the search result to a string
     *
     * @param result search result
     * @return search result as a string
     */
    public static String convertToString(SearchResult result) {

        StringBuilder resultString = new StringBuilder();
        int counter = 1;

        for (URIInfo uriInfos : result.getResults()) {
            resultString.append(counter).append(":\nURL-> ").append(uriInfos.getUri().toString()).append("\nTitle-> ").append(uriInfos.getTitle()).append("\nDescription-> ").append(uriInfos.getDescription()).append("\n");

            counter++;
        }

        return resultString.toString();
    }

    /**
     * Main method
     * <p>
     * It starts the client and connects to the server. It then waits for user input.
     *
     * @param args arguments
     */
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
            System.out.println();

            while (true) {
                System.out.println("Search a query: ->");
                System.out.println("Get more results of a search: >");
                System.out.println("Index a URL: !");
                System.out.println("Search for a URL: $");
                System.out.println("Open Admin page: search://status");
                System.out.println();

                String query = scanner.nextLine();

                if (query.startsWith("!")) {
                    query = query.substring(1);
                    URI url = new URI(query);
                    gateway.addURL(url);
                    System.out.println("URL requested for indexing: " + query);

                } else if (query.startsWith("search://status")) {

                    admin(gateway);

                } else if (query.startsWith("->")) {
                    System.out.println("Search results: \n" + gateway.searchQuery(query.substring(2)));

                } else if (query.startsWith("$")) {
                    URI url = new URI(query.substring(1));

                    System.out.println("URL results: " + gateway.searchURL(url).toString());
                } else if (query.startsWith(">")) {
                    location += 10;
                    //gateway.giveMore10(location);
                } else {
                    System.out.println("Unknown query: " + query);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get the admin info.
     *
     * @param gateway gateway
     */
    private static void admin(Gateway gateway) {
        try {
            String info = gateway.admin();

            System.out.println(info);
        } catch (RemoteException e) {
            System.out.println("Error getting admin info: " + e.getMessage());
        }
    }
}
