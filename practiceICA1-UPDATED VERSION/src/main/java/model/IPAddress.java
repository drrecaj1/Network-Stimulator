package main.java.model;

public class IPAddress implements NetworkAddress {
    private String ip;

    public IPAddress(String ip) {
        this.ip = ip;
    }

    @Override
    public String getAddress() {
        return ip;
    }
}
