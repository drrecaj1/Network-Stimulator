package main.java.model;

public class DeviceFactory {
    public static Device createDevice(String type, String name) {
        if (type.equalsIgnoreCase("Router")) {
            return new Router(name);
        } else if (type.equalsIgnoreCase("Computer")) {
            return new Computer(name);
        }
        throw new IllegalArgumentException("Unknown device type");
    }
}
