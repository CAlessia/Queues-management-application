package Model;

import BusinessLogic.SimulationTimer;
import BusinessLogic.SimulationManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks ;
    private AtomicInteger waitingPeriod ;
    private volatile Task crtTask;

    public Server() {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
    }

    public Server(BlockingQueue<Task> tasks, AtomicInteger waitingPeriod) {
        this.tasks = tasks;
        this.waitingPeriod = waitingPeriod;
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }

    public Task getCrtTask() {
        return crtTask;
    }

    public int getQueueSize() {
        return tasks.size()+ (crtTask != null ? 1 : 0);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task task = tasks.take();
                task.setStartTime(SimulationTimer.getCurrentTime());
                SimulationManager.getCompletedTasks().add(task);
                crtTask = task;

                while (task.getServiceTime() > 0) {
                    Thread.sleep(1000);
                    task.decreaseServiceTime();
                    waitingPeriod.decrementAndGet();
                }
                crtTask = null;

                SimulationManager.getCompletedTasks().add(task);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
