package main.java.layers;

import main.java.model.Packet;

import main.java.services.ConsoleColors;

public class Ethernet implements Layer {
    @Override
    public void processPacket(Packet packet) {
        // Fetch MAC addresses dynamically
        String sourceMAC = packet.getSource().getMacAddress().getAddress();
        String destinationMAC = packet.getDestination().getMacAddress().getAddress();

        // Add MAC addresses to headers
        packet.addHeader("SourceMAC", packet.getSource().getMacAddress());
        packet.addHeader("DestinationMAC", packet.getDestination().getMacAddress());

        // Log processing
        System.out.println(ConsoleColors.BLUE + "\nEthernet Layer:" + ConsoleColors.RESET + " Processing Ethernet Frame");
        System.out.println(ConsoleColors.YELLOW + "  Source MAC:" + ConsoleColors.RESET + " " + packet.getSource().getMacAddress().getAddress());
        System.out.println(ConsoleColors.YELLOW + "  Destination MAC:" + ConsoleColors.RESET + " " + packet.getDestination().getMacAddress().getAddress());
        System.out.println(ConsoleColors.PURPLE + "Transitioning to the WiFi Layer..." + ConsoleColors.RESET);
    }
    }


