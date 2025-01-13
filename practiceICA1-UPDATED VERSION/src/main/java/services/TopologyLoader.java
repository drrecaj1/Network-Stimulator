package main.java.services;

import java.io.*;
import java.util.*;

import main.java.model.*;
import main.java.connections.WiredConnection;
import main.java.connections.WirelessConnection;
import main.java.services.ConsoleColors;

public class TopologyLoader {
    public static List<Device> loadDevices(String filePath) throws IOException {
        List<Device> devices = new ArrayList<>();
        Map<String, Device> deviceMap = new HashMap<>();
        InputStream inputStream = TopologyLoader.class.getClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        int ipSuffix = 1; // IP address suffix for dynamic assignment
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals("Device")) {
                // Create the device and assign MAC and IP addresses
                Device device = DeviceFactory.createDevice(parts[1], parts[2]);
                device.setMacAddress(new MACAddress(generateRandomMAC()));
                device.setIpAddress(new IPAddress("192.168.1." + ipSuffix++));

                devices.add(device);
                deviceMap.put(parts[2], device);
            } else if (parts[0].equals("Connection")) {
                Device d1 = deviceMap.get(parts[2]);
                Device d2 = deviceMap.get(parts[3]);
                double rate = Double.parseDouble(parts[4]);


                // Log connection type
                if (parts[1].equals("Wired")) {
                    System.out.println("\nWired Connection: " + d1.getName() + ConsoleColors.RED + " <--> " + ConsoleColors.RESET + d2.getName() + " at " + rate + " Mbps");
                    d1.addConnection(new WiredConnection(d1, d2, rate));
                } else if (parts[1].equals("Wireless")) {
                    double signal = Double.parseDouble(parts[5]);
                    System.out.println("Wireless Connection: " + d1.getName() + ConsoleColors.BLUE + " <--> " + ConsoleColors.RESET + d2.getName() + " at " + rate + " Mbps, Signal Strength: " + signal);
                    d1.addConnection(new WirelessConnection(d1, d2, rate, signal));
                }
            }
        }
        reader.close();
        return devices;
    }

    // Generate a random MAC address
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
