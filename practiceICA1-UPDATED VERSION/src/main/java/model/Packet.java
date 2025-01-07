package main.java.model;

public class Packet {
    private String data;
    private Device source;
    private Device destination;
    private String sourceIP;
    private String destinationIP;
    private boolean dropped; // NEW: Track whether packet is dropped

    public Packet(String data, Device source, Device destination, String sourceIP, String destinationIP) {
        this.data = data;
        this.source = source;
        this.destination = destination;
        this.sourceIP = sourceIP;
        this.destinationIP = destinationIP;
        this.dropped = false; // Default to false
    }

    public String getData() {
        return data;
    }

    public Device getSource() {
        return source;
    }

    public Device getDestination() {
        return destination;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    // NEW: Mark packet as dropped
    public void drop() {
        dropped = true;
    }

    // NEW: Check if packet is dropped
    public boolean isDropped() {
        return dropped;
    }
}
