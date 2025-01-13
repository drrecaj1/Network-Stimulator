package main.java.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Device {
    protected String name;
    private MACAddress macAddress; // MAC Address field
    private IPAddress ipAddress;  // IP Address field
    public List<Connection> connections; // Stores all connections (wired and wireless)

    // Constructor
    public Device(String name) {
        this.name = name;
        this.connections = new ArrayList<>();
        this.macAddress = new MACAddress(generateRandomMAC()); // Assign a unique MAC address
    }

    // Add connection to the device
    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public String getName() {
        return name;
    }

    // Generate a random MAC address (for simulation purposes)
    private String generateRandomMAC() {
        StringBuilder mac = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            mac.append(String.format("%02X", (int) (Math.random() * 256)));
            if (i < 5) mac.append(":");
        }
        return mac.toString();
    }

    // Getter for MAC address
    public MACAddress getMacAddress() {
        return macAddress;
    }

    // Setter for MAC address (if needed)
    public void setMacAddress(MACAddress macAddress) {
        this.macAddress = macAddress;
    }

    // Getter for IP address
    public IPAddress getIpAddress() {
        return ipAddress;
    }

    // Setter for IP address
    public void setIpAddress(IPAddress ipAddress) {
        this.ipAddress = ipAddress;
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
