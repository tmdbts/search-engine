package pt.uc.dei.student.tmdbts.search_engine.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Message {
    private RequestTypes type;

    private String body;

    private HashMap<String, String> bodyMap = new HashMap<>();

    private ArrayList<String> list;

    private int listLength = 0;

    private String listName;

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

        System.out.println("List: " + containsList(messageMap));

        bodyMap = messageMap;

        fidListProperties(messageMap);
        parseList(messageMap);
    }

    public static String encode(HashMap<String, String> messageMap, RequestTypes type, String listName, ArrayList<String> list) {
        if (messageMap.isEmpty()) {
            throw new IllegalStateException("The body of the message is empty.");
        }

        if (type == null) {
            throw new IllegalStateException("Type not set");
        }

        StringBuilder message = new StringBuilder();

        message.append("type | ").append(type.getTypeString()).append(" ; ");

        for (String key : messageMap.keySet()) {
            message.append(key).append(" | ").append(messageMap.get(key)).append(" ; ");
        }

        if (listName != null && list != null) {
            message.append(encodeList(list, listName));
        }

        message.append('\n');

        return message.toString();
    }

    public String encode() throws IllegalStateException {
        if (type == null) {
            throw new IllegalStateException("Type not set");
        }

        if (bodyMap.isEmpty()) {
            encodeList(list, listName);
        }

        StringBuilder message = new StringBuilder();

        message.append("type | ").append(type.getTypeString()).append(" ; ");

        for (String key : bodyMap.keySet()) {
            message.append(key).append(" | ").append(bodyMap.get(key)).append(" ; ");
        }

        if (listName != null && list != null) {
            message.append(encodeList(list, listName));
        }

        message.append('\n');

        return message.toString();
    }

    public static String encode(RequestTypes type, List<String> list, String listName) {
        int listLength = list.size();
        StringBuilder message = new StringBuilder();

        message.append("type | ").append(type.getTypeString()).append(" ; ");
        message.append(listName).append("_count | ").append(listLength).append(" ; ");

        for (int i = 0; i < list.size(); i++) {
            message.append(listName).append("_").append(i).append(" | ").append(list.get(i)).append(" ; ");
        }

        return message.toString();
    }


    private static String encodeList(ArrayList<String> list, String listName) {
        int listLength = list.size();
        StringBuilder message = new StringBuilder();

        message.append(listName).append("_count | ").append(listLength).append(" ; ");

        for (int i = 0; i < list.size(); i++) {
            message.append(listName).append("_").append(i).append("_name | ").append(list.get(i)).append(" ; ");
        }

        return message.toString();
    }

    /**
     * Find the list length and name properties
     *
     * @param messageMap the message map
     * @return true if the list properties were found, false otherwise
     */
    private boolean fidListProperties(HashMap<String, String> messageMap) {
        if (!containsList(messageMap)) return false;

        for (String key : messageMap.keySet()) {
            if (key.contains("count")) {
                listLength = Integer.parseInt(messageMap.get(key));
                listName = key.split("_")[0];

                list = new ArrayList<>();

                return true;
            }

            if (key.contains("type")) {
                type = RequestTypes.fromString(messageMap.get(key));
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
            boolean listExists = fidListProperties(messageMap);

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
