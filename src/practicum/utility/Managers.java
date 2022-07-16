package practicum.utility;

import practicum.service.HistoryManagerService;
import practicum.service.InMemoryHistoryManager;
import practicum.service.InMemoryTaskManager;
import practicum.service.TaskManagerService;

public class Managers {
    static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(inMemoryHistoryManager);

    public static TaskManagerService getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManagerService getDefaultHistory() {
        return inMemoryHistoryManager;
    }

}

