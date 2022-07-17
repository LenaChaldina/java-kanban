package practicum.service;

import practicum.task.Task;

import java.util.LinkedList;

public interface HistoryManagerService {
    LinkedList<Task> getTasksHistory();

    void addTask(Task task);
}
