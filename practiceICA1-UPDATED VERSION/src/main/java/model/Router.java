package main.java.model;

import main.java.services.ConsoleColors;

public class Router extends Device {
    public Router(String name) {
        super(name);
    }

    @Override
    public void sendPacket(Packet packet, Device receiver) {
        // Retrieve the destination address from the packet headers dynamically
        NetworkAddress destinationAddress = packet.getHeader("DestinationIP"); //  layer header

        if (destinationAddress != null) {
            System.out.println(ConsoleColors.CYAN + "Router " + name + ":" + ConsoleColors.RESET +
                    " Routing packet to " + destinationAddress.getAddress() + "...");
        } else {
            System.out.println(ConsoleColors.RED + "Router " + name + ":" + ConsoleColors.RESET +
                    " Could not find a valid destination address. Dropping packet.");
            packet.drop();
            return; // Stop processing if destination address is missing
        }

        // Simulate transmission details
        NetworkInterface outgoingInterface = this.getInterfaces().stream()
                .filter(iface -> iface.getType().equals("Ethernet") || iface.getType().equals("WiFi"))
                .findFirst()
                .orElse(null);

        if (outgoingInterface != null) {
            System.out.println(ConsoleColors.YELLOW + "Transmitting via Interface: " + ConsoleColors.RESET +
                    outgoingInterface.getPort() +
                    " (" + outgoingInterface.getType() + ", Bandwidth: " + outgoingInterface.getBandwidth() + ")");
        }

        // Find the connection to the receiver and simulate the actual transmission
        Connection connectionToReceiver = connections.stream()
                .filter(connection -> connection.device2.equals(receiver))
                .findFirst()
                .orElse(null);

        if (connectionToReceiver != null) {
            System.out.println(ConsoleColors.GREEN + "Router " + name + ":" + ConsoleColors.RESET +
                    " Sending packet to " + receiver.getName() + " via " +
                    connectionToReceiver.getClass().getSimpleName() + " connection.");
            connectionToReceiver.transmitPacket(packet);
        } else {
            System.out.println(ConsoleColors.RED + "Router " + name + ":" + ConsoleColors.RESET +
                    " No valid connection to " + receiver.getName() + ". Dropping packet.");
            packet.drop();
            return;
        }

        System.out.println(ConsoleColors.GREEN + "Router " + name + ":" + ConsoleColors.RESET +
                " Successfully sent packet to " + receiver.getName());
    }


    @Override
    public void receivePacket(Packet packet) {
        // Fetch source and destination addresses from packet headers
        NetworkAddress sourceAddress = packet.getHeader("SourceIP");
        NetworkAddress destinationAddress = packet.getHeader("DestinationIP");

        if (sourceAddress == null || destinationAddress == null) {
            System.out.println(ConsoleColors.RED + "Router " + name + ":" + ConsoleColors.RESET +
                    " Missing IP headers. Dropping packet.");
            packet.drop();
            return;
        }

        System.out.println(ConsoleColors.GREEN + "Router " + name + ":" + ConsoleColors.RESET +
                " Received packet from " + sourceAddress.getAddress() + " destined for " + destinationAddress.getAddress());

        // Log the interface handling the incoming packet
        NetworkInterface incomingInterface = this.getInterfaces().stream()
                .filter(iface -> iface.getType().equals("Ethernet") || iface.getType().equals("WiFi"))
                .findFirst()
                .orElse(null);

        if (incomingInterface != null) {
            System.out.println(ConsoleColors.YELLOW + "Processing on Interface: " + ConsoleColors.RESET +
                    incomingInterface.getPort() +
                    " (" + incomingInterface.getType() + ", Bandwidth: " + incomingInterface.getBandwidth() + ")");
        }

        // Forward the packet to its next hop (destination or another device)
        for (Connection connection : connections) {
            Device nextHop = connection.device2; // Assuming device2 is the other end
            if (!nextHop.equals(packet.getSource())) { // Avoid sending back to the sender
                System.out.println(ConsoleColors.CYAN + "Router " + name + ":" + ConsoleColors.RESET +
                        " Forwarding packet to " + nextHop.getName());
                sendPacket(packet, nextHop); // Forward packet
                return; // Exit after forwarding
            }
        }

        System.out.println(ConsoleColors.RED + "Router " + name + ":" + ConsoleColors.RESET +
                " No route found for packet! Dropping packet.");
        packet.drop();
    }
}
