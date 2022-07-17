package practicum.service;

import practicum.task.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManagerService {
    private LinkedList<Task> tasksHistory = new LinkedList<>();
    private static final int HISTORY_LENGTH = 10;
    private static final int REPLACEABLE_ELEMENT = 0;

    @Override
    public LinkedList<Task> getTasksHistory() {
        return tasksHistory;
    }

    @Override
    public void addTask(Task task) {
        if (tasksHistory.size() >= HISTORY_LENGTH) {
            //Если размер списка исчерпан, из него нужно удалить самый старый элемент — тот который находится в начале списка.
            tasksHistory.remove(REPLACEABLE_ELEMENT);
        }
            tasksHistory.add(task);
    }
}
