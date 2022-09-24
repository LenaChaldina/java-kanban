package practicum.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.constants.Status;
import practicum.http.server.HttpTaskServer;
import practicum.http.server.KVServer;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    KVServer kvServer;
    HttpTaskServer taskServer;
    Task task;
    Task task2;
    Task task3;
    Epic epic;
    Epic epic2;
    Subtask subtask;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    public void beforeEach() throws IOException {

        kvServer = new KVServer();
        kvServer.start();

        taskServer = new HttpTaskServer();
        taskServer.start();

        task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW, 60, "07:09:2022; 08:00");
        task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS, 30, "07:09:2022; 19:10");
        task3 = new Task("Посещение", "Позаниматься плаванием перед работой", Status.NEW);

        taskServer.manager.addTask(task);
        taskServer.manager.addTask(task2);
        taskServer.manager.addTask(task3 );

        epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");

        taskServer.manager.addEpic(epic);
        taskServer.manager.addEpic(epic2);

       subtask  = new Subtask("Завершить работу над 7-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, 6000, "10:09:2022; 08:00", epic.getId());
       subtask2 = new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.DONE, 80, "08:09:2022; 08:00", epic2.getId());
       subtask3 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, 40, "07:09:2022; 12:00", epic2.getId());

        taskServer.manager.addSubtask(subtask);
        taskServer.manager.addSubtask(subtask2);
        taskServer.manager.addSubtask(subtask3);
    }

    @AfterEach
    public void stop() {
        taskServer.stop();
        kvServer.stop();
    }

    @Test
    public void getTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void createTask() throws IOException, InterruptedException{
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        Task newTask = new Task("--", "--", Status.NEW, 10, "12:09:2022; 08:00");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
}
