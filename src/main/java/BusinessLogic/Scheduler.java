package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler() {
    }

    public Scheduler(List<Server> servers, int maxTasksPerServer, Strategy strategy, int maxNoServers) {
        this.servers = servers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.strategy = strategy;
        this.maxNoServers = maxNoServers;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public int getMaxNoServers() {
        return maxNoServers;
    }

    public void setMaxNoServers(int maxNoServers) {
        this.maxNoServers = maxNoServers;
    }

    public int getMaxTasksPerServer() {
        return maxTasksPerServer;
    }

    public void setMaxTasksPerServer(int maxTasksPerServer) {
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void changeStrategy(SelectionPolicy selectionPolicy) {
        switch (selectionPolicy) {
            case SHORTEST_QUEUE:
                this.strategy = new ShortestQueueStrategy();
                break;
            case SHORTEST_TIME:
                this.strategy = new TimeStrategy();
                break;
        }
    }

    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
    }

}
