package practicum.service;

public class Node<E> {
    public E data;
    Node prev;
    Node next;

    public Node (E data, Node prev, Node next) {
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