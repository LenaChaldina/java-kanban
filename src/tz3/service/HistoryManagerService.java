package tz3.service;

import tz3.task.Task;

import java.util.List;

public interface HistoryManagerService {
    List<Task> getTasksHistory();

    //пометить задачи как просмотренные
    //если задачка "просмотрена" буду потом ее в отдельный список перекладывать
    void add(Task task);
}
