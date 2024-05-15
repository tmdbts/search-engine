package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.URIInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Client implementation
 */
public class ClientImpl extends UnicastRemoteObject implements Client {
    Monitor monitor;

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

    @Override
    public void updateMonitor(MonitorUpdate monitorUpdate) {
        monitorUpdate.updateMonitor(monitor);
    }
}
