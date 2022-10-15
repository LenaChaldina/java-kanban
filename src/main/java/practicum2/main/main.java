package practicum2.main;

import practicum2.service.InMemoryTaskManager;
import practicum2.service.TaskManagerService;
import practicum2.task.*;

import static practicum.constants.Status.IN_PROGRESS;

public class main {
    public static void main(String[] args) {
        TaskManagerService taskManagerService = new InMemoryTaskManager(new InMemoryTaskStorage());
        Task task = new CachedTask(taskManagerService.addTask("Task1", "Очень важная таска"));
        System.out.println(task);

        Epic epic = taskManagerService.addEpic("Epic1", "Первый эпик");
        Subtask subtask1 = epic.addSubTask("SubTask1", "Создать подзадачу");
        Subtask subtask2 = epic.addSubTask("SubTask2", "Создать подзадачу 2");
        System.out.println(epic);

        taskManagerService.setTaskStatus(subtask1, IN_PROGRESS);

        System.out.println(subtask1);
    }
}
