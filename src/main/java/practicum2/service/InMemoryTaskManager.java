package practicum2.service;


import practicum2.task.Epic;
import practicum2.task.Subtask;
import practicum2.task.Task;
import practicum2.task.TheTask;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class InMemoryTaskManager implements TaskManagerService {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    private int generator = 0;


    @Override
    public Map<Integer, Task> getResultTasks() {
        return null;
    }

    @Override
    public Map<Integer, Subtask> getResultSubtasks() {
        return null;
    }

    @Override
    public Map<Integer, Epic> getResultEpics() {
        return null;
    }

    @Override
    public Task addTask(String name, String description) {
        int taskId = generator++;
        Task task = new TheTask(taskId, name, description);
        tasks.put(taskId, task);
        return task;
    }

    @Override
    public void addEpic(String name, String description, LocalDateTime endTime) {

    }

    @Override
    public void addSubtask(String name, String description) {

    }


}