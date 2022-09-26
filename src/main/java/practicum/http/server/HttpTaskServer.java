package practicum.http.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import practicum.service.HistoryManagerService;
import practicum.service.TaskManagerService;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;
import practicum.utility.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static practicum.utility.Managers.getGson;

public class HttpTaskServer {
    public static TaskManagerService manager;
    public static HistoryManagerService historyManager;
    private static final Gson gson = getGson();
    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public HttpTaskServer() {
        try {
            manager = Managers.getHttpTaskManager(gson);
            historyManager = manager.getInMemoryHistoryManager();
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TasksHandler());
            System.out.println("HTTP-сервер запущен на" + PORT + " порту");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

    static class TasksHandler implements HttpHandler {
        private int getParametersFromQuery(String query) throws NumberFormatException {
            return Integer.parseInt(query.split("=")[1]);
        }

        @Override
        public void handle(final HttpExchange httpExchange) {
            try {
                final String path = httpExchange.getRequestURI().getPath();
                final String query = httpExchange.getRequestURI().getQuery();
                final String requestMethod = httpExchange.getRequestMethod();
                String[] splitPath = path.split("/");

                switch (requestMethod) {
                    case "GET":
                        if (query == null) {
                            getMethodsWithoutQuery(httpExchange, splitPath);
                        } else {
                            getMethodsWithQuery(httpExchange, splitPath, query);
                        }
                        break;
                    case "POST":
                        postMethod(httpExchange, splitPath);
                        break;
                    case "DELETE":
                        if (query == null) {
                            deleteMethodsWithoutQuery(httpExchange, splitPath);
                        } else {
                            deleteMethodsWithQuery(httpExchange, splitPath, query);
                        }
                    default:
                        httpExchange.sendResponseHeaders(404, 0);
                        try (final OutputStream os = httpExchange.getResponseBody()) {
                            os.write(("Данный метод обработать не можем, используй GET").getBytes());
                            break;
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }

        private void postMethod(final HttpExchange httpExchange, String[] splitPath) throws IOException {
            InputStream inputStream = httpExchange.getRequestBody();
            String json = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            inputStream.close();
            try {
                switch (splitPath[2]) {
                    case "task":
                        Task task = gson.fromJson(json, Task.class);
                        if (manager.getTasks().stream().anyMatch(t -> t.getId() == task.getId())) {
                            manager.updateTask(task);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            manager.addTask(task);
                            httpExchange.sendResponseHeaders(201, 0);
                        }
                        break;
                    case "subtask":
                        Subtask subtask = gson.fromJson(json, Subtask.class);
                        if (manager.getSubtasks().stream().anyMatch(t -> t.getId() == subtask.getId())) {
                            manager.updateSubtask(subtask);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            manager.addSubtask(subtask);
                            httpExchange.sendResponseHeaders(201, 0);
                        }
                        break;
                    case "epic":
                        Epic epic = gson.fromJson(json, Epic.class);
                        if (manager.getEpics().stream().anyMatch(t -> t.getId() == epic.getId())) {
                            manager.updateEpic(epic);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            manager.addEpic(epic);
                            httpExchange.sendResponseHeaders(201, 0);
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(400, 0);
                }
            } catch (NullPointerException | JsonSyntaxException e) {
                httpExchange.sendResponseHeaders(400, 0);
            }
        }

        private void deleteMethodsWithoutQuery(final HttpExchange httpExchange, String[] splitPath) throws IOException {
            switch (splitPath[2]) {
                case "task":
                    manager.removeTasks();
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "subtask":
                    manager.removeSubtasks();
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "epic":
                    manager.removeEpics();
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    httpExchange.sendResponseHeaders(400, 0);
            }
        }

        private void deleteMethodsWithQuery(final HttpExchange httpExchange, String[] splitPath, final String query) throws IOException {
            try {
                int id = getParametersFromQuery(query);
                switch (splitPath[2]) {
                    case "task":
                        if (!manager.getTasks().contains(id)) {
                            httpExchange.sendResponseHeaders(404, 0);
                        } else {
                            manager.removeTask(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    case "subtask":
                        if (!manager.getSubtasks().contains(id)) {
                            httpExchange.sendResponseHeaders(404, 0);
                        } else {
                            manager.removeSubtask(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    case "epic":
                        if (!manager.getEpics().contains(id)) {
                            httpExchange.sendResponseHeaders(404, 0);
                        } else {
                            manager.removeEpic(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(400, 0);
                        break;
                }
            } catch (NumberFormatException e) {
                httpExchange.sendResponseHeaders(400, 0);
            }
        }

        private <T extends Task> void sendJson(final HttpExchange httpExchange, Collection<T> tasks) throws IOException {
            httpExchange.sendResponseHeaders(200, 0);
            try (final OutputStream outputStream = httpExchange.getResponseBody()) {
                outputStream.write(gson.toJson(tasks).getBytes(DEFAULT_CHARSET));
            } catch (final IOException e) {
                e.printStackTrace();
                httpExchange.sendResponseHeaders(404, 0);
            }
        }

        private <T extends Task> void sendJsonForId(final HttpExchange httpExchange, T task) throws IOException {
            httpExchange.sendResponseHeaders(200, 0);
            try (final OutputStream outputStream = httpExchange.getResponseBody()) {
                outputStream.write(gson.toJson(task).getBytes(DEFAULT_CHARSET));
            } catch (final IOException e) {
                e.printStackTrace();
                httpExchange.sendResponseHeaders(404, 0);
            }
        }

        private void getMethodsWithoutQuery(final HttpExchange httpExchange, String[] splitPath) throws IOException {
            if (splitPath.length == 2) {
                sendJson(httpExchange, manager.getPrioritizedTasks());
            }
            switch (splitPath[2]) {
                case "task":
                    sendJson(httpExchange, manager.getTasks());
                    break;
                case "subtask":
                    sendJson(httpExchange, manager.getSubtasks());
                    break;
                case "epic":
                    sendJson(httpExchange, manager.getEpics());
                    break;
                case "history":
                    sendJson(httpExchange, historyManager.getTasksHistory());
                    break;
                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    break;
            }
        }

        private void getMethodsWithQuery(final HttpExchange httpExchange, String[] splitPath, final String query) throws IOException {
            int id = getParametersFromQuery(query);
            if (splitPath.length == 4) {
                sendJson(httpExchange, manager.getEpicSubtasks(manager.getEpic(id)));
            }
            switch (splitPath[2]) {
                case "task":
                    sendJsonForId(httpExchange, manager.getTask(id));
                    break;
                case "subtask":
                    sendJsonForId(httpExchange, manager.getSubtask(id));
                    break;
                case "epic":
                    sendJsonForId(httpExchange, manager.getEpic(id));
                    break;
                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    break;
            }
        }
    }
}
