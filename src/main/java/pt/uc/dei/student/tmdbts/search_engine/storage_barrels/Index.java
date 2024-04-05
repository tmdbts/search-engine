package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.protocol.Message;
import pt.uc.dei.student.tmdbts.search_engine.protocol.RequestTypes;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Index {
    private ArrayList<String> namesList;
    private HashMap<String, List<URI>> index = new HashMap<>();
    private HashMap<URI, List<String>> meta = new HashMap<>();
    private Message message;

    public HashMap<String, List<URI>> getIndex() {
        return index;
    }

    public void setIndex(HashMap<String, List<URI>> index) {
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
        message.parseMessage(inComingMessage);
        namesList = message.getList();

        if (message.getType().equals(RequestTypes.WORD_LIST)) {
            for (String item : message.getList()) {
                try {
                    URI uri = new URI(message.getBodyMap().get("url"));
                    index.put(item, uri);
                } catch (URISyntaxException e) {
                    System.out.println("Error in handleMessage: " + e.getMessage());
                }
            }

            Path path = Path.of("./index.txt");

            FileWriter.writeData(index, path.toString());

        }
    }
}