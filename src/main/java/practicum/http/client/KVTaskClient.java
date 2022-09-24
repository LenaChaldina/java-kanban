package practicum.http.client;

import practicum.exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        apiToken = register(url);
    }

    private String register(String url) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
            return send.body();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // Метод void put(String key, String json) должен сохранять состояние менеджера задач через запрос
    // POST /save/<ключ>?API_TOKEN=.
    public void save(String key, String json) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
            if (send.statusCode() != 200) {
                System.out.println(send.statusCode());
                throw new ManagerSaveException("Во время сохранения на сервер произошла ошибка");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод String load(String key) должен возвращать состояние менеджера задач через запрос
    // GET /load/<ключ>?API_TOKEN=.
    public String load(String key) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
            return send.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
