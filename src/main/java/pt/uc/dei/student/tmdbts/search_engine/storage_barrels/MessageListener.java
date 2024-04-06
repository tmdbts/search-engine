package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.protocol.CommunicationHandler;

public class MessageListener implements Runnable {
    private CommunicationHandler commHandler;

    private StorageBarrelsImpl storageBarrelsImpl;

    private String message;

    MessageListener(CommunicationHandler commHandler, StorageBarrelsImpl storageBarrels) {
        this.commHandler = commHandler;
        storageBarrelsImpl = storageBarrels;
    }

    public String getMessage() {
        return message;
    }

    public void run() {
        try {
            while (true) {
                message = commHandler.receiveMessage();

                System.out.println("Recived message: " + message);

                storageBarrelsImpl.sendMessage(message);
            }
        } catch (Exception e) {
            System.out.println("Error while listening for multicast messages: " + e);
        } finally {
            commHandler.closeSocket();
        }
    }
}
