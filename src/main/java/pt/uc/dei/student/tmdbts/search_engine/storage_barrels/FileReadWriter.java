package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public abstract class FileReadWriter {

    public static void writeData(HashMap<String, ArrayList<URI>> index, String filePath) {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (Map.Entry<String, ArrayList<URI>> entry : index.entrySet()) {
                String urlist = entry.getValue().stream().map(URI::toString).collect(Collectors.joining(", "));

                writer.write(String.format("%s | %s\n", entry.getKey(), urlist));
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static HashMap<String, ArrayList<URI>> readData(String filePath) {
        HashMap<String, ArrayList<URI>> index = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split each line into a key and the URL list part
                String[] parts = line.split(" \\| ");
                if (parts.length == 2) {
                    String key = parts[0];
                    String[] urls = parts[1].split(", ");
                    ArrayList<URI> uriList = new ArrayList<>();

                    // Convert each URL string to a URI and add it to the list
                    for (String url : urls) {
                        try {
                            URI uri = new URI(url.trim());
                            uriList.add(uri);
                        } catch (URISyntaxException e) {
                            System.err.println("Invalid URI syntax: " + url);
                        }
                    }

                    // Put the key and URI list into the map
                    index.put(key, uriList);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return index;
    }


}
