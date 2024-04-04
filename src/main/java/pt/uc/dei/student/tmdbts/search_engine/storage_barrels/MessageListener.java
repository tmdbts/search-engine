package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.protocol.SEProtocol;

public class MessageListener implements Runnable{

    private SEProtocol protocol;
    private StorageBarrelsImpl storageBarrelsImpl;
    private String message;

    MessageListener(SEProtocol seProtocol, StorageBarrelsImpl storageBarrels) {
        protocol = seProtocol;
        storageBarrelsImpl = storageBarrels;
    }

    public String getMessage(){
        return message;
    }

    public void run(){
        try {
            while (true){
                String message = protocol.receiveMessage();
                System.out.println("Recived message: " + message);
                storageBarrelsImpl.sendMessage(message);
            }
        } catch (Exception e){
            System.out.println("Error while listening for multicast messages: " + e);
            e.printStackTrace();
        }
    }
}
