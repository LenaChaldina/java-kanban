package practicum.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.constants.Status;
import practicum.http.client.KVTaskClient;
import practicum.http.server.KVServer;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;
import practicum.utility.Managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest extends TaskManagerServiceTest<HttpTaskManager> {
    private KVServer kvServer;
    private Gson managersJson;
    private HttpTaskManager managers;
    Task task;
    Task task2;
    Epic epic;
    Epic epic2;
    Subtask subtask;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078/");
        managersJson = Managers.getGson();
        super.beforeEach();

        task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW, 60, "07:09:2022; 08:00");
        task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS, 30, "07:09:2022; 19:10");
        List<Task> taskForKV = new ArrayList<>();

        taskForKV.add(task);
        taskForKV.add(task2);
        String jsonTasks = managersJson.toJson(taskForKV);
        kvTaskClient.save("tasks", jsonTasks);

        epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");
        List<Epic> epicForKV = new ArrayList<>();

        epicForKV.add(epic);
        epicForKV.add(epic2);
        String jsonEpics = managersJson.toJson(epicForKV);
        kvTaskClient.save("epics", jsonEpics);

        subtask = new Subtask("Завершить работу над 7-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, 600, "10:09:2022; 08:00", 2);
        subtask2 = new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.DONE, 80, "08:09:2022; 08:00", 3);
        subtask3 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, 40, "07:09:2022; 12:00", 3);
        List<Subtask> subtaskForKV = new ArrayList<>();

        subtaskForKV.add(subtask);
        subtaskForKV.add(subtask2);
        subtaskForKV.add(subtask3);
        String jsonSubtasks = managersJson.toJson(subtaskForKV);
        kvTaskClient.save("subtasks", jsonSubtasks);

        managers = Managers.getHttpTaskManager(managersJson);
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void save() {
        managers.save();
        assertNotNull(kvServer.getData(), "Непустая мапа на КВ-сервере");
        assertEquals(4, kvServer.getData().size());
    }

    @Test
    public void load() {
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

        Collection<Task> httpTasks = managers.getTasks();
        List<Subtask> httpSubtask = managers.getSubtasks();
        List<Epic> httpEpic = managers.getEpics();
        List<Task> httpHistory = managers.inMemoryHistoryManager.getTasksHistory();

        assertNotNull(httpTasks, "Непустые таски");
        assertEquals(2, httpTasks.size());

        assertNotNull(httpEpic, "Непустые эпики");
        assertEquals(2, httpEpic.size());

        assertNotNull(httpSubtask, "Непустые сабтаски");
        assertEquals(3, httpSubtask.size());

        assertNotNull(httpHistory, "Непустая история");
        assertEquals(7, httpHistory.size());
    }
}
