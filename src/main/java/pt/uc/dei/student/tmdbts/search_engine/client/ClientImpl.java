package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;

import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

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


    public String verifyURLs(HashMap<String, ArrayList<URI>> veriifyURL){

        List<URI> commonURLs = null;

        for (List<URI> urls : veriifyURL.values()){
            if (commonURLs == null){
                commonURLs = new ArrayList<>(urls);
            }
            else {
                commonURLs.retainAll(urls);
            }
        }

        if (commonURLs == null || commonURLs.isEmpty()){
            return "";
        }

        return commonURLs.stream().map(URI::toString).collect(Collectors.joining(", "));
    }

    public String handleQuery(HashMap<String, ArrayList<URI>> queryResults){

        return verifyURLs(queryResults);
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
                    ClientImpl client = new ClientImpl();
                    String result = client.handleQuery(gateway.search(query));
                    System.out.println("Search results: \n" + result);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
