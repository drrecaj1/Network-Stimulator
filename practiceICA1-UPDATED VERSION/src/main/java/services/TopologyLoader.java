package main.java.services;

import java.io.*;
import java.util.*;

import main.java.model.DeviceFactory;
import main.java.model.Device;
import main.java.connections.WiredConnection;
import main.java.connections.WirelessConnection;

public class TopologyLoader {
    public static List<Device> loadDevices(String filePath) throws IOException {
        List<Device> devices = new ArrayList<>();
        Map<String, Device> deviceMap = new HashMap<>();
        InputStream inputStream = TopologyLoader.class.getClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals("Device")) {
                Device device = DeviceFactory.createDevice(parts[1], parts[2]);
                devices.add(device);
                deviceMap.put(parts[2], device);
            } else if (parts[0].equals("Connection")) {
                Device d1 = deviceMap.get(parts[2]);
                Device d2 = deviceMap.get(parts[3]);
                double rate = Double.parseDouble(parts[4]);

                // Log connection type
                if (parts[1].equals("Wired")) {
                    System.out.println("Wired Connection: " + d1.getName() + " <--> " + d2.getName() + " at " + rate + " Mbps");
                    d1.addConnection(new WiredConnection(d1, d2, rate));
                } else if (parts[1].equals("Wireless")) {
                    double signal = Double.parseDouble(parts[5]);
                    System.out.println("Wireless Connection: " + d1.getName() + " <--> " + d2.getName() + " at " + rate + " Mbps, Signal Strength: " + signal);
                    d1.addConnection(new WirelessConnection(d1, d2, rate, signal));
                }
            }
        }
        reader.close();
        return devices;
    }
}
