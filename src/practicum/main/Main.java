package practicum.main;

import practicum.constants.Status;
import practicum.service.FileBackedTasksManager;
import practicum.service.HistoryManagerService;
import practicum.service.PrintConsoleService;
import practicum.service.TaskManagerService;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.File;

import static practicum.utility.Managers.getInMemoryTaskManager;

public class Main {
    public static void main(String[] args) {

        //test1 создайте две задачи, эпик с тремя подзадачами и эпик без подзадач;
        //testCustomLinkedList();
        //test2 FileBackedTasksManager
        testFileBackedTasksManager();
    }

    private static void testFileBackedTasksManager() {

        TaskManagerService inMemoryTaskManager = getInMemoryTaskManager();
        HistoryManagerService inMemoryHistoryManager = inMemoryTaskManager.getInMemoryHistoryManager();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(inMemoryHistoryManager, "resources/tasks.csv");
        PrintConsoleService printConsoleService = new PrintConsoleService();

        Task task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW);
        Task task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS);

        fileBackedTasksManager.addTask(task);
        fileBackedTasksManager.addTask(task2);

        Epic epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        Epic epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");

        fileBackedTasksManager.addEpic(epic);
        fileBackedTasksManager.addEpic(epic2);

        Subtask subtask = new Subtask("Завершить работу над 3-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.DONE, epic2.getId());
        Subtask subtask3 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, epic2.getId());

        fileBackedTasksManager.addSubtask(subtask);
        fileBackedTasksManager.addSubtask(subtask2);
        fileBackedTasksManager.addSubtask(subtask3);

        fileBackedTasksManager.getTask(0);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getEpic(2);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getSubtask(4);
        fileBackedTasksManager.getSubtask(5);
        fileBackedTasksManager.getSubtask(6);
        fileBackedTasksManager.getEpic(2);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getSubtask(4);
        fileBackedTasksManager.getSubtask(5);
        fileBackedTasksManager.getSubtask(12);

        fileBackedTasksManager = fileBackedTasksManager.loadFromFile(inMemoryTaskManager, inMemoryHistoryManager, new File("resources/tasks.csv"));

        printConsoleService.printTasks(inMemoryTaskManager);
        printConsoleService.printEpics(inMemoryTaskManager);
        printConsoleService.printSubtasks(inMemoryTaskManager);
        printConsoleService.printTasksHistory(inMemoryHistoryManager);
    }

    private static void testCustomLinkedList() {

        TaskManagerService inMemoryTaskManager = getInMemoryTaskManager();
        HistoryManagerService inMemoryHistoryManager = inMemoryTaskManager.getInMemoryHistoryManager();

        Task task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW);
        Task task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS);

        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);

        Epic epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        Epic epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");

        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);

        Subtask subtask = new Subtask("Завершить работу над 3-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Влажная уборка", "Помыть пол, протереть пыль", Status.DONE, epic2.getId());
        Subtask subtask3 = new Subtask("сухая уборка", "Пропылесосить, разложить вещи", Status.IN_PROGRESS, epic2.getId());

        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);

        PrintConsoleService printConsoleService = new PrintConsoleService();

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

        inMemoryTaskManager.removeSubtask(4);
        inMemoryTaskManager.removeEpic(2);
        inMemoryTaskManager.removeTask(1);
        inMemoryTaskManager.removeTask(0);
        inMemoryTaskManager.removeTask(12);

        printConsoleService.printTasksHistory(inMemoryHistoryManager);
    }
}

