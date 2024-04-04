package pt.uc.dei.student.tmdbts.search_engine.url_queue;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class URLQueue implements RemoteURLQueue {
    private ConcurrentLinkedDeque<URI> queue = new ConcurrentLinkedDeque<>();

    public void addURL(URI url) {
        queue.add(url);
    }

    public void addURL(List<URI> urls) {
        queue.addAll(urls);
    }

    public URI getURL() {
        return queue.pop();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

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
