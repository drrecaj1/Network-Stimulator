package main.java.model;

import java.util.Random;

public class DeviceFactory {
    public static Device createDevice(String type, String name) {
        Device device;
        if (type.equalsIgnoreCase("Router")) {
            device = new Router(name);
        } else if (type.equalsIgnoreCase("Computer")) {
            device = new Computer(name);
        } else {
            throw new IllegalArgumentException("Unknown device type");
        }

        // Assign a random MAC address
        device.setMacAddress(new MACAddress(generateRandomMAC()));
        return device;
    }

    // Helper method to generate a random MAC address
    private static String generateRandomMAC() {
        StringBuilder mac = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            mac.append(String.format("%02X", random.nextInt(256)));
            if (i < 5) mac.append(":");
        }
        return mac.toString();
    }
}
