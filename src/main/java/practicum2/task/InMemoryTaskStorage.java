package practicum2.task;

import java.time.LocalDateTime;
import java.util.HashMap;

import static practicum.constants.Status.NEW;

public class InMemoryTaskStorage implements TaskStorage {
    private final HashMap<Integer, InMemTaskData> taskMap = new HashMap<>();
    private int generator = 0;

    @Override
    public Task addTask(String name, String description) {
        int taskId = generator++;
        InMemTaskData taskData = new InMemTaskData(taskId, name, description, NEW, LocalDateTime.now(), LocalDateTime.now());
        taskMap.put(taskId, taskData);
        return new InMemoryTask(taskMap, taskId);
    }
}
