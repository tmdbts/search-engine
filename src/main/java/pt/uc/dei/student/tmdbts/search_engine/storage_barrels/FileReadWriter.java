package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that reads and writes data to a file
 */
public abstract class FileReadWriter {

    /**
     * Write the index data to a file
     * <p>
     * The format of a file line is:
     * <p>
     * word | url1, url2, url3
     *
     * @param index    The index data
     * @param filePath The file path
     */
    public static void writeIndexData(HashMap<String, ArrayList<URI>> index, String filePath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (Map.Entry<String, ArrayList<URI>> entry : index.entrySet()) {
                String urlist = entry.getValue().stream().map(URI::toString).collect(Collectors.joining(", "));

                writer.write(String.format("%s | %s\n", entry.getKey(), urlist));
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Write the URLs data to a file.
     * <p>
     * The format of a file line is:
     * <p>
     * url1 | url2, url3, url4
     *
     * @param urls     The URLs data
     * @param filePath The file path
     */
    public static void writeUrls(HashMap<URI, ArrayList<URI>> urls, String filePath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (Map.Entry<URI, ArrayList<URI>> entry : urls.entrySet()) {
                String urlist = entry.getValue().stream().map(URI::toString).collect(Collectors.joining(", "));

                writer.write(String.format("%s | %s\n", entry.getKey(), urlist));
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Read the index data from a file
     *
     * @param filePath The file path
     * @return The index data
     */
    public static HashMap<String, ArrayList<URI>> readData(String filePath) {
        HashMap<String, ArrayList<URI>> index = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 2) {
                    String key = parts[0];
                    String[] urls = parts[1].split(", ");
                    ArrayList<URI> uriList = new ArrayList<>();

                    for (String url : urls) {
                        try {
                            URI uri = new URI(url.trim());
                            uriList.add(uri);
                        } catch (URISyntaxException e) {
                            System.err.println("Invalid URI syntax: " + url);
                        }
                    }

                    index.put(key, uriList);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file " + filePath + ": " + e.getMessage());
        }

        return index;
    }

    public static HashMap<URI, List<URI>> readUrls(String filePath) {
        HashMap<URI, List<URI>> urlList = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");

                URI uri_key = URI.create(parts[0]);

                String[] urls = parts[1].split(", ");

                ArrayList<URI> uriList = new ArrayList<>();

                for (String url : urls) {
                    try {
                        URI uri_value = URI.create(url.trim());
                        uriList.add(uri_value);
                    } catch (Exception e) {
                        System.err.println("Invalid URI syntax: " + url);
                    }
                }

                urlList.put(uri_key, uriList);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file " + filePath + ": " + e.getMessage());
        }

        return urlList;
    }
}
