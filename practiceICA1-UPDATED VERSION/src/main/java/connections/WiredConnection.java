package main.java.connections;

import main.java.model.Connection;
import main.java.model.Packet;
import main.java.model.Device;


public class WiredConnection extends Connection {

    public WiredConnection(Device device1, Device device2, double dataRate) {
        super(device1, device2, dataRate, 0.01); // Lower error rate for wired
    }

    // Implement signal check - Always strong for wired
    @Override
    public boolean isSignalStrong() {
        return true; // Wired connections are always strong
    }

    @Override
    public boolean requiresSignalCheck() {
        return false; // No signal check needed for wired connections
    }

    @Override
    public void transmitPacket(Packet packet) {
        simulateDelay(packet); // Add delay simulation (F6)

        if (isCorrupted()) { // Simulate corruption check (F5)
            System.out.println("DEBUG: Packet corrupted due to interference!");
        } else {
            System.out.println("DEBUG: Packet successfully transmitted via wired connection.");
            device2.receivePacket(packet); // Successfully transmit the packet
        }
    }
}
