package practicum.task;

import practicum.constants.Status;
import practicum.constants.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    //для тестинга
    public Epic(String name, String description, Status status) {
        super(name, description);
        this.status = status;
    }

    //для обновления данных
    public Epic(Integer id, String name, String description) {
        super(id, name, description);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void addSubTask(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public static Epic fromString(String value) {
        String[] epicsFromFile = value.split(",");
        Integer id = Integer.valueOf(epicsFromFile[0]);
        String name = epicsFromFile[2];
        String description = epicsFromFile[4];

        Epic epic = new Epic(id, name, description);
        return epic;
    }
}
