package practicum2.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class InMemoryTask implements Task {
    private final Map<Integer, InMemTaskData> storageMap;
    private final int id;

    public InMemoryTask(Map<Integer, InMemTaskData> storageMap, int id) {
        this.storageMap = storageMap;
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
                ", duration=" + duration() +
                ", startTime=" + startTime() +
                ", endTime=" + endTime() +
                '}';
    }
}
