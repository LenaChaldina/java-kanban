package practicum2.service;


import practicum2.task.Epic;
import practicum2.task.Subtask;
import practicum2.task.Task;

import java.time.LocalDateTime;
import java.util.*;

public interface TaskManagerService {


    Map<Integer, Task> getResultTasks();

    Map<Integer, Subtask> getResultSubtasks();

    Map<Integer, Epic> getResultEpics();

    Task addTask(String name, String description);

    Epic addEpic(String name, String description/*, LocalDateTime endTime*/);

    Subtask addSubtask(Epic epic, String name, String description);

   /* Collection<Task> getTasks();

    Collection<Epic> getEpics();

    Collection<Subtask> getSubtasks();

    void updateEpicStatus(Epic epic);

    Collection<Subtask> getEpicSubtasks(Epic epic);

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

    void updateEpicDuration(Epic epic);

    Collection<Task> getPrioritizedTasks();

    boolean isValid(Task task);*/
}