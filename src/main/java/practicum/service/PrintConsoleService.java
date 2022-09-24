package practicum.service;

import practicum.http.client.KVTaskClient;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

public class PrintConsoleService {
    public void printTasks(TaskManagerService taskManagerService) {
        System.out.println("Таски: ");
        for (Task task : taskManagerService.getResultTasks().values()) {
            System.out.println(task);
        }
    }

    public void printEpics(TaskManagerService taskManagerService) {
        System.out.println("Эпики: ");
        for (Epic epic : taskManagerService.getResultEpics().values()) {
            System.out.println(epic);
        }
    }

    public void printSubtasks(TaskManagerService taskManagerService) {
        System.out.println("Сабтаски: ");
        for (Subtask subtask : taskManagerService.getResultSubtasks().values()) {
            System.out.println(subtask);
        }
    }

    public void printTasksHistory(HistoryManagerService historyManager) {
        System.out.println("Список из просмотренных задач : ");
        for (Task task : historyManager.getTasksHistory()) {
            System.out.println(task);
        }
    }

    public void printPriority(TaskManagerService taskManagerService) {
        System.out.println("Отсортированные таски и сабтаски : ");
        for (Task task : taskManagerService.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }

    public void printHttpTasks(HttpTaskManager taskManagerService) {
        System.out.println("Tasks : ");
        for (Task task : taskManagerService.getResultTasks().values()) {
            System.out.println(task);
        }
    }

    public void printHttpSubtask(HttpTaskManager taskManagerService) {
        System.out.println("Subtasks : ");
        for (Task task : taskManagerService.getResultSubtasks().values()) {
            System.out.println(task);
        }
    }

    public void printHttpEpic(HttpTaskManager taskManagerService) {
        System.out.println("Epics : ");
        for (Task task : taskManagerService.getResultEpics().values()) {
            System.out.println(task);
        }
    }
}
