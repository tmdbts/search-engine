package pt.uc.dei.student.tmdbts.search_engine.protocol;

/**
 * Enumerates the different types of requests that can be made.
 */
public enum RequestTypes {
    /**
     * URL list request type. Used to send a list of URLs linked to one of them.
     */
    URL_LIST("url_list"),
    /**
     * Word list request type. Used to send a list of words present in the URL page.
     */
    WORD_LIST("word_list"),
    /**
     * Metadata request type. Used to send the metadata of a URL page.
     */
    META_DATA("meta_data");

    /**
     * The string representation of the request type.
     */
    private final String typeString;

    /**
     * Constructor
     *
     * @param stringType The string representation of the request type
     */
    RequestTypes(String stringType) {
        this.typeString = stringType;
    }

    /**
     * Get the string representation of the request type.
     *
     * @return The string representation of the request type
     */
    public String getTypeString() {
        return typeString;
    }

    /**
     * Get the request type from a string.
     *
     * @param text The string representation of the request type
     * @return The request type
     */
    public static RequestTypes fromString(String text) {
        for (RequestTypes reqType : RequestTypes.values()) {
            if (reqType.typeString.equalsIgnoreCase(text)) {
                return reqType;
            }
        }
        return null;
    }

}