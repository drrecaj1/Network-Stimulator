package main.java.model;

public class MACAddress implements NetworkAddress {
    private String mac;

    public MACAddress(String mac) {
        this.mac = mac;
    }

    @Override
    public String getAddress() {
        return mac;
    }
}
