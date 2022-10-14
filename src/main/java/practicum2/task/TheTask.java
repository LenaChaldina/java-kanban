package practicum2.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class TheTask implements Task {
    private final Integer id;
    private final String name;
    private final String description;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TheTask(Integer id, String name, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public TheTask(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
