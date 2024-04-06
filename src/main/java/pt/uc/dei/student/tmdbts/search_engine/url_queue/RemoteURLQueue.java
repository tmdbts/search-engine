package pt.uc.dei.student.tmdbts.search_engine.url_queue;

import java.net.URI;
import java.rmi.Remote;
import java.util.List;

/**
 * URLQueue
 */
public interface RemoteURLQueue extends Remote {
    void addURL(URI url);

    void addURL(List<URI> urls);

    URI getURL();

    boolean isEmpty();

    int size();

    void printQueue();
}
