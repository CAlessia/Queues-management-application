package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server crtServer = null;
        int minWait = Integer.MAX_VALUE;;
        for(Server s : servers) {
            int serverWaitTime = s.getWaitingPeriod().get();
            if(crtServer == null || serverWaitTime < minWait) {
                minWait = serverWaitTime;
                crtServer = s;
            }
        }
        if(crtServer != null) {
            crtServer.addTask(task);
        }

    }
}
