package pt.uc.dei.student.tmdbts.search_engine.url_queue;

import pt.uc.dei.student.tmdbts.search_engine.URL;

import java.rmi.Remote;
import java.util.List;

/**
 * URLQueue
 */
public interface RemoteURLQueue extends Remote {
    void addURL(URL url);

    void addURL(List<URL> urls);

    URL getURL();

    boolean isEmpty();

    int size();

    void printQueue();
}
