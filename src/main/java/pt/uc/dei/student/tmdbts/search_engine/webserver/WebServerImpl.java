package pt.uc.dei.student.tmdbts.search_engine.webserver;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;
import pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayCallback;

import java.io.FileInputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class WebServerImpl extends UnicastRemoteObject implements WebServer, GatewayCallback {


    WebServerImpl(String webServerName) throws RemoteException {
        super();

        String rootPath = System.getProperty("user.dir");
        String appConfigPath = rootPath + "/app.properties";

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(appConfigPath));
            Gateway gateway = (Gateway) Naming.lookup("rmi://" + properties.get("rmi_server_hostname") + ":" + properties.get("rmi_server_port") + "/server");
            gateway.webServer(webServerName, this);
            System.out.println("WebServer " + webServerName + " started");
            gateway.registerForCallback(webServerName, this);

        } catch (Exception e) {
            System.out.println("Error on WebServer: " + e.getMessage());
        }


    }

    @Override
    public void notifyNewDataAvailable(String name, String message) throws RemoteException {
        System.out.println("Notificação recebida no " + name + ": " + message);
    }

    public static void main(String[] args) throws RemoteException {
        try {
            WebServerImpl webServer = new WebServerImpl("WebServer_V1");
        } catch (RemoteException e) {
            System.out.println("Error on WebServer CRITICAL: " + e.getMessage());
        }
    }
}
