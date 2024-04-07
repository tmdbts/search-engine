package pt.uc.dei.student.tmdbts.search_engine.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Message {
    private RequestTypes type;

    private String body;

    private HashMap<String, String> bodyMap = new HashMap<>();

    private ArrayList<String> list;

    private int listLength = 0;

    private String listName;

    private static final int MAX_UDP_MESSAGE_SIZE = 1024;

    public Message() {
    }

    public Message(String body) {
        this.body = body;
    }

    public void parseMessage(String messageBody) {
        HashMap<String, String> messageMap = new HashMap<>();

        String[] splitMessage = messageBody.split(";");

        for (String field : splitMessage) {
            if (field.isBlank()) continue;

            String[] keyValuePair = field.split("\\|");

            System.out.println(Arrays.toString(keyValuePair));

            messageMap.put(keyValuePair[0].trim(), keyValuePair[1].trim());
        }

        bodyMap = messageMap;

        findListProperties(messageMap);
        parseList(messageMap);
    }

    public static ArrayList<String> encode(HashMap<String, String> messageMap, RequestTypes type, String listName, ArrayList<String> list) {
        ArrayList<String> messages = new ArrayList<>();
        int i = 0;

        while (!list.isEmpty()) {
            StringBuilder message = initMessage(messageMap, type, listName, list.size());

            while (message.length() < MAX_UDP_MESSAGE_SIZE) {
                if (list.isEmpty()) {
                    break;
                }

                String encodedItem = encodeListItem(list.removeFirst(), listName, i);

                if (message.length() + encodedItem.length() >= MAX_UDP_MESSAGE_SIZE - 2) {
                    message.append('\n');
                    messages.add(message.toString());

                    return messages;
                }

                message.append(encodedItem);
                i += 1;
            }

            message.append('\n');

            messages.add(message.toString());
        }

        return messages;
    }

    private static StringBuilder initMessage(HashMap<String, String> messageMap, RequestTypes type, String listName, int listLength) {
        if (messageMap.isEmpty()) {
            throw new IllegalStateException("The body of the message is empty.");
        }

        if (type == null) {
            throw new IllegalStateException("Type not set");
        }

        StringBuilder message = new StringBuilder();

        message.append("type | ").append(type.getTypeString()).append(" ; ");
        message.append(listName).append("_count | ").append(listLength).append(" ; ");

        for (String key : messageMap.keySet()) {
            message.append(key).append(" | ").append(messageMap.get(key)).append(" ; ");
        }

        return message;
    }

    private static String encodeList(ArrayList<String> list, String listName) {
        StringBuilder message = new StringBuilder();
        int listLength = list.size();

        for (int i = 0; i < list.size(); i++) {
            message.append(encodeListItem(list.get(i), listName, i));
        }

        return message.toString();
    }

    private static String encodeListItem(String item, String listName, int index) {
        return listName + "_" + index + "_name" + " | " + item + " ; ";
    }

    /**
     * Find the list length and name properties
     *
     * @param messageMap the message map
     * @return true if the list properties were found, false otherwise
     */
    private boolean findListProperties(HashMap<String, String> messageMap) {
        if (!containsList(messageMap)) return false;

        System.out.println("DEBUG " + messageMap); //mesage map sem type para a meta data

        for (String key : messageMap.keySet()) {
            if (list != null && type != null) {
                return true;
            }

            if (key.contains("count")) {
                listLength = Integer.parseInt(messageMap.get(key).trim());
                listName = key.split("_")[0].trim();

                list = new ArrayList<>();
            }

            if (key.equals("type")) {
                type = RequestTypes.fromString(messageMap.get(key).trim());
                System.out.println("HERE " + type);
            }
        }

        return false;
    }

    private boolean containsList(HashMap<String, String> messageMap) {
        for (String key : messageMap.keySet()) {
            if (key.contains("count")) {
                return true;
            }
        }

        return false;
    }

    private void parseList(HashMap<String, String> messageMap) {
        if (list == null) {
            boolean listExists = findListProperties(messageMap);

            if (!listExists) {
                return;
            }
        }

        for (String key : messageMap.keySet()) {
            if (key.contains(listName)) {
                String[] keyParts = key.split("_");

                if (keyParts.length != 3) {
                    continue;
                }

                int index = Integer.parseInt(keyParts[1]);
                String keyName = keyParts[2];

                list.add(messageMap.get(key));
            }
        }

        System.out.println(list);
    }

    public RequestTypes getType() {
        return type;
    }

    public void setType(RequestTypes type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HashMap<String, String> getBodyMap() {
        return bodyMap;
    }

    public void setBodyMap(HashMap<String, String> bodyMap) {
        this.bodyMap = bodyMap;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public int getListLength() {
        return listLength;
    }

    public void setListLength(int listLength) {
        this.listLength = listLength;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
