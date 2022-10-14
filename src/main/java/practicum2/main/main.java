package practicum2.main;

import practicum2.service.InMemoryTaskManager;
import practicum2.service.TaskManagerService;

public class main {
    public static void main(String[] args) {
        TaskManagerService taskManagerService = new InMemoryTaskManager();
        taskManagerService.addTask("Task1", "Очень важная таска");
    }
}
