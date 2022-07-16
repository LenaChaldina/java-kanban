package practicum.service;

import practicum.task.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManagerService {
    private LinkedList<Task> tasksHistory = new LinkedList<>();
    static final int HISTORY_LENGTH = 10;
    static final int REPLACEABLE_ELEMENT = 0;

    @Override
    public LinkedList<Task> getTasksHistory() {
        return tasksHistory;
    }

    @Override
    public void addTask(Task task) {
        if (getTasksHistory().size() >= HISTORY_LENGTH) {
            //Если размер списка исчерпан, из него нужно удалить самый старый элемент — тот который находится в начале списка.
            getTasksHistory().remove(REPLACEABLE_ELEMENT);
            getTasksHistory().add(task);
        } else {
            getTasksHistory().add(task);
        }

    }
}
