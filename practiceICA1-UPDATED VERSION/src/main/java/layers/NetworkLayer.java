package main.java.layers;

import main.java.model.Packet;

public class NetworkLayer implements Layer {
    @Override
    public void processPacket(Packet packet) {
        System.out.println("Processing packet at the Network Layer: " + packet.getData());
    }
}
