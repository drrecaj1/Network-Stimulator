package main.java.layers;

import main.java.model.Packet;
import main.java.services.ConsoleColors;

public class NetworkLayer implements Layer {
    @Override
    public void processPacket(Packet packet) {
        // Log processing details
        System.out.println(ConsoleColors.BLUE + "\nNetwork Layer:" + ConsoleColors.RESET
                + " Processing packet data");
        System.out.println("  Packet Data:" + " " + packet.getData());
        System.out.println(ConsoleColors.PURPLE + "Transitioning to the next layer..."
                + ConsoleColors.RESET);
    }
}
