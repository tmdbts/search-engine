package pt.uc.dei.student.tmdbts.search_engine.storage_barrels;

import pt.uc.dei.student.tmdbts.search_engine.protocol.SEProtocol;

public class MessageListener implements Runnable{

    MessageListener(SEProtocol seProtocol) {

    }

    public void run(){
        try {
            while (true){
                String message = protocol.receiveMessage();
                System.out.println("Recived message: " + message);
            }
            listenerThread.join();
        } catch (Exception e){
            System.out.println("Error while listening for multicast messages: " + e);
            e.printStackTrace();
        }
    }
}
