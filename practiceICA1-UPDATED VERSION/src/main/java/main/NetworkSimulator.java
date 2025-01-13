package main.java.main;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

// Import models
import main.java.model.*;


// Import layers
import main.java.layers.Ethernet;
import main.java.layers.IPProtocol;
import main.java.layers.NetworkLayer;
import main.java.layers.PhysicalLayer;
import main.java.layers.WiFi;
import main.java.layers.Layer;

// Import connections
import main.java.connections.WiredConnection;
import main.java.connections.WirelessConnection;

// Import services
import main.java.services.ConsoleColors;
import main.java.services.SimulationLogger;
import main.java.services.TopologyLoader;

public class NetworkSimulator {
    private List<Device> devices;
    private List<Layer> layers;
    private SimulationLogger logger;

    // Statistics Variables for Enhanced Logging (F9)
    private int totalPacketsSent = 0;
    private int totalPacketsDropped = 0;
    private double totalDelay = 0.0;
    private int processedPackets = 0;

    public NetworkSimulator(boolean useExternalFile) {
        devices = new ArrayList<>();
        layers = new ArrayList<>();
        logger = new SimulationLogger();

        // Declare WirelessConnection placeholder
        WirelessConnection wireless = null;

        if (useExternalFile) {
            try {
                devices = TopologyLoader.loadDevices("main/resource/topology.txt");

                // Find the wireless connection for the WiFi layer
                for (Device device : devices) {
                    for (Connection connection : device.connections) {
                        if (connection instanceof WirelessConnection) {
                            wireless = (WirelessConnection) connection; // Capture the wireless connection
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading topology file: " + e.getMessage());
                return;
            }
        } else {
            // Hardcoded topology in source code
            Device device1 = DeviceFactory.createDevice("Computer", "PC1");
            device1.setIpAddress(new IPAddress("192.168.1.1"));
            device1.addInterface(new NetworkInterface("Ethernet", "Gig0/1", "1 Gbps"));

            Device device2 = DeviceFactory.createDevice("Router", "Router1");
            device2.setIpAddress(new IPAddress("192.168.1.254"));
            device2.addInterface(new NetworkInterface("Ethernet", "Gig0/1", "1 Gbps"));
            device2.addInterface(new NetworkInterface("WiFi", "WiFi0", "54 Mbps", 60.0));

            Device device3 = DeviceFactory.createDevice("Computer", "PC2");
            device3.setIpAddress(new IPAddress("192.168.1.2"));
            device3.addInterface(new NetworkInterface("Ethernet", "Gig0/1", "100 Mbps"));

            devices.add(device1);
            devices.add(device2);
            devices.add(device3);

            Connection wired = new WiredConnection(device1, device2, 100);
            wireless = new WirelessConnection(device2, device3, 54, 60); // Use 60 signal strength

            device1.addConnection(wired);
            device2.addConnection(wired);
            device2.addConnection(wireless);
            device3.addConnection(wireless);

            System.out.println("\nWired Connection: " + device1.getName() + ConsoleColors.RED + " <--> " + ConsoleColors.RESET + device2.getName() + " at " + 100.0 + " Mbps");
            System.out.println("Wireless Connection: " + device2.getName() + ConsoleColors.BLUE + " <--> " + ConsoleColors.RESET + device3.getName() + " at " + 54.0 + " Mbps, Signal Strength: " + 60.0);
        }

        // Add layers (Physical, Network, Ethernet, and WiFi)
        layers.add(new PhysicalLayer());
        layers.add(new NetworkLayer());
        layers.add(new Ethernet());
        layers.add(new WiFi(wireless)); // Pass wireless connection reference to WiFi layer
        layers.add(new IPProtocol());

        logDeviceInterfaces();
    }

    private void logDeviceInterfaces() {
        System.out.println(ConsoleColors.PURPLE + "\n--- Device Interfaces ---" + ConsoleColors.RESET);
        for (Device device : devices) {
            System.out.println(device.getName() + " Interfaces:");
            for (NetworkInterface iface : device.getInterfaces()) {
                String signalStrengthInfo = iface.getType().equals("WiFi")
                        ? ", Signal Strength: " + iface.getSignalStrength()
                        : ""; // Only include signal strength for WiFi

                System.out.println("  Type: " + iface.getType() +
                        ", Port: " + iface.getPort() +
                        ", Bandwidth: " + iface.getBandwidth() +
                        signalStrengthInfo);
            }
        }
    }


    public void runSimulation() {
        System.out.println(ConsoleColors.CYAN + "\nSimulating packet transfer from " +
                devices.get(0).getName() + " to " + devices.get(2).getName() + "..." + ConsoleColors.RESET);

        Packet packet = new Packet("Hello, Network!", devices.get(0), devices.get(2));
//
        packet.addHeader("SourceIP", devices.get(0).getIpAddress());
        packet.addHeader("DestinationIP", devices.get(2).getIpAddress());



        // Process the packet through all layers
        for (Layer layer : layers) {
            layer.processPacket(packet);

            // **NEW CHECK** - Stop processing if the packet is dropped
            if (packet.isDropped()) { // Check if packet was marked as dropped
                System.out.println(ConsoleColors.RED + "Simulation stopped. Packet dropped during processing." + ConsoleColors.RESET);
                logger.logEvent("Packet dropped before reaching the destination.");
                totalPacketsDropped++; // Increment dropped counter
                printStatistics(); // Print stats immediately for dropped packets
                return; // Stop simulation early
            }
        }

        // If not dropped, simulate transmission
//        devices.get(0).sendPacket(packet, devices.get(2));
        System.out.println(ConsoleColors.BLUE + "\n--- Router Packet Routing ---" + ConsoleColors.RESET);
        Device router = devices.get(1); // Assuming router is the second device
        router.sendPacket(packet, devices.get(2));
        logger.logEvent("Packet sent from " + devices.get(0).getName() + " to " + devices.get(2).getName());

        // Update Statistics
        totalPacketsSent++; // Increment sent packets
        double delay = ((Connection) devices.get(0).connections.get(0)).simulateDelay(packet); // Track delay
        totalDelay += delay; // Sum up total delay
        processedPackets++;
        printStatistics(); // Print final statistics
    }

    // Enhanced Logging (F9) - Statistics Reporting
    private void printStatistics() {
        System.out.println(ConsoleColors.RED + "\n--- Simulation Statistics ---" + ConsoleColors.RESET);
        System.out.println("Total Packets Sent: " + totalPacketsSent);
        System.out.println("Total Packets Dropped: " + totalPacketsDropped);
        System.out.println("Packet Delivery Rate: " + (totalPacketsSent > 0 ?
                (100.0 * (totalPacketsSent - totalPacketsDropped) / totalPacketsSent) + "%" : "0%"));
        System.out.println("Average Transmission Delay: " +
                (processedPackets > 0 ? (totalDelay / processedPackets) + " ms" : "N/A"));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose how to run the network simulator:");
        System.out.println("1. Use Source Code Topology");
        System.out.println("2. Load Topology from External File");

        int choice = 0; // Initialize with default value

        // Input validation loop
        while (true) {
            System.out.print("Enter 1 or 2: ");
            String input = scanner.next(); // Read input as string

            // Check if input is a valid integer and either 1 or 2
            if (input.matches("[12]")) { // Regular expression to allow only '1' or '2'
                choice = Integer.parseInt(input); // Convert valid input to integer
                break; // Exit loop for valid input
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }

        // Pass user choice to constructor
        boolean useExternalFile = (choice == 2);
        NetworkSimulator simulator = new NetworkSimulator(useExternalFile);
        simulator.runSimulation();
        scanner.close(); // Close the scanner
    }
}
