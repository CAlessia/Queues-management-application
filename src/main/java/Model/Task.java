package Model;

public class Task {
    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int startTime=-1;
    private int waitingTime = 0;

    public Task(){}

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public void setStartTime(int time) {
        this.startTime = time;
        this.waitingTime = this.startTime - this.arrivalTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public void decreaseServiceTime(){
        if(serviceTime > 0){
            serviceTime--;
        }
    }

    public int getWaitingTime() {
        if (startTime == -1) return 0;
        return startTime - arrivalTime;
    }

    @Override
    public String toString() {
        return "(" + id + "," + arrivalTime + "," + serviceTime + ")";
    }
}
