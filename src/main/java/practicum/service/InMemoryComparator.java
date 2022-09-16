package practicum.service;

import practicum.task.Task;

import java.util.Comparator;

public class InMemoryComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        if ((o1.getStartTime() == null) && (o2.getStartTime() != null)) {
            return 1;
        } else if ((o1.getStartTime() != null) && (o2.getStartTime() == null)) {
            return -1;
        } else if ((o1.getStartTime() == null) && (o2.getStartTime() == null)) {
            return 0;
        } else {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    }
}