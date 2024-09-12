package pt.uc.dei.student.tmdbts.search_engine.protocol;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

/**
 * Class that handles the communication between the nodes. It sends and receives messages from the multicast group.
 */
public class CommunicationHandler {
    /**
     * IP address of the multicast group
     */
    private final String GROUP_IP_ADDRESS = "224.0.0.69";

    /**
     * Timeout for the socket
     */
    private final int SOCKET_TIMEOUT = 5000;

    /**
     * Port of the multicast group
     */
    private final int PORT = 42069;

    /**
     * Multicast socket
     */
    private MulticastSocket socket;

    /**
     * Multicast group
     */
    private InetAddress group;

    /**
     * Constructor
     * <p>
     * Creates a new multicast socket and joins the multicast group
     */
    public CommunicationHandler() {
        try {
            this.socket = new MulticastSocket(PORT);
            this.group = InetAddress.getByName(GROUP_IP_ADDRESS);

            InetSocketAddress groupAddress = new InetSocketAddress(GROUP_IP_ADDRESS, PORT);

            socket.joinGroup(groupAddress, socket.getNetworkInterface());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send a message to the multicast group
     * <p>
     * The message is sent as a byte array
     *
     * @param message Message to send
     * @throws Exception If an error occurs while sending the message
     */
    public void sendMessage(String message) throws Exception {
        byte[] sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, group, PORT);
        socket.send(sendPacket);

        System.out.println("Message sent to multicast group: " + message);
    }

    /**
     * Send a list of messages to the multicast group
     *
     * @param messages List of messages to send
     * @throws Exception If an error occurs while sending the messages
     */
    public void sendMessage(ArrayList<String> messages) throws Exception {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    /**
     * Receive a message from the multicast group
     *
     * @return Received message
     * @throws Exception If an error occurs while receiving the message
     */
    public String receiveMessage() throws Exception {
        byte[] receiveData = new byte[2000]; //WARNING: Magic number
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        socket.receive(receivePacket);

        return new String(receivePacket.getData(), 0, receivePacket.getLength());
    }

    /**
     * Close the socket
     */
    public void closeSocket() {
        socket.close();
    }
}
