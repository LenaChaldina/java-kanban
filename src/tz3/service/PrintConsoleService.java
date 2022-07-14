package tz3.service;

import tz3.task.Epic;
import tz3.task.Subtask;
import tz3.task.Task;

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
        System.out.println("Список из последних 10-и просмотренных задач : ");
        for (Task task : historyManager.getTasksHistory()) {
            System.out.println(task);
        }
    }
}
