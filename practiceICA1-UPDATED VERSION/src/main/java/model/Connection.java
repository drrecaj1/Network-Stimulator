package main.java.model;

import java.util.Random;

public abstract class Connection {
    protected Device device1;
    protected Device device2;
    protected double dataRate; // Speed in Mbps
    protected double errorRate; // Simulate environmental effects (F5)

    // Logging and Statistics (F9)
    protected double lastPacketDelay = 0.0; // Track delay per packet
    protected boolean lastPacketCorrupted = false; // Track corruption status

    public Connection(Device device1, Device device2, double dataRate, double errorRate) {
        this.device1 = device1;
        this.device2 = device2;
        this.dataRate = dataRate;
        this.errorRate = errorRate; // Environmental effect parameter
    }

    // Calculate transmission delay based on data rate (F6)
    public double simulateDelay(Packet packet) {
        double packetSize = packet.getData().length(); // Assume 1 byte per character
        double delay = (packetSize * 8) / (dataRate * 1000000); // Convert Mbps to bps

        try {
            Thread.sleep((long) (delay * 1000)); // Delay in milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Track delay for logging
        lastPacketDelay = delay * 1000; // Convert to milliseconds
        System.out.println("DEBUG: Simulated delay = " + lastPacketDelay + " ms");
        return lastPacketDelay;
    }

    // Simulate data corruption based on error rate (F5)
    protected boolean isCorrupted() {
        Random random = new Random();
        lastPacketCorrupted = random.nextDouble() < errorRate; // Probability of corruption

        // Log corruption status
        if (lastPacketCorrupted) {
            System.out.println("DEBUG: Packet corrupted due to environmental effects.");
        }
        return lastPacketCorrupted;
    }

    // NEW METHOD - Force each subclass to define signal strength behavior
    public abstract boolean isSignalStrong(); // Abstract to enforce override

    // Abstract method for transmitting packets
    public abstract void transmitPacket(Packet packet);

    // Force behavior - Abstract wireless check
    public abstract boolean requiresSignalCheck(); // Forces child to implement behavior

    // Logging helper for statistics (F9)
    public void logConnectionDetails() {
        System.out.println(device1.getName() + " <--> " + device2.getName() +
                " | Data Rate: " + dataRate + " Mbps | Last Delay: " + lastPacketDelay + " ms" +
                " | Corruption: " + (lastPacketCorrupted ? "Yes" : "No"));
    }
}



