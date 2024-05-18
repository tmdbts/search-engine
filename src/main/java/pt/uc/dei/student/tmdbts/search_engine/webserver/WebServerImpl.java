package pt.uc.dei.student.tmdbts.search_engine.webserver;

import pt.uc.dei.student.tmdbts.search_engine.client.Client;
import pt.uc.dei.student.tmdbts.search_engine.client.MonitorUpdate;
import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayCallback;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;

import java.io.FileInputStream;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;


public class WebServerImpl extends UnicastRemoteObject implements Client, GatewayCallback {

    private Gateway gateway;

    public WebServerImpl() throws RemoteException {
        super();
    }

    public void connect() {

        String webServerName = "WebServer_V1";
        String rootPath = System.getProperty("user.dir");
        String appConfigPath = rootPath + "/app.properties";

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(appConfigPath));
            gateway = (Gateway) Naming.lookup("rmi://" + properties.get("rmi_server_hostname") + ":" + properties.get("rmi_server_port") + "/server");
            System.out.println("WebServer " + webServerName + " started");
            gateway.registerForCallback(webServerName, this);

        } catch (Exception e) {
            System.out.println("Error on WebServer: " + e.getMessage());
        }
    }

    public SearchResult searchQuery(String query) throws RemoteException {
        return gateway.searchQuery(query);
    }

    public void addURL(URI url) throws RemoteException {
        gateway.addURL(url);
    }

    public static void main(String[] args) throws RemoteException {
        try {
            WebServerImpl webServer = new WebServerImpl();
        } catch (RemoteException e) {
            System.out.println("Error on WebServer CRITICAL: " + e.getMessage());
        }
    }

    @Override
    public void updateMonitor(MonitorUpdate monitorUpdate) throws RemoteException {
    }

    @Override
    public void notifyNewDataAvailable(String name, String message) throws RemoteException {
    }
}
