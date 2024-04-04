package pt.uc.dei.student.tmdbts.search_engine.protocol;

public enum RequestTypes {
    URL_LIST("url_list"),
    WORD_LIST("word_list"),
    LOGIN("login"),
    STATUS("status"),
    SEARCH("search");

    private final String typeString;

    RequestTypes(String stringType) {
        this.typeString = this.name();
    }

    public String getTypeString() {
        return typeString;
    }
}