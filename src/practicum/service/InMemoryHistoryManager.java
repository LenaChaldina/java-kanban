package practicum.service;

import practicum.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManagerService {
    //будет заполняться по мере добавления новых задач
    private final Map<Integer, Node> nodes = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public ArrayList<Task> getTasksHistory() {
        return getTasks();
    }

    @Override
    public void addTask(Task task) {
        Node nodeForRemove = nodes.get(task.getId());
        removeNode(nodeForRemove);
        linkLast(task);
    }

    //добавляет задачу в конец списка
    private void linkLast(Task task) {
        final Node oldLast = last;
        final Node newNode = new Node(task, oldLast, null);
        last = newNode;

        if (oldLast == null)
            first = newNode;
        else
            oldLast.next = newNode;
        nodes.put(task.getId(), newNode);
    }

    //собирать все задачи в ArrayList
    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        for (Node i = first; i != null; i = i.next) {
            tasks.add(i.task);
        }
        return tasks;
    }

    @Override
    public void removeTask(int id) {
        Node nodeForRemove = nodes.get(id);
        removeNode(nodeForRemove);

    }

    private void removeNode(Node node) {
        if (node != null) {
            nodes.remove(node.task.getId());
            if (first == last) { //если список из одного элемента
                first = null;
                last = null;
            } else if (node.prev == null) {  //если удаляем годову из ноды
                first = node.next;
                first.prev = null;
            } else if (node.next == null) { //удаляем хвост из ноды
                last = node.prev;
                last.next = null;
            } else { //удаляем из середины списка
                node.next.prev = node.prev;
                node.prev.next = node.next;
            }

        }
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", prev=" + (prev.task != null ? prev.task : null) +
                    ", next=" + (next.task != null ? next.task : null) +
                    '}';
        }
    }

}
