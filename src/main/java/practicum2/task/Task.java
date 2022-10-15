package practicum2.task;

import practicum.constants.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Task {
    Integer id();
    String name();
    String description();

    Status status();
    Duration duration();
    LocalDateTime startTime();
    LocalDateTime endTime();
}
