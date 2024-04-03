package pt.uc.dei.student.tmdbts.search_engine.url_queue;

import pt.uc.dei.student.tmdbts.search_engine.URL;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class URLQueue implements RemoteURLQueue {
    private ConcurrentLinkedDeque<URL> queue = new ConcurrentLinkedDeque<>();

    public void addURL(URL url) {
        queue.add(url);
    }

    public void addURL(List<URL> urls) {
        queue.addAll(urls);
    }

    public URL getURL() {
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
        for (URL url : queue) {
            System.out.println(url);
        }
    }
}
