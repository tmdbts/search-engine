package pt.uc.dei.student.tmdbts.search_engine.url_queue;

import pt.uc.dei.student.tmdbts.search_engine.URL;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * URLQueue
 */
public class URLQueue {
    private ConcurrentLinkedDeque<URL> queue = new ConcurrentLinkedDeque<>();

}
