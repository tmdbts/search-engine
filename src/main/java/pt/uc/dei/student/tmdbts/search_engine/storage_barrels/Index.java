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
    private HashMap<String, ArrayList<URI>> index = new HashMap<>();
    private HashMap<URI, List<String>> meta = new HashMap<>();
    private Message message;

    public HashMap<String, ArrayList<URI>> getIndex() {
        return index;
    }

    public void setIndex(HashMap<String, ArrayList<URI>> index) {
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

        if (message.getType() == null) {
            return;
        }

        if (message.getType().equals(RequestTypes.WORD_LIST)) {
            for (String item : message.getList()) {
                URI current = URI.create(message.getBodyMap().get("url"));

                ArrayList<URI> currentURL = new ArrayList<>();

                if (index.get(item) != null && index.get(item).contains(current)) {
                    continue;
                } else if (index.get(item) != null && !index.get(item).contains(current)){
                    index.get(item).add(current);
                } else {
                    currentURL.add(current);
                    index.put(item, currentURL);
                }

                Path path = Path.of("./index.txt");

                FileWriter.writeData(index, path.toString());
            }
        }
    }
}