package pt.uc.dei.student.tmdbts.search_engine.downloader;

import java.util.logging.Logger;

public class Downloader implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final String url;

    private final String multicastAddress = "";

    public Downloader(String url) {
        this.url = url;
//        TODO: Initialise multicast address
    }

    @Override
    public void run() {
        LOGGER.info("Starting download of " + this.url);

//        TODO: get urls
//        TODO: send found URLs to URLQueue
//        TODO: get words
//        TODO: send data to inverted index
    }

    /**
     * Send data to the server through multicast
     */
    private void sendData() {

    }
}