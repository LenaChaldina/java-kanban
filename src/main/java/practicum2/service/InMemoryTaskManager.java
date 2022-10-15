package practicum2.service;


import practicum2.task.*;

import java.util.HashMap;
import java.util.Map;


public class InMemoryTaskManager implements TaskManagerService {
    private final TaskStorage taskStorage;
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    public InMemoryTaskManager(TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
    }


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
        return taskStorage.addTask(name, description);
    }

    @Override
    public Epic addEpic(String name, String description/*, LocalDateTime endTime*/) {
        Task task = taskStorage.addTask(name, description);
        return new InMemoryEpic(task, taskStorage);
    }

    @Override
    public Subtask addSubtask(Epic epic, String name, String description) {
        return epic.addSubTask(name, description);
    }


}