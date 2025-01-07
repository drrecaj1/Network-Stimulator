package main.java.layers;

import main.java.model.Packet;

public class IPProtocol implements Layer {
    @Override
    public void processPacket(Packet packet) {
        System.out.println("Processing IP Packet from " + packet.getSourceIP() + " to " + packet.getDestinationIP());
    }
}
