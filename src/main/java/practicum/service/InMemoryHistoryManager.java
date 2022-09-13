package practicum.service;

import practicum.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManagerService {

    private final Map<Integer, Node> nodesMap = new HashMap<>();
    private CustomLinkedList<Task> taskList = new CustomLinkedList();

    @Override
    public ArrayList<Task> getTasksHistory() {
        return taskList.getTasks();
    }

    @Override
    public void addTask(Task task) {
        if(task != null) {
            Node nodeForRemove = nodesMap.get(task.getId());

            taskList.removeNode(nodeForRemove);
            taskList.linkTail(task);
            nodesMap.put(task.getId(), taskList.tail);
        }
    }

    @Override
    public void removeTask(int id) {
        Node nodeForRemove = nodesMap.get(id);

        taskList.removeNode(nodeForRemove);
        nodesMap.remove(nodeForRemove);

    }

    private class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        //добавляет задачу в конец списка
        private void linkTail(T element) {
            final Node<T> newNode = new Node<>(element, tail, null);

            if (head == null) {
                head = newNode;
            } else {
                tail.next = newNode;
            }

            tail = newNode;
        }

        private void removeNode(Node<T> node) {
            if (node != null) {
                if (head == tail) { //если список из одного элемента
                    head = null;
                    tail = null;
                } else if (node == head) {  //если удаляем годову из ноды
                    head = node.next;
                    head.prev = null;
                    node.next = null;
                } else if (node == tail) { //удаляем хвост из ноды
                    tail = node.prev;
                    tail.next = null;
                    node.prev = null;
                } else { //удаляем из середины списка
                    node.next.prev = node.prev;
                    node.prev.next = node.next;
                }
            }
        }

        //собирать все задачи в ArrayList
        private ArrayList<T> getTasks() {
            ArrayList<T> tasks = new ArrayList<>();

            for (Node<T> i = head; i != null; i = i.next) {
                tasks.add(i.data);
            }
            return tasks;
        }
    }

}
