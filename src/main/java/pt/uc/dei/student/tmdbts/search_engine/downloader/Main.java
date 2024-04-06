package pt.uc.dei.student.tmdbts.search_engine.downloader;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());

        Crawler crawler = new Crawler();

        crawler.run();
    }
}
