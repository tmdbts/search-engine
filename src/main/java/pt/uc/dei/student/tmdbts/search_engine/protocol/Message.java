package pt.uc.dei.student.tmdbts.search_engine.protocol;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that represents a message
 * <p>
 * The message is a data structure that stores the type of the request, the body of the message and the list of items.
 * The message can be encoded and decoded to be sent over the network.
 * <p>
 * The message has the following format:
 * type | request_type ; custom_field | custom_field_value ; listName_count | list_length ; listName_index_name | item ; listName_index_name | item ; ...
 * <p>
 * The format of the message follows the following rules:
 * <p>
 * - The message starts with the type of the request
 * <p>
 * - The type of the request is followed by a semicolon
 * <p>
 * - The type of the request is followed by the body of the message
 * <p>
 * - The body of the message is a list of key-value pairs separated by a semicolon
 * <p>
 * - The key-value pairs are separated by a pipe
 * <p>
 * - The key is the name of the field
 * <p>
 * - The value is the value of the field
 * <p>
 * - The body of the message is followed by a semicolon
 * <p>
 * - The body of the message is followed by the list of items
 * <p>
 * - The list of items is a list of key-value pairs separated by a semicolon
 * <p>
 * - The key-value pairs are separated by a pipe
 * <p>
 * - The key is the name of the field
 * <p>
 * - The value is the value of the field
 * <p>
 * - The list of items is followed by a semicolon
 */
public class Message {
    /**
     * The type of the request
     */
    private RequestTypes type;

    /**
     * The body of the message. As received from the network.
     */
    private String body;

    /**
     * The body of the message as a map.
     */
    private HashMap<String, String> bodyMap = new HashMap<>();

    /**
     * The list of items in the message
     */
    private ArrayList<String> list;

    /**
     * The length of the list
     */
    private int listLength = 0;

    /**
     * The name of the list
     */
    private String listName;

    /**
     * The maximum size of a UDP message
     */
    private static final int MAX_UDP_MESSAGE_SIZE = 1024;

    /**
     * Constructor
     */
    public Message() {
    }

    /**
     * Constructor
     *
     * @param body The body of the message
     */
    public Message(String body) {
        this.body = body;
    }

    /**
     * Parse the message body.
     *
     * @param messageBody The body of the message
     */
    public void parseMessage(String messageBody) {
        bodyMap = generateMessageMap(messageBody);
        findListProperties(bodyMap);
        parseList(bodyMap);
    }

    /**
     * Encode a message
     *
     * @param messageMap The message map
     * @param type       The type of the message
     * @param listName   The name of the list
     * @param list       The list of items
     * @return The encoded message
     */
    public static ArrayList<String> encode(HashMap<String, String> messageMap, RequestTypes type, String listName, ArrayList<String> list) {
        ArrayList<String> messages = new ArrayList<>();
        int i = 0;

        while (!list.isEmpty()) {
            StringBuilder message = initMessage(messageMap, type, listName, list.size());

            while (message.length() < MAX_UDP_MESSAGE_SIZE) {
                if (list.isEmpty()) {
                    break;
                }

                String encodedItem = encodeListItem(list.remove(0), listName, i);

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

    /**
     * Encode a message
     *
     * @param messageMap The message map
     * @param type       The type of the message
     * @param listName   The name of the list
     * @param listLength The list of items
     * @return The encoded message
     */
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

    /**
     * Encode a list
     *
     * @param list     The list of items
     * @param listName The name of the list
     * @return The encoded list
     */
    private static String encodeList(ArrayList<String> list, String listName) {
        StringBuilder message = new StringBuilder();
        int listLength = list.size();

        for (int i = 0; i < list.size(); i++) {
            message.append(encodeListItem(list.get(i), listName, i));
        }

        return message.toString();
    }

    /**
     * Encode a list item.
     * <p>
     * The format is: listName_index_name | item ;
     *
     * @param item     The item to encode
     * @param listName The name of the list
     * @param index    The index of the item
     * @return The encoded list item
     */
    private static String encodeListItem(String item, String listName, int index) {
        return listName + "_" + index + "_name" + " | " + item + " ; ";
    }

    private HashMap<String, String> generateMessageMap(String messageBody) {
        HashMap<String, String> messageMap = new HashMap<>();

        String[] splitMessage = messageBody.split(";");

        for (String field : splitMessage) {
            if (field.isEmpty() || field.isBlank()) continue;

            String[] keyValuePair = field.split("\\|");

            if (keyValuePair.length != 2) {
                continue;
            }

            if (keyValuePair[0].isBlank() || keyValuePair[0].isEmpty() || keyValuePair[1].isBlank() || keyValuePair[1].isEmpty())
                continue;

            messageMap.put(keyValuePair[0].trim(), keyValuePair[1].trim());
        }

        return messageMap;
    }

    /**
     * Find the list length and name properties
     *
     * @param messageMap the message map
     * @return true if the list properties were found, false otherwise
     */
    private boolean findListProperties(HashMap<String, String> messageMap) {
        if (!containsList(messageMap)) return false;

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
            }
        }

        return false;
    }


    /**
     * Check if the message contains a list
     *
     * @param messageMap The message map
     * @return true if the message contains a list, false otherwise
     */
    private boolean containsList(HashMap<String, String> messageMap) {
        for (String key : messageMap.keySet()) {
            if (key.contains("count")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Parse the list
     *
     * @param messageMap The message map
     */
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
