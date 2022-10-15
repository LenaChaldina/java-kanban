package practicum2.task;

import practicum.constants.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class InMemoryTask implements Task {
    private final Map<Integer, InMemTaskData> storageMap;
    private final Map<Integer, Status> statusMap;
    private final int id;

    public InMemoryTask(Map<Integer, InMemTaskData> storageMap, Map<Integer, Status> statusMap, int id) {
        this.storageMap = storageMap;
        this.statusMap = statusMap;
        this.id = id;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public String name() {
        return storageMap.get(id).name;
    }

    @Override
    public String description() {
        return storageMap.get(id).description;
    }

    @Override
    public Status status() {
        return statusMap.get(id);
    }

    @Override
    public Duration duration() {
        InMemTaskData taskData = storageMap.get(id);
        return Duration.between(taskData.startTime, taskData.endTime);
    }

    @Override
    public LocalDateTime startTime() {
        return storageMap.get(id).startTime;
    }

    @Override
    public LocalDateTime endTime() {
        return storageMap.get(id).endTime;
    }

    @Override
    public String toString() {
        return "InMemoryTask{" +
                "id=" + id +
                ", name='" + name() + '\'' +
                ", description='" + description() + '\'' +
                ", status=" + status() +
                ", duration=" + duration() +
                ", startTime=" + startTime() +
                ", endTime=" + endTime() +
                '}';
    }
}
