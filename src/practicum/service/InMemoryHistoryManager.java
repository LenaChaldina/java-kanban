package practicum.service;

import practicum.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManagerService {
    private final Map<Integer, Node> nodes = new HashMap<>();
    private CustomLinkedList<Task> customLinkedList = new CustomLinkedList();

    @Override
    public ArrayList<Task> getTasksHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void addTask(Task task) {
        Node nodeForRemove = nodes.get(task.getId());

        customLinkedList.removeNode(nodeForRemove);
        customLinkedList.linkTail(task);
    }

    @Override
    public void removeTask(int id) {
        Node nodeForRemove = nodes.get(id);
        customLinkedList.removeNode(nodeForRemove);
    }

    private class CustomLinkedList<T extends Task> {
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
            nodes.put(element.getId(), newNode);
        }

        private void removeNode(Node<T> node) {
            if (node != null) {
                nodes.remove(node);
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
