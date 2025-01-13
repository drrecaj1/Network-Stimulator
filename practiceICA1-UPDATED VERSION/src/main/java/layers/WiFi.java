package main.java.layers;

import main.java.connections.WirelessConnection;
import main.java.model.Packet;
import main.java.services.ConsoleColors;

public class WiFi implements Layer {
    private WirelessConnection connection;

    public WiFi(WirelessConnection connection) {
        this.connection = connection;
    }

    @Override
    public void processPacket(Packet packet) {
        // Processing WiFi frame
        System.out.println(ConsoleColors.BLUE + "\nWiFi Layer:"

                + ConsoleColors.RESET
                + " Processing WiFi Frame: " + packet.getData() + ConsoleColors.RESET);

        // DEBUG Signal Strength
        double signalStrength = connection.getSignalStrength();
        System.out.println(ConsoleColors.GREEN + "Signal Strength:" + ConsoleColors.RESET
                + " " + signalStrength + ConsoleColors.RESET);

        // Check signal strength
        if (!connection.isSignalStrong()) {
            System.out.println(ConsoleColors.RED + "WiFi:" + ConsoleColors.RESET + " Weak signal detected. Dropping packet.")
                    ;
            packet.drop();
            return;
        }

        // Add MAC headers
        String sourceMAC = packet.getSource().getMacAddress().getAddress();
        String destinationMAC = packet.getDestination().getMacAddress().getAddress();
        packet.addHeader("SourceMAC", packet.getSource().getMacAddress());
        packet.addHeader("DestinationMAC", packet.getDestination().getMacAddress());

        // Log transmission
        System.out.println(ConsoleColors.GREEN + "WiFi:" + ConsoleColors.RESET + " Strong signal detected. Transmitting packet."
                );
        System.out.println(ConsoleColors.YELLOW + "  Source MAC:" + ConsoleColors.RESET
                + " " + sourceMAC);
        System.out.println(ConsoleColors.YELLOW + "  Destination MAC:" + ConsoleColors.RESET
                + " " + destinationMAC);

        // Transition to the next layer
        System.out.println(ConsoleColors.PURPLE + "Transitioning to the IP Layer..."
                + ConsoleColors.RESET);
    }
}

