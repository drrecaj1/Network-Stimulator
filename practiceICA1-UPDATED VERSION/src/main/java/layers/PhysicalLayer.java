package main.java.layers;

import main.java.model.Packet;
import main.java.services.ConsoleColors;

public class PhysicalLayer implements Layer {
    @Override
    public void processPacket(Packet packet) {
        // Log processing details
        System.out.println(ConsoleColors.BLUE + "\nPhysical Layer:" + ConsoleColors.RESET
                + " Processing packet data");
        System.out.println("  Packet Data:" +  " " +  packet.getData());
        System.out.println(ConsoleColors.PURPLE + "Transitioning to the Network Layer..."
                + ConsoleColors.RESET);
    }
}

