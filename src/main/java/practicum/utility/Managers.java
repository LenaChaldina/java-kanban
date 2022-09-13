package practicum.utility;

import practicum.service.HistoryManagerService;
import practicum.service.InMemoryHistoryManager;
import practicum.service.InMemoryTaskManager;
import practicum.service.TaskManagerService;

public class Managers {
    private Managers(){}
    public static HistoryManagerService getInMemoryHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManagerService getInMemoryTaskManager() {
        return new InMemoryTaskManager(getInMemoryHistoryManager());
    }

}

