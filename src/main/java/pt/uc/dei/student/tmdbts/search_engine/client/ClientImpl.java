package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientImpl extends UnicastRemoteObject {

    ClientImpl() throws RemoteException {
        super();
    }

    /* TODO LIST
     *
     *   Resultados ordenados por nº de ligacoes para cada pagina
     *
     *   Consultar lista de paginas com ligacoes especificas para uma pagina especifica
     *
     *   Agrupar resultados de pesquisa de 10 em 10
     *
     * */

    public List<URI> handleQuery(HashMap<String, ArrayList<URI>> queryResults){
        return verifyURLs(queryResults);
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


        try (Scanner scanner = new Scanner(System.in)) {
            Gateway gateway = (Gateway) Naming.lookup("rmi://" + appProps.get("rmi_server_hostname") + ":" + appProps.get("rmi_server_port") + "/server");

            System.out.println("Welcome to Search Engine!");

            while (true) {
                System.out.print("Enter your query or URL to index -> ");

                String query = scanner.nextLine();
                if (query.startsWith("https://")) {
                    URI url = new URI(query);
                    gateway.addURL(url);
                    System.out.println("URL requested for indexing: " + query);
                } else if (query.equals("search://admin")) {
                    admin(gateway);
                } else {
                    ClientImpl client = new ClientImpl();
                    List<URI> result = client.handleQuery(gateway.search(query));

                    System.out.println("Search results: \n" + result);
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
