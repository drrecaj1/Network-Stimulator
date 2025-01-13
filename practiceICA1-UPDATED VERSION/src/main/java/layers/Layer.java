package main.java.layers;

import main.java.model.Packet;

public interface Layer {
    void processPacket(Packet packet);
}
