package practicum2.main;

import practicum2.service.InMemoryTaskManager;
import practicum2.service.TaskManagerService;
import practicum2.task.CachedTask;
import practicum2.task.Task;

public class main {
    public static void main(String[] args) {
        TaskManagerService taskManagerService = new InMemoryTaskManager();
        Task task = new CachedTask(taskManagerService.addTask("Task1", "Очень важная таска"));
        System.out.println(task);
    }
}
