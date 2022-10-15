package practicum2.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryEpic implements Epic {
    private final List<Subtask> subtasks = new ArrayList<>();
    private final Task task;
    private final TaskStorage taskStorage;

    public InMemoryEpic(Task task, TaskStorage taskStorage) {
        this.task = task;
        this.taskStorage = taskStorage;
    }

    @Override
    public Subtask addSubTask(String name, String description) {
        InMemorySubtask subtask = new InMemorySubtask(this, taskStorage.addTask(name, description));
        subtasks.add(subtask);
        return subtask;
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
        return "InMemoryEpic{" + task.toString() +
            ", subtasks=\n" + subtasks.stream().map(subtask -> "  " + subtask.toString()).collect(Collectors.joining("\n")) +
            '}';
    }
}
