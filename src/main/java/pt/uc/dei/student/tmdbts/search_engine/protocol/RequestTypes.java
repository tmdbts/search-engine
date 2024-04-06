package pt.uc.dei.student.tmdbts.search_engine.protocol;

public enum RequestTypes {
    URL_LIST("url_list"),
    WORD_LIST("word_list"),
    META_DATA("meta_data"),
    LOGIN("login"),
    STATUS("status"),
    SEARCH("search");

    private final String typeString;

    RequestTypes(String stringType) {
        this.typeString = stringType;
    }

    public String getTypeString() {
        return typeString;
    }

    public static RequestTypes fromString(String text) {
        for (RequestTypes reqType : RequestTypes.values()) {
            if (reqType.typeString.equalsIgnoreCase(text)) {
                return reqType;
            }
        }
        return null;
    }

}