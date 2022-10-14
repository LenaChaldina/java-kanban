package practicum.main;

import com.google.gson.Gson;
import practicum.constants.Status;
import practicum.http.client.KVTaskClient;
import practicum.http.server.HttpTaskServer;
import practicum.http.server.KVServer;
import practicum.service.HistoryManagerService;
import practicum.service.HttpTaskManager;
import practicum.service.PrintConsoleService;
import practicum.service.TaskManagerService;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;
import practicum.utility.Managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static practicum.utility.Managers.getInMemoryTaskManager;

public class Main {
    public static void main(String[] args) throws IOException {
        //test1 создайте две задачи, эпик с тремя подзадачами и эпик без подзадач;
        // testCustomLinkedList();
        //test3 тестим даты
        //testDataTime();
        //test4 тестим клиент-сервер
        testKVServer();
    }


    private static void testKVServer() throws IOException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078/");
        final HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
        Gson managersJson = Managers.getGson();

        Task task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW, 60, "07:09:2022; 08:00");
        Task task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS, 30, "07:09:2022; 19:10");
        List<Task> taskForKV = new ArrayList<>();

        taskForKV.add(task);
        taskForKV.add(task2);
        String jsonTasks = managersJson.toJson(taskForKV);
        kvTaskClient.save("tasks", jsonTasks);

        Epic epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        Epic epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");
        List<Epic> epicForKV = new ArrayList<>();

        epicForKV.add(epic);
        epicForKV.add(epic2);
        String jsonEpics = managersJson.toJson(epicForKV);
        kvTaskClient.save("epics", jsonEpics);

        Subtask subtask = new Subtask("Завершить работу над 7-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, 600, "10:09:2022; 08:00", 2);
        Subtask subtask2 = new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.DONE, 80, "08:09:2022; 08:00", 3);
        Subtask subtask3 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, 40, "07:09:2022; 12:00", 3);
        List<Subtask> subtaskForKV = new ArrayList<>();

        subtaskForKV.add(subtask);
        subtaskForKV.add(subtask2);
        subtaskForKV.add(subtask3);
        String jsonSubtasks = managersJson.toJson(subtaskForKV);
        kvTaskClient.save("subtasks", jsonSubtasks);

        for (String data : kvServer.getData().values()) {
            System.out.println(data);
        }

        System.out.println(kvTaskClient.load("tasks"));

        HttpTaskManager managers = Managers.getHttpTaskManager(managersJson);
        managers.addTask(task);
        managers.addTask(task2);
        managers.addEpic(epic);
        managers.addEpic(epic2);
        managers.addSubtask(subtask);
        managers.addSubtask(subtask2);
        managers.addSubtask(subtask3);
        managers.getTask(0);
        managers.getTask(1);
        managers.getEpic(2);
        managers.getEpic(3);
        managers.getSubtask(4);
        managers.getSubtask(5);
        managers.getSubtask(6);

        managers.save();
        managers.load();

        PrintConsoleService printConsoleService = new PrintConsoleService();
        printConsoleService.printHttpTasks(managers);
        printConsoleService.printHttpSubtask(managers);
        printConsoleService.printHttpEpic(managers);
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
    private static void testDataTime() {

        TaskManagerService inMemoryTaskManager = getInMemoryTaskManager();
        HistoryManagerService inMemoryHistoryManager = inMemoryTaskManager.getInMemoryHistoryManager();

        Task task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW, 60, "07:09:2022; 08:00");
        Task task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS, 30, "07:09:2022; 19:10");
        Task task3 = new Task("Посещение", "Позаниматься плаванием перед работой", Status.NEW);

        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3 );

        Epic epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        Epic epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");

        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);


        Subtask subtask  = new Subtask("Завершить работу над 7-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, 6000, "10:09:2022; 08:00", epic.getId());
        Subtask subtask2 = new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.DONE, 80, "08:09:2022; 08:00", epic2.getId());
        Subtask subtask3 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, 40, "07:09:2022; 12:00", epic2.getId());
        Subtask subtask4 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, 30, "06:09:2022; 12:00",epic2.getId());
        Subtask subtask5 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, 20, "06:09:2022; 10:00",epic2.getId());

        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.addSubtask(subtask4);
        inMemoryTaskManager.addSubtask(subtask5);

        inMemoryTaskManager.updateEpicDuration(epic2);

        PrintConsoleService printConsoleService = new PrintConsoleService();

        System.out.println("Первоначальное заполнение: ");
        printConsoleService.printTasks(inMemoryTaskManager);
        printConsoleService.printEpics(inMemoryTaskManager);
        printConsoleService.printSubtasks(inMemoryTaskManager);
        printConsoleService.printPriority(inMemoryTaskManager);

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
        printConsoleService.printPriority(inMemoryTaskManager);



    }
}

