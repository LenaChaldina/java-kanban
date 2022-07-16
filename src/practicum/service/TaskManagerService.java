package practicum.service;

import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.util.HashMap;
import java.util.ArrayList;

public interface TaskManagerService {
    HashMap<Integer, Task> getResultTasks();

    HashMap<Integer, Subtask> getResultSubtasks();

    HashMap<Integer, Epic> getResultEpics();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    void updateEpicStatus(Epic epic);

    ArrayList<Subtask> getEpicSubtasks(Epic epic);

    void removeTasks();

    void removeSubtasks();

    void removeEpics();

    Task getTask(Integer taskId);

    Subtask getSubtask(Integer subtaskId);

    Epic getEpic(Integer epicId);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void removeTask(Integer taskId);

    void removeSubtask(Integer subtaskId);

    void removeEpic(Integer epicId);
}