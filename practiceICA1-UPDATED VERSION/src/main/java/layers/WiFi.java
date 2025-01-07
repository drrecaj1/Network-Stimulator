package main.java.layers;

import main.java.connections.WirelessConnection;
import main.java.model.Packet;

public class WiFi implements Layer {
    private WirelessConnection connection; // Link to WirelessConnection


    public WiFi(WirelessConnection connection) {
        this.connection = connection; // Save reference
    }

    @Override
    public void processPacket(Packet packet) {
        System.out.println("Processing WiFi Frame: " + packet.getData());

        // Get signal strength dynamically from WirelessConnection
        double signalStrength = connection.getSignalStrength(); // Fetch actual signal strength


        // Handle weak signal
        if (signalStrength < 50) { // Threshold for weak signal
            packet.drop(); // NEW: Mark packet as dropped
            return; // Stop further processing in this layer
        }

        // Handle strong signal
        System.out.println("DEBUG: WiFi signal strong. Transmitting packet.");
    }
}
