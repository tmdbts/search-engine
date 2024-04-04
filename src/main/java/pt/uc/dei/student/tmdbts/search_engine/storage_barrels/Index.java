package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.URL;

import java.util.HashMap;
import java.util.List;


public class Index {

    private HashMap<String, HashMap<URL, Integer>> index = new HashMap<>();
    private HashMap<URL, List<String>> meta = new HashMap<>();

    public HashMap<String, HashMap<URL, Integer>> getIndex() {
        return index;
    }

    public void setIndex(HashMap<String, HashMap<URL, Integer>> index) {
        this.index = index;
    }

    public HashMap<URL, List<String>> getMeta() {
        return meta;
    }

    public void setMeta(HashMap<URL, List<String>> meta) {
        this.meta = meta;
    }

   void handleMessage (String message){

   }

}
