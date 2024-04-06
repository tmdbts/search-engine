package pt.uc.dei.student.tmdbts.search_engine.protocol;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class CommunicationHandler {
    private final String GROUP_IP_ADDRESS = "224.0.0.69";

    private final int PORT = 42069;

    private MulticastSocket socket;

    private InetAddress group;

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

    public void sendMessage(String message) throws Exception {
        byte[] sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, group, PORT);
        socket.send(sendPacket);

        System.out.println("Message sent to multicast group: " + message);
    }

    public String receiveMessage() throws Exception {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        socket.receive(receivePacket);

        return new String(receivePacket.getData(), 0, receivePacket.getLength());
    }

    public void closeSocket() {
        socket.close();
    }
}
