package pt.uc.dei.student.tmdbts.search_engine.protocol;

import java.util.ArrayList;
import java.util.HashMap;

public class Message {
    private String type;

    private String body;

    private HashMap<String, String> bodyMap = new HashMap<>();

    private ArrayList<String> list;

    private int listLength = 0;

    private String listName;

    public Message(String body) {
        this.body = body;
    }

    public void parseMessage(String messageBody) {
        HashMap<String, String> messageMap = new HashMap<>();

        String[] splitMessage = messageBody.split(";");

        for (String field : splitMessage) {
            String[] keyValuePair = field.split("\\|");

            messageMap.put(keyValuePair[0].trim(), keyValuePair[1].trim());
        }

        System.out.println("List: " + containsList(messageMap));

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

        message.append("type | ").append(type).append(" ; ");

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
            throw new IllegalStateException("The body of the message is empty.");
        }

        StringBuilder message = new StringBuilder();

        message.append("type | ").append(type).append(" ; ");

        for (String key : bodyMap.keySet()) {
            message.append(key).append(" | ").append(bodyMap.get(key)).append(" ; ");
        }

        if (listName != null && list != null) {
            message.append(encodeList(list, listName));
        }

        message.append('\n');

        return message.toString();
    }

    private static String encodeList(ArrayList<String> list, String listName) {
        StringBuilder message = new StringBuilder();

        message.append(listName).append("_count | ").append(list.size()).append(" ; ");

        for (int i = 0; i < list.size(); i++) {
            message.append(listName).append("_").append(i).append("_name | ").append(list.get(i)).append(" ; ");
        }

        return message.toString();
    }

    private String findType(HashMap<String, String> messageMap) {
        for (String key : messageMap.keySet()) {
            if (key.startsWith("type")) {
                return messageMap.get(key);
            }
        }

        return null;
    }

    /**
     * Find the list properties
     *
     * @param messageMap the message map
     * @return true if the list properties were found, false otherwise
     */
    private boolean fidListProperties(HashMap<String, String> messageMap) {
        for (String key : messageMap.keySet()) {
            if (key.contains("count")) {
                listLength = Integer.parseInt(messageMap.get(key));
                listName = key.split("_")[0];

                list = new ArrayList<>();

                return true;
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

    public String getBody() {
        return body;
    }
}
