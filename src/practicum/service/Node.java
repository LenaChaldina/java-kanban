package practicum.service;

public class Node<E, K> {
    public K key;
    public E data;
    Node prev;
    Node next;

    public Node(K key, E data, Node prev, Node next) {
        this.key = key;
        this.data = data;
        this.prev = prev;
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "task=" + data +
                ", prev=" + (prev.data != null ? prev.data : null) +
                ", next=" + (next.data != null ? next.data : null) +
                '}';
    }
}