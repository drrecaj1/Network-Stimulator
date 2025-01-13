package main.java.connections;

import java.util.Random;
import main.java.model.Connection;
import main.java.model.Device;
import main.java.model.Packet;


public class WirelessConnection extends Connection {
    private double signalStrength; // Simulate interference (F5)

    // Constructor
    public WirelessConnection(Device device1, Device device2, double dataRate, double signalStrength) {
        super(device1, device2, dataRate, 0.1); // Error rate for wireless
        this.signalStrength = signalStrength;
    }

    // Implement signal strength check for wireless
    @Override
    public boolean isSignalStrong() {
        System.out.println("DEBUG: Checking wireless signal strength: " + signalStrength);
        return signalStrength >= 50; // Return true if signal strength >= 50
    }

    // Requires signal check - Wireless connections only
    @Override
    public boolean requiresSignalCheck() {
        return true; // Wireless requires signal checks
    }

    @Override
    public void transmitPacket(Packet packet) {
        simulateDelay(packet); // Add delay simulation (F6)

        // Step 1: Weak Signal Check - Call isSignalStrong()
        if (!isSignalStrong()) { // Weak signal
            System.out.println("Packet corrupted due to weak signal or interference!");
            return; // Immediate exit after corruption
        }

        // Step 2: Random corruption check
        Random random = new Random();
        boolean randomError = random.nextDouble() < errorRate; // Use error rate
        if (randomError) {
            System.out.println("Packet corrupted due to random errors!");
            return; // Immediate exit after corruption
        }

        // Step 3: Successful Transmission
        System.out.println("DEBUG: WiFi signal strong. Transmitting packet.");
        device2.receivePacket(packet); // Successfully transmit the packet
    }
    // Getter for signal strength
    public double getSignalStrength() {
        return signalStrength;
    }

}
