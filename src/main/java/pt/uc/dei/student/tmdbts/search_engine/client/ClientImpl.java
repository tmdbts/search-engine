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
public class ClientImpl extends UnicastRemoteObject implements Client {
    private Monitor monitor;

    private String lastQuery = "";

    private int lastOffset = 0;

    protected ClientImpl() throws RemoteException {
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
     * Get the admin info.
     *
     * @param gateway gateway
     */
    public void admin(Gateway gateway) {
        Scanner scanner = new Scanner(System.in);

        try {
            monitor = gateway.getMonitor();

            do {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                System.out.println("Active Barrels: " + monitor.getActiveBarrels());
                System.out.println("Average Response Time: " + monitor.getAverageResponseTime());
                System.out.println("Top Ten Searches: " + monitor.getTopTenSearches().getTop10Searches());
                System.out.println("Press enter to refresh the monitor");
                System.out.println("Press q to exit");

                String input = scanner.nextLine();
                scanner.reset();

                if (input.equals("q")) {
                    break;

                }

                if (input.equals("r")) {
                    monitor = gateway.getMonitor();
                }
            } while (true);

        } catch (RemoteException e) {
            System.out.println("Error getting admin info: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        String rootPath = System.getProperty("user.dir");
        String appConfigPath = rootPath + "/app.properties";
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

                String input = scanner.nextLine();

                if (input.startsWith("!")) {
                    input = input.substring(1);
                    URI url = new URI(input);
                    gateway.addURL(url);
                    System.out.println("URL requested for indexing: " + input);

                } else if (input.startsWith("search://status")) {

                    client.admin(gateway);

                } else if (input.startsWith("->")) {
                    String query = input.substring(2);
                    SearchResult result = gateway.searchQuery(query);

                    client.setLastQuery(query);
                    client.setLastOffset(0);

                    System.out.println("Search results: \n" + ClientImpl.convertToString(result));
                } else if (input.startsWith("$")) {
                    URI url = new URI(input.substring(1));

                    System.out.println("URL results: " + gateway.searchURL(url).toString());
                } else if (input.startsWith(">")) {
                    SearchResult result = gateway.searchQuery(client.getLastQuery(), client.getLastOffset() + 10);

                    client.setLastOffset(client.getLastOffset() + 10);

                    System.out.println("Search results:");
                    System.out.println(ClientImpl.convertToString(result));
                } else {
                    System.out.println("Unknown query: " + input);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateMonitor(MonitorUpdate monitorUpdate) {
        System.out.println("Updating monitor");
        monitorUpdate.updateMonitor(monitor);
    }


    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public String getLastQuery() {
        return lastQuery;
    }

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    public int getLastOffset() {
        return lastOffset;
    }

    public void setLastOffset(int lastOffset) {
        this.lastOffset = lastOffset;
    }
}
