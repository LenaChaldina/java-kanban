package practicum.service;

import practicum.task.Task;

import java.util.LinkedList;

public interface HistoryManagerService {
    LinkedList<Task> getTasksHistory();

    //пометить задачи как просмотренные
    //если задачка "просмотрена" буду потом ее в отдельный список перекладывать
    void addTask(Task task);
}
