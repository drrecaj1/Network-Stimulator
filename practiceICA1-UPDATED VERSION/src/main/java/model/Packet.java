package main.java.model;

import java.util.HashMap;
import java.util.Map;

public class Packet {
    private String data;
    private Device source;
    private Device destination;
    private Map<String, NetworkAddress> headers; // Key: Header type, Value: NetworkAddress
    private boolean dropped;

    public Packet(String data, Device source, Device destination) {
        this.data = data;
        this.source = source;
        this.destination = destination;
        this.headers = new HashMap<>();
        this.dropped = false;
    }

    // Getter and Setter for packet data
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    // Source and Destination Devices
    public Device getSource() {
        return source;
    }

    public Device getDestination() {
        return destination;
    }

    // Add a header for a specific layer
    public void addHeader(String headerName, NetworkAddress address) {
        headers.put(headerName, address);
    }

    // Get a header for a specific layer
    public NetworkAddress getHeader(String headerName) {
        return headers.get(headerName); // Return NetworkAddress directly
    }

    // Drop packet
    public void drop() {
        dropped = true;
    }

    // Check if packet is dropped
    public boolean isDropped() {
        return dropped;
    }
}
