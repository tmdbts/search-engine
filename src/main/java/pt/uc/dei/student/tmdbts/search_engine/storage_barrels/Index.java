package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.URL;
import pt.uc.dei.student.tmdbts.search_engine.protocol.Message;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SplittableRandom;

public class Index {
    private ArrayList<String> namesList;
    private HashMap<String, URI> index = new HashMap<>();
    private HashMap<URI, List<String>> meta = new HashMap<>();
    private Message message;
    public HashMap<String, URI> getIndex() {
        return index;
    }

    public void setIndex(HashMap<String, URI> index) {
        this.index = index;
    }

    public HashMap<URI, List<String>> getMeta() {
        return meta;
    }

    public void setMeta(HashMap<URI, List<String>> meta) {
        this.meta = meta;
    }

    public void handleMessage(String inComingMessage) {
        message = new Message(inComingMessage);
        namesList = message.getList();

        if (message.getType().equals("word_list")) {
            for (String item : message.getList()) {
                try {
                    URI uri = new URI(message.getBodyMap().get("url"));
                    index.put(item, uri);
                } catch (URISyntaxException e) {
                    System.out.println("Error in handleMessage: " + e.getMessage());
                }
            }

            FileWriter.writeData(index, "index.txt");

        }
    }
}
