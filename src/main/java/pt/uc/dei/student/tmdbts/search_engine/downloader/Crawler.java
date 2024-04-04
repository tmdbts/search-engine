package pt.uc.dei.student.tmdbts.search_engine.downloader;

import pt.uc.dei.student.tmdbts.search_engine.URL;
import pt.uc.dei.student.tmdbts.search_engine.gateway.Gateway;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

public class Crawler extends UnicastRemoteObject {
    protected Crawler() throws RemoteException {
    }

    protected Crawler(int port) throws RemoteException {
        super(port);
    }

    protected Crawler(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    public void run() {
        HashSet<Thread> activeDownloaders = new HashSet<>();

        try {
            Crawler crawler = (Crawler) Naming.lookup("rmi://localhost:32450/TO_DO");

            while (true) {
                if (activeDownloaders.size() >= 5) { /* TODO: Magic number */
                    wait();

                    activeDownloaders.removeIf(thread -> !thread.isAlive());

                    continue;
                }

                URL url = Gateway.getURL();

                Downloader downloader = new Downloader(url);
                Thread thread = new Thread(downloader);

                thread.start();
                activeDownloaders.add(thread);
            }
        } catch (NotBoundException | MalformedURLException | RemoteException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
