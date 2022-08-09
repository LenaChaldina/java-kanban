package practicum.service;

import practicum.task.Task;

import java.util.List;

public interface HistoryManagerService{
    List<Task> getTasksHistory();

    void addTask(Task task);
    void removeTask(int id);
}
