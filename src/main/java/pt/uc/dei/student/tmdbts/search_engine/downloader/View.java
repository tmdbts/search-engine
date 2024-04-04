package pt.uc.dei.student.tmdbts.search_engine.downloader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * View
 */
public class View {
    public void displaySearchBar() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Search: ");

            String url = scanner.nextLine();

            System.out.println(url);

            if (isCommand(url)) {
                System.out.println("Command");
                continue;
            }

            if (!isURL(url)) {
                try {
                    URI urlObject = new URI(url);
                } catch (URISyntaxException e) {
                    System.out.println("Invalid URL");
                }
//                TODO: search for the terms in the inverted index

                continue;
            }

//            TODO: run the downloader

//            Thread thread = new Thread(downloader);
//            Downloader downloader = new Downloader(url);
//            thread.start();
        }
    }

    /**
     * Check if the given string is a command
     *
     * @param input search input
     * @return True if the given string is a command and false otherwise
     */
    private boolean isCommand(String input) {
        return input.startsWith(":");
    }

    /**
     * Check if the given string is a URL
     *
     * @param input search input
     * @return True if the given string is a URL and false otherwise
     */
    private Boolean isURL(String input) {
        return input.startsWith("http://") || input.startsWith("https://");
    }
}
