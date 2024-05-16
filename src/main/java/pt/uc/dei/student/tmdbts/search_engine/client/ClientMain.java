package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Scanner;

public class ClientMain {
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

        ClientImpl client = null;
        try {
            client = new ClientImpl();
        } catch (RemoteException e) {
            System.out.println("Error creating client: " + e.getMessage());
        }

        try (Scanner scanner = new Scanner(System.in)) {
            Gateway gateway = (Gateway) Naming.lookup("rmi://" + appProps.get("rmi_server_hostname") + ":" + appProps.get("rmi_server_port") + "/server");

            System.out.println("Welcome to Search Engine!");
            System.out.println();

            assert client != null;
            gateway.registerForCallback(client.hashCode(), client);

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

                    client.admin(gateway);

                } else if (query.startsWith("->")) {
                    location = 0;

                    String result = gateway.searchQuery(query.substring(2));

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
}
