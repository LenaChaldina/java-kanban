package practicum2.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class InMemorySubtask implements Subtask {
    private final Epic epic;
    private final Task task;

    public InMemorySubtask(Epic epic, Task task) {
        this.epic = epic;
        this.task = task;
    }

    @Override
    public Epic getEpic() {
        return epic;
    }

    @Override
    public Integer id() {
        return task.id();
    }

    @Override
    public String name() {
        return task.name();
    }

    @Override
    public String description() {
        return task.description();
    }

    @Override
    public Duration duration() {
        return task.duration();
    }

    @Override
    public LocalDateTime startTime() {
        return task.startTime();
    }

    @Override
    public LocalDateTime endTime() {
        return task.endTime();
    }

    @Override
    public String toString() {
        return "InMemorySubtask{" + task.toString() + '}';
    }
}
