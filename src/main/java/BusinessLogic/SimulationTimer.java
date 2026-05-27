package BusinessLogic;

public class SimulationTimer {
    private static int currentTime;

    public static synchronized void setCurrentTime(int time) {
        currentTime = time;
    }

    public static synchronized int getCurrentTime() {
        return currentTime;
    }
}
