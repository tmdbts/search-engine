package pt.uc.dei.student.tmdbts.search_engine.protocol;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;

public class SEProtocol {
    private final String GROUP_ADDRESS = "127.0.0.1";

    private final int PORT = 42069;

    private HashMap<Integer, String> cache = new HashMap<>();

    public void sendMessage(String message) throws Exception {
        MulticastSocket socket = new MulticastSocket();
        InetAddress group = InetAddress.getByName(GROUP_ADDRESS);
        byte[] sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, group, PORT);
        socket.send(sendPacket);

        System.out.println("Message sent to multicast group: " + message);

        socket.close();
    }

    public String receiveMessage() throws Exception {
        MulticastSocket socket = new MulticastSocket(PORT);
        InetAddress group = InetAddress.getByName(GROUP_ADDRESS);

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        socket.joinGroup(receivePacket.getSocketAddress(), socket.getNetworkInterface());

        socket.receive(receivePacket);

        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Message received from multicast group: " + message);

        socket.close();

        return message;
    }
}
