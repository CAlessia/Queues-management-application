package BusinessLogic;

import GUI.SimulationFrame;
import Model.Task;
import Model.Server;

import javax.swing.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class SimulationManager implements Runnable {
    public Scheduler scheduler;
    private List<Task> tasks;
    private SimulationFrame frame;
    private int totalServiceTime = 0;
    private int totalWaitingTime = 0;
    private int totalServedClients = 0;
    private int peakHour = 0;
    private int maxTasksInQueues = 0;
    private static final List<Task> completedTasks = Collections.synchronizedList(new ArrayList<>());

    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public int minArrivalTime;
    public int maxArrivalTime;
    public static SelectionPolicy selectionPolicy;

    public SimulationManager() {}

    public static List<Task> getCompletedTasks() {
        return completedTasks;
    }

    public List<Task> generateRandomTasks() {
        tasks = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < numberOfClients; i++) {
            int arrivalTime = rand.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = rand.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;
            tasks.add(new Task(i + 1, arrivalTime, serviceTime));
        }
        tasks.sort(Comparator.comparingInt(Task::getArrivalTime));
        return tasks;
    }

    public void setFrame(SimulationFrame frame) {
        this.frame = frame;
    }

    @Override
    public void run() {  File logFile = new File("simulation_3.txt");

        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                startServerThreads();
                int currentTime = 0;
                while (currentTime <= timeLimit) {
                    SimulationTimer.setCurrentTime(currentTime);

                    dispatchArrivingTasks(currentTime);
                    collectPeakHourData(currentTime);
                    String log = generateLog(currentTime);

                    if (frame != null) {
                        SwingUtilities.invokeLater(() -> frame.appendLog(log));
                    }
                    writer.write(log);
                    writer.flush();

                    currentTime++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                finishSimulation(writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startServerThreads() {
        for (Server server : scheduler.getServers()) {
            new Thread(server).start();
        }
    }

    private void dispatchArrivingTasks(int currentTime) {
        List<Task> toRemove = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getArrivalTime() == currentTime) {
                scheduler.dispatchTask(task);
                totalServiceTime += task.getServiceTime();
                totalServedClients++;
                toRemove.add(task);
            }
        }
        tasks.removeAll(toRemove);
    }

    private void collectPeakHourData(int currentTime) {
        int currentTotalQueueSize = scheduler.getServers().stream()
                .mapToInt(Server::getQueueSize)
                .sum();
        if (currentTotalQueueSize > maxTasksInQueues) {
            maxTasksInQueues = currentTotalQueueSize;
            peakHour = currentTime;
        }
    }

    private String generateLog(int currentTime) {
        StringBuilder log = new StringBuilder();
        log.append("Time ").append(currentTime).append("\n");

        log.append("Waiting clients: ");
        for (Task task : tasks) {
            if (task.getArrivalTime() > currentTime) {
                log.append(task.toString()).append(" ");
            }
        }
        log.append("\n");

        List<Server> servers = scheduler.getServers();
        for (int i = 0; i < servers.size(); i++) {
            log.append("Queue ").append(i + 1).append(": ");
            logServerQueueStatus(servers.get(i), log);
            log.append("\n");
        }

        return log.toString();
    }

    private void logServerQueueStatus(Server server, StringBuilder log) {
        Task currentTask = server.getCrtTask();
        if (currentTask == null && server.getTasks().length == 0) {
            log.append("closed");
        } else {
            if (currentTask != null) {
                log.append(currentTask.toString()).append("; ");
            }
            for (Task t : server.getTasks()) {
                log.append(t.toString()).append("; ");
            }
        }
    }

    private void finishSimulation(BufferedWriter writer) throws IOException {
        synchronized (completedTasks) {
            for (Task task : completedTasks) {
                totalWaitingTime += task.getWaitingTime();
            }
        }

        double averageWaitingTime = totalServedClients == 0 ? 0 : (double) totalWaitingTime / totalServedClients;
        double averageServiceTime = numberOfClients == 0 ? 0 : (double) totalServiceTime / numberOfClients;

        StringBuilder finalStats = new StringBuilder();
        finalStats.append("Average Waiting Time: ").append(String.format("%.2f", averageWaitingTime)).append("\n");
        finalStats.append("Average Service Time: ").append(String.format("%.2f", averageServiceTime)).append("\n");
        finalStats.append("Peak Hour: ").append(peakHour).append("\n");

        writer.write(finalStats.toString());
        writer.flush();

        if (frame != null) {
            SwingUtilities.invokeLater(() -> {
                frame.appendLog(finalStats.toString());
            });
        }
    }

}
