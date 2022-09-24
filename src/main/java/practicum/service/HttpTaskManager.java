package practicum.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import practicum.http.client.KVTaskClient;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient kvTaskClient;
    private Gson gson;

    public HttpTaskManager(HistoryManagerService inMemoryHistoryManager, KVTaskClient kvTaskClient, Gson gson) {
        super(inMemoryHistoryManager);
        this.kvTaskClient = kvTaskClient;
        this.gson = gson;
    }

    @Override
    public void save() {
        kvTaskClient.save("tasks", gson.toJson(tasks.values()));
        kvTaskClient.save("subtasks", gson.toJson(subtasks.values()));
        kvTaskClient.save("epics", gson.toJson(epics.values()));
        kvTaskClient.save("history", gson.toJson(historyToString(inMemoryHistoryManager)));
    }

    public void load() {
        String taskStateJson = kvTaskClient.load("tasks");
        String subtaskStateJson = kvTaskClient.load("subtasks");
        String epicStateJson = kvTaskClient.load("epics");
        String historyStateJson = kvTaskClient.load("history");
        if (taskStateJson != null) {
            List<Task> tasksState = gson.fromJson(taskStateJson, new TypeToken<List<Task>>() {
            }.getType());
            for (Task task : tasksState) {
                addTask(task);
            }
        }
        if (epicStateJson != null) {
            List<Epic> epicsState = gson.fromJson(epicStateJson, new TypeToken<List<Epic>>() {
            }.getType());
            for (Epic epic : epicsState) {
                addEpic(epic);
            }
        }
        if (subtaskStateJson != null) {
            List<Subtask> subtasksState = gson.fromJson(subtaskStateJson, new TypeToken<List<Subtask>>() {
            }.getType());
            for (Subtask subtask : subtasksState) {
                addSubtask(subtask);
            }
        }
        if (historyStateJson != null) {
            String historyState = gson.fromJson(historyStateJson, String.class);
            List<Integer> history = historyFromString(historyState);
            for (Integer hist : history) {
                getTask(hist);
            }
        }

    }

}
