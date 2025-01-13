package main.java.services;

import java.util.ArrayList;
import java.util.List;

public class SimulationLogger {
    private List<String> logs;

    public SimulationLogger() {
        logs = new ArrayList<>();
    }

    public void logEvent(String event) {
        logs.add(event);
        System.out.println("Log: " + event);
    }

    public List<String> getLogs() {
        return logs;
    }
}
