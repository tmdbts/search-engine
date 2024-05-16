package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

public class Main {
    public static void main(String[] args) {
        try {
            StorageBarrelsImpl storageBarrels = new StorageBarrelsImpl("tmdbts");
        } catch (Exception e) {
            System.out.println("Error in main: " + e.getMessage());
        }
    }
}
