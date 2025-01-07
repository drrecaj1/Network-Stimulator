package main.java.model;

public class Router extends Device {
    public Router(String name) {
        super(name);
    }

    @Override
    public void sendPacket(Packet packet, Device receiver) {
        System.out.println("Router " + name + " routing packet to " + receiver.getName());
        receiver.receivePacket(packet);
    }

    @Override
    public void receivePacket(Packet packet) {
        System.out.println("Router " + name + " received packet: " + packet.getData());
    }
}
