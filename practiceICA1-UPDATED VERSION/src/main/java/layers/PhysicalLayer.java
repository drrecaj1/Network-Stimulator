package main.java.layers;

import main.java.model.Packet;

public class PhysicalLayer implements Layer {
    @Override
    public void processPacket(Packet packet) {
        System.out.println("Processing packet at the Physical Layer: " + packet.getData());
    }
}

