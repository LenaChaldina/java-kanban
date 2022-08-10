package practicum.service;

import practicum.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManagerService {
    private CustomLinkedList<Task, Integer> customLinkedList = new CustomLinkedList();

    @Override
    public ArrayList<Task> getTasksHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void addTask(Task task) {
        Node nodeForRemove = customLinkedList.nodes.get(task.getId());

        customLinkedList.removeNode(nodeForRemove);
        customLinkedList.linkLast(task, task.getId());
    }

    @Override
    public void removeTask(int id) {
        Node nodeForRemove = customLinkedList.nodes.get(id);
        customLinkedList.removeNode(nodeForRemove);
    }

    private class CustomLinkedList<T, K> {
        //будет заполняться по мере добавления новых задач
        private final Map<K, Node> nodes = new HashMap<>();
        private Node<T, K> first;
        private Node<T, K> last;

        //добавляет задачу в конец списка
        private void linkLast(T element, K key) {
            final Node<T, K> oldLast = last;
            final Node<T, K> newNode = new Node<>(key, element, oldLast, null);
            last = newNode;

            if (oldLast == null)
                first = newNode;
            else
                oldLast.next = newNode;
            nodes.put(key, newNode);
        }

        private void removeNode(Node<T, K> node) {
            if (node != null) {
                nodes.remove(node.key);
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

        //собирать все задачи в ArrayList
        private ArrayList<T> getTasks() {
            ArrayList<T> tasks = new ArrayList<>();

            for (Node<T, K> i = first; i != null; i = i.next) {
                tasks.add(i.data);
            }
            return tasks;
        }
    }

}
