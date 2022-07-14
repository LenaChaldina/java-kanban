package tz3.service;

import tz3.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManagerService {
    private List<Task> tasksHistory = new ArrayList<>();
    private List<Task> viewedTasks = new ArrayList<>();

    @Override
    public List<Task> getTasksHistory() {
        return tasksHistory;
    }

    @Override
    public void add(Task task) {
        if (tasksHistory.contains(task)) {
            viewedTasks.add(task);
        }
    }
}
