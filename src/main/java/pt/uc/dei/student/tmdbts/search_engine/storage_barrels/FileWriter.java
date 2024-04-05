package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class FileWriter {

    public static void writeData(HashMap<String, ArrayList<URI>> index, String filePath){

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))){
                for (Map.Entry<String,ArrayList<URI>> entry : index.entrySet()){
                    String urlist = entry.getValue().stream().map(URI::toString).collect(Collectors.joining(", "));

                    writer.write(String.format("%s | %s\n", entry.getKey(), urlist));
            }
        } catch (IOException e){
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
