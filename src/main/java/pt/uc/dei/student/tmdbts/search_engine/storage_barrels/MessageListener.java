package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.protocol.CommunicationHandler;

/**
 * Class that listens for messages and handles them
 */
public class MessageListener implements Runnable {
    /**
     * The communication handler
     */
    private CommunicationHandler commHandler;

    /**
     * The storage barrels implementation
     */
    private StorageBarrelsImpl storageBarrelsImpl;

    /**
     * The message
     */
    private String message;

    /**
     * Constructor
     *
     * @param commHandler    The communication handler
     * @param storageBarrels The storage barrels implementation
     */
    MessageListener(CommunicationHandler commHandler, StorageBarrelsImpl storageBarrels) {
        this.commHandler = commHandler;
        storageBarrelsImpl = storageBarrels;
    }

    /**
     * Receive messages and handle them. Close the socket when done.
     */
    public void run() {
        try {
            while (true) {
                message = commHandler.receiveMessage();

                System.out.println("Recived message: " + message);

                storageBarrelsImpl.handleMessage(message);
            }
        } catch (Exception e) {
            System.out.println("Error while listening for multicast messages: " + e);
        } finally {
            commHandler.closeSocket();
        }
    }

    public String getMessage() {
        return message;
    }
}
