package main.java.layers;

import main.java.model.Packet;

public class Ethernet implements Layer {
    @Override
    public void processPacket(Packet packet) {
        // Simulate Ethernet frame encapsulation
        System.out.println("Processing Ethernet Frame: " + packet.getData());
        System.out.println("Adding MAC address header.");
    }
}
