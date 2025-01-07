package main.java.main;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

// Import models
import main.java.model.Connection;
import main.java.model.Device;
import main.java.model.DeviceFactory;
import main.java.model.Packet;


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
            // Load topology from external file
            try {
                devices = TopologyLoader.loadDevices("main/resource/topology.txt"); // Load from external file

                // Find wireless connection (example assumes it exists)
                for (Device device : devices) {
                    for (Connection connection : device.connections) {
                        if (connection instanceof WirelessConnection) {
                            wireless = (WirelessConnection) connection; // Capture wireless connection
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading topology file: " + e.getMessage());
                return; // Stop execution if file loading fails
            }
        } else {
            // Hardcoded topology in source code
            Device device1 = DeviceFactory.createDevice("Computer", "PC1");
            Device device2 = DeviceFactory.createDevice("Router", "Router1");
            Device device3 = DeviceFactory.createDevice("Computer", "PC2");

            devices.add(device1);
            devices.add(device2);
            devices.add(device3);

            Connection wired = new WiredConnection(device1, device2, 100);
            wireless = new WirelessConnection(device2, device3, 54, 60); // Use 60 signal strength

            device1.addConnection(wired);
            device2.addConnection(wired);
            device2.addConnection(wireless);
            device3.addConnection(wireless);
        }

        // Add layers (Physical, Network, Ethernet, and WiFi)
        layers.add(new PhysicalLayer());
        layers.add(new NetworkLayer());
        layers.add(new Ethernet());
        layers.add(new WiFi(wireless)); // Pass wireless connection reference to WiFi layer
        layers.add(new IPProtocol());
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void runSimulation() {
        // Create and send packet
        Packet packet = new Packet("Hello, Network!", devices.get(0), devices.get(2), "192.168.1.1", "192.168.1.2");

        // Process the packet through all layers
        for (Layer layer : layers) {
            layer.processPacket(packet);

            // **NEW CHECK** - Stop processing if the packet is dropped
            if (packet.isDropped()) { // Check if packet was marked as dropped
                System.out.println("Simulation stopped. Packet dropped during processing.");
                logger.logEvent("Packet dropped before reaching the destination.");
                totalPacketsDropped++; // Increment dropped counter
                printStatistics(); // Print stats immediately for dropped packets
                return; // Stop simulation early
            }
        }

        // If not dropped, simulate transmission
        devices.get(0).sendPacket(packet, devices.get(2));
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
        System.out.println("\n--- Simulation Statistics ---");
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
