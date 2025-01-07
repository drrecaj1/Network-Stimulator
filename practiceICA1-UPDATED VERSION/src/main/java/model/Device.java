package main.java.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Device {
    protected String name;
    public List<Connection> connections; // Stores all connections (wired and wireless)

    // Constructor
    public Device(String name) {
        this.name = name;
        this.connections = new ArrayList<>();
    }

    // Add connection to the device
    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public String getName() {
        return name;
    }

    // MODIFIED sendPacket() - Handles Wireless and Wired Connections
    public void sendPacket(Packet packet, Device receiver) {
        for (Connection connection : connections) {
            // Check if signal strength matters
            if (connection.requiresSignalCheck()) {
                if (!connection.isSignalStrong()) { // Weak signal
                    return; // Stop processing packet due to weak signal
                }
            }

            // Transmit the packet using the correct method
            connection.transmitPacket(packet);
        }
    }




    // Abstract methods for receiving packets
    public abstract void receivePacket(Packet packet);
}
