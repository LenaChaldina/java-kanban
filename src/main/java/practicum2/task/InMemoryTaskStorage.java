package practicum2.task;

import practicum.constants.Status;

import java.time.LocalDateTime;
import java.util.HashMap;

import static practicum.constants.Status.NEW;

public class InMemoryTaskStorage implements TaskStorage {
    private final HashMap<Integer, InMemTaskData> tasksMap = new HashMap<>();
    private final HashMap<Integer, Status> taskStatuses = new HashMap<>();
    private int generator = 0;

    @Override
    public Task addTask(String name, String description) {
        int taskId = generator++;
        InMemTaskData taskData = new InMemTaskData(taskId, name, description, /*NEW,*/ LocalDateTime.now(), LocalDateTime.now());
        tasksMap.put(taskId, taskData);
        taskStatuses.put(taskId, NEW);
        return new InMemoryTask(tasksMap, taskStatuses, taskId);
    }

    @Override
    public void setStatus(Task task, Status status) {
        taskStatuses.put(task.id(), status);
    }

}
