package pt.uc.dei.student.tmdbts.search_engine.url_queue;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Class that represents a queue of URLs. It is a thread-safe queue
 */
public class URLQueue implements RemoteURLQueue {
    /**
     * Queue of URLs. It is a thread-safe queue
     */
    private ConcurrentLinkedDeque<URI> queue = new ConcurrentLinkedDeque<>();

    /**
     * Add a URL to the queue
     *
     * @param url URL to add
     */
    public void addURL(URI url) {
        queue.add(url);
    }

    /**
     * Add a list of URLs to the queue
     *
     * @param urls List of URLs to add
     */
    public void addURL(List<URI> urls) {
        queue.addAll(urls);
    }

    public URI getURL() {
        return queue.pop();
    }

    /**
     * Get the queue
     *
     * @return The queue
     */
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Get the size of the queue
     *
     * @return The size of the queue
     */
    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public void printQueue() {
        for (URI url : queue) {
            System.out.println(url.toString());
        }
    }
}
