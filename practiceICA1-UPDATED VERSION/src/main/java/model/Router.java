package main.java.model;

public class Router extends Device {
    public Router(String name) {
        super(name);
    }

    @Override
    public void sendPacket(Packet packet, Device receiver) {
        // Retrieve the destination address from the packet headers dynamically
        NetworkAddress destinationAddress = packet.getHeader("IP"); // "IP" layer header

        if (destinationAddress != null) {
            System.out.println("Router " + name + " routing packet to " + destinationAddress.getAddress());
        } else {
            System.out.println("Router " + name + " could not find a valid destination address.");
            return; // Stop processing if destination address is missing
        }

        // Pass the packet to the receiver
        receiver.receivePacket(packet);
    }

    @Override
    public void receivePacket(Packet packet) {
        // Fetch source and destination addresses from packet headers
        NetworkAddress sourceAddress = packet.getHeader("IP_SOURCE");
        NetworkAddress destinationAddress = packet.getHeader("IP_DESTINATION");

        if (sourceAddress == null || destinationAddress == null) {
            System.out.println("Router " + name + ": Missing IP headers. Cannot process packet.");
            packet.drop();
            return;
        }

        System.out.println("Router " + name + " received packet from: " +
                sourceAddress.getAddress());

        // Forward the packet to its next hop (destination or another device)
        for (Connection connection : connections) {
            Device nextHop = connection.device2; // Assuming device2 is the other end
            if (!nextHop.equals(packet.getSource())) { // Avoid sending back to the sender
                System.out.println("Router " + name + " routing packet to: " +
                        destinationAddress.getAddress());
                nextHop.receivePacket(packet); // Forward packet
                return; // Exit after forwarding
            }
        }

        System.out.println("Router " + name + ": No route found for packet!");
    }
}
