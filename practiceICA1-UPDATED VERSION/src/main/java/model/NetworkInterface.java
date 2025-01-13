package main.java.model;

public class NetworkInterface {
    private String type; // Ethernet, WiFi, etc.
    private String port; // Interface port (e.g., Gig0/1)
    private String bandwidth; // Bandwidth (e.g., 1 Gbps)
    private double signalStrength; // Optional, for WiFi

    public NetworkInterface(String type, String port, String bandwidth, double signalStrength) {
        this.type = type;
        this.port = port;
        this.bandwidth = bandwidth;
        this.signalStrength = signalStrength;
    }

    public NetworkInterface(String type, String port, String bandwidth) {
        this(type, port, bandwidth, 0.0);
    }

    public String getType() {
        return type;
    }

    public String getPort() {
        return port;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public double getSignalStrength() {
        return signalStrength;
    }

    @Override
    public String toString() {
        return type + " (" + port + ", " + bandwidth +
                (signalStrength > 0 ? ", Signal Strength: " + signalStrength + "%" : "") + ")";
    }
}
