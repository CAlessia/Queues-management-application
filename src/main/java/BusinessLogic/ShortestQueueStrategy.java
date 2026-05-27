package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server crtServer = null;
        int minQueueSize = Integer.MAX_VALUE;

        for (Server server : servers) {
            int queueSize = server.getQueueSize();
            if (queueSize < minQueueSize) {
                minQueueSize = queueSize;
                crtServer = server;
            }
        }

        if (crtServer != null) {
            crtServer.addTask(task);
        }
    }
}
