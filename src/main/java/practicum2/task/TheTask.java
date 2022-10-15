package practicum2.task;

import practicum.constants.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static practicum.constants.Status.NEW;

public class TheTask implements Task {
    private final Integer id;
    private final String name;
    private final String description;
    private final Status status;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TheTask(Integer id, String name, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = NEW;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public TheTask(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = NEW;
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
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
    public Status status() {
        return status;
    }

    @Override
    public Duration duration() {
        return Duration.between(startTime, endTime);
    }

    @Override
    public LocalDateTime startTime() {
        return startTime;
    }

    @Override
    public LocalDateTime endTime() {
        return endTime;
    }
}
