package pt.uc.dei.student.tmdbts.search_engine.downloader;

import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Properties;

public class Crawler {
    /**
     * Main method of the Crawler class.
     * <p>
     * This method is responsible for the main loop of the Crawler.
     * It will connect to the server and request URLs to download.
     * <p>
     * The Crawler will download the URLs and store them in the Storage Barrels.
     * <p>
     * The Crawler will download up to 5 URLs at the same time.
     *
     * @apiNote Might make a ton of requests to the server.
     */
    public void run() {
        String rootPath = System.getProperty("user.dir");
        String appConfigPath = rootPath + "/app.properties";

        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            System.out.println("Error loading app properties: " + e.getMessage());
        }

        HashSet<Thread> activeDownloaders = new HashSet<>();

        try {
            Gateway gateway = (Gateway) Naming.lookup("rmi://" + appProps.get("rmi_server_hostname") + ":" + appProps.get("rmi_server_port") + "/server");

            while (true) {
                if (activeDownloaders.size() >= Integer.parseInt((String) appProps.get("max_downloaders"))) { /* TODO: Magic number */
                    activeDownloaders.removeIf(thread -> !thread.isAlive());

                    continue;
                }

                if (gateway.isQueueEmpty()) continue;

                URI url = gateway.getURL();

                Downloader downloader = new Downloader(url, gateway);
                Thread thread = new Thread(downloader);

                thread.start();
                activeDownloaders.add(thread);
            }
        } catch (NotBoundException | MalformedURLException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Error getting URL from server: " + e.getMessage());
        }
    }
}
