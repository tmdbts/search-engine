package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class FileWriter {

    public static void writeData(HashMap<String, URI> index, String filePath){

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))){
                for (Map.Entry<String, URI> entry : index.entrySet()){
                    writer.write(String.format("%s|%s", entry.getKey(), entry.getValue().toString()));
            }
        } catch (IOException e){
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
