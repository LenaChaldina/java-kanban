package practicum2.task;

import practicum.constants.Status;

public interface TaskStorage {
    Task addTask(String name, String description);

    void setStatus(Task task, Status status);
}
