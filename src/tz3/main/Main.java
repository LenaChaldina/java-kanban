package tz3.main;

import tz3.constants.Status;
import tz3.service.HistoryManagerService;
import tz3.service.PrintConsoleService;
import tz3.service.TaskManagerService;
import tz3.task.Epic;
import tz3.task.Subtask;
import tz3.task.Task;
import tz3.utility.Managers;

public class Main {
    public static void main(String[] args) {

        Managers manager = new Managers();

        TaskManagerService inMemoryTaskManager = manager.getDefault();
        HistoryManagerService inMemoryHistoryManager = manager.getDefaultHistory();

        Task task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW);
        Task task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS);

        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);

        Epic epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        Epic epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");

        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);

        Subtask subtask = new Subtask("Завершить работу над 3-им спринтом", "Закончить теоритическую и практическую части", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Влажная уборка", "Помыть пол, протетереть пыль", Status.DONE, epic2.getId());
        Subtask subtask3 = new Subtask("сухая уборка", "Пропылесосить, разложить вещи", Status.IN_PROGRESS, epic2.getId());

        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);

        PrintConsoleService printConsoleService = new PrintConsoleService();

        ///тестинг
        System.out.println("Первоначальное заполнение: ");

        printConsoleService.printTasks(inMemoryTaskManager);
        printConsoleService.printEpics(inMemoryTaskManager);
        printConsoleService.printSubtasks(inMemoryTaskManager);

        inMemoryTaskManager.getTask(0);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getEpic(3);
        inMemoryTaskManager.getSubtask(4);
        inMemoryTaskManager.getSubtask(5);
        inMemoryTaskManager.getSubtask(6);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getEpic(3);
        inMemoryTaskManager.getSubtask(4);
        inMemoryTaskManager.getSubtask(5);
        inMemoryTaskManager.getSubtask(12);

        inMemoryHistoryManager.getTasksHistory();

        printConsoleService.printTasksHistory(inMemoryHistoryManager);
    }
}
