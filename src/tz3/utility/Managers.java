package tz3.utility;

import tz3.service.HistoryManagerService;
import tz3.service.InMemoryHistoryManager;
import tz3.service.InMemoryTaskManager;
import tz3.service.TaskManagerService;

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

