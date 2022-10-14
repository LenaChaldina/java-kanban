package practicum2.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class CachedTask implements Task {
    private final Task innerTask;
    private int id;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;
    private String innerTaskString;


    public CachedTask(Task innerTask) {
        this.innerTask = innerTask;
        renewCache();
    }

    private void renewCache() {
        id = innerTask.id();
        name = innerTask.name();
        description = innerTask.description();
        startTime = innerTask.startTime();
        endTime = innerTask.endTime();
        duration = innerTask.duration();
        innerTaskString = innerTask.toString();
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public Duration duration() {
        return duration;
    }

    @Override
    public LocalDateTime startTime() {
        return startTime;
    }

    @Override
    public LocalDateTime endTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return innerTaskString;
    }
}
