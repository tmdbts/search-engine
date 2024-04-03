package pt.uc.dei.student.tmdbts.search_engine.downloader;

public class Main {
    public static void main(String[] args) {
//        if (args.length != 0) {
//            System.out.println("Usage: java -jar downloader.jar <url>");
//            System.exit(1);
//        }

        Downloader downloader = new Downloader("https://www.uc.pt");

        downloader.run();
    }
}
