package pt.uc.dei.student.tmdbts.search_engine.webserver;

import pt.uc.dei.student.tmdbts.search_engine.client.Client;
import pt.uc.dei.student.tmdbts.search_engine.client.Monitor;
import pt.uc.dei.student.tmdbts.search_engine.client.MonitorUpdate;
import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;

import java.io.FileInputStream;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

/**
 * This class represents the web server implementation.
 */
public class WebServerImpl extends UnicastRemoteObject implements Client {
    /**
     * Gateway
     */
    private Gateway gateway;

    /**
     * Status Monitor
     */
    private Monitor monitor;

    /**
     * Last searched query
     */
    String querySearch;

    /**
     * Constructor
     *
     * @throws RemoteException
     */
    public WebServerImpl() throws RemoteException {
        super();
    }

    /**
     * Connect to the gateway
     */
    public void connect() {
        String webServerName = "WebServer_V1";
        String rootPath = System.getProperty("user.dir");
        String appConfigPath = rootPath + "/app.properties";

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(appConfigPath));
            System.out.println(properties);
            gateway = (Gateway) Naming.lookup("rmi://" + properties.get("rmi_server_hostname") + ":" + properties.get("rmi_server_port") + "/server");
            gateway.registerForCallback(this.hashCode(), this);
        } catch (Exception e) {
            System.out.println("Error on WebServer: " + e.getMessage());
        }

        System.out.println("WebServer " + webServerName + " started");
    }

    /**
     * Search by query
     *
     * @param query query
     * @return search result
     * @throws RemoteException
     */
    public SearchResult searchQuery(String query) throws RemoteException {
        querySearch = query;
        return gateway.searchQuery(query);
    }

    /**
     * Search by query and page
     *
     * @param index index
     * @return search result
     * @throws RemoteException
     */
    public SearchResult searchQuery(int index) throws RemoteException {
        return gateway.searchQuery(querySearch, index);
    }

    /**
     * Add URL to the index queue
     *
     * @param url url
     * @throws RemoteException
     */
    public void addURL(URI url) throws RemoteException {
        gateway.addURL(url);
    }

    /**
     * Update the monitor
     *
     * @param monitorUpdate monitor update
     * @throws RemoteException
     */
    @Override
    public void updateMonitor(MonitorUpdate monitorUpdate) throws RemoteException {
        monitorUpdate.updateMonitor(monitor);
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    /**
     * The main method
     *
     * @param args args
     * @throws RemoteException
     */
    public static void main(String[] args) throws RemoteException {
        try {
            WebServerImpl webServer = new WebServerImpl();
        } catch (RemoteException e) {
            System.out.println("Error on WebServer CRITICAL: " + e.getMessage());
        }
    }
}
