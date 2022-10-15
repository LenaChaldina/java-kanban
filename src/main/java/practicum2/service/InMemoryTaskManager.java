package practicum2.service;


import practicum.constants.Status;
import practicum2.task.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static practicum.constants.Status.DONE;


public class InMemoryTaskManager implements TaskManagerService {
    private final TaskStorage taskStorage;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    public InMemoryTaskManager(TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
    }


    @Override
    public Map<Integer, Task> getResultTasks() {
        return tasks.entrySet().stream()
            .filter(entry -> entry.getValue().status().equals(DONE))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<Integer, Subtask> getResultSubtasks() {
        return subtasks.entrySet().stream()
            .filter(entry -> entry.getValue().status().equals(DONE))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<Integer, Epic> getResultEpics() {
        return epics.entrySet().stream()
            .filter(entry -> entry.getValue().status().equals(DONE))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Task addTask(String name, String description) {
        Task task = taskStorage.addTask(name, description);
        tasks.put(task.id(), task);
        return task;
    }

    @Override
    public Epic addEpic(String name, String description/*, LocalDateTime endTime*/) {
        Task task = taskStorage.addTask(name, description);
        InMemoryEpic epic = new InMemoryEpic(task, taskStorage);
        epics.put(epic.id(), epic);
        return epic;
    }

    @Override
    public Subtask addSubtask(Epic epic, String name, String description) {
        Subtask subtask = epic.addSubTask(name, description);
        subtasks.put(subtask.id(), subtask);
        return subtask;
    }

    @Override
    public void setTaskStatus(Task task, Status status) {
        taskStorage.setStatus(task, status);
    }


}