package main.java.layers;

import main.java.model.Packet;
import main.java.model.NetworkAddress;
import main.java.services.ConsoleColors;

public class IPProtocol implements Layer {
    @Override
    public void processPacket(Packet packet) {
        // Fetch IP addresses from headers
        NetworkAddress sourceIP = packet.getHeader("SourceIP");
        NetworkAddress destinationIP = packet.getHeader("DestinationIP");

        // Check if the headers are present
        if (sourceIP == null || destinationIP == null) {
            System.out.println(ConsoleColors.RED + "IP Layer:" + ConsoleColors.RESET
                    + " Missing IP headers. Cannot process packet.");
            packet.drop(); // Drop the packet if headers are missing
            return;
        }

        // Log processing details
        System.out.println(ConsoleColors.BLUE + "\nIP Layer:" + ConsoleColors.RESET
                + " Processing IP Packet");
        System.out.println(ConsoleColors.YELLOW + "  Source IP:" + ConsoleColors.RESET
                + " " + sourceIP.getAddress());
        System.out.println(ConsoleColors.YELLOW + "  Destination IP:" + ConsoleColors.RESET
                + " " + destinationIP.getAddress());
    }
}
