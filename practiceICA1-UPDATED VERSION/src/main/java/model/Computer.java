package main.java.model;

public class Computer extends Device {
    public Computer(String name) {
        super(name);
    }

    @Override
    public void sendPacket(Packet packet, Device receiver) {
        System.out.println("\nComputer " + name + " sending packet to " + receiver.getName());
        receiver.receivePacket(packet);
    }

    @Override
    public void receivePacket(Packet packet) {
        System.out.println("Computer " + name + " received packet: " + packet.getData());
    }
}
