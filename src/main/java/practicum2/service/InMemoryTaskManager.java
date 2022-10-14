package practicum2.service;


import practicum.constants.Status;
import practicum2.task.Epic;
import practicum2.task.InMemTaskData;
import practicum2.task.InMemoryTask;
import practicum2.task.Subtask;
import practicum2.task.Task;
import practicum2.task.TheTask;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static practicum.constants.Status.*;


public class InMemoryTaskManager implements TaskManagerService {
    protected final HashMap<Integer, InMemTaskData> taskStorage = new HashMap<>();
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
        InMemTaskData taskData = new InMemTaskData(taskId, name, description, NEW, LocalDateTime.now(), LocalDateTime.now());
        taskStorage.put(taskId, taskData);
        return new InMemoryTask(taskStorage, taskId);
    }

    @Override
    public void addEpic(String name, String description, LocalDateTime endTime) {

    }

    @Override
    public void addSubtask(String name, String description) {

    }


}