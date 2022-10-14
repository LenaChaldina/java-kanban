package practicum2.task;

import practicum.constants.Status;

import java.time.LocalDateTime;

public class InMemTaskData {
    public final int id;
    public final String name;
    public final String description;
    public final Status status;
    public final LocalDateTime startTime;
    public final LocalDateTime endTime;

    public InMemTaskData(int id, String name, String description, Status status, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
