package practicum.task;

import practicum.constants.Status;
import practicum.constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    private final int epicID;

    public Subtask(String name, String description, Status status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    //если сабтаск пришел с датой начала и продолжительностью
    public Subtask(String name, String description, Status status, Integer duration, String startTime, int epicID) {
        super(name, description, status, duration, startTime);
        this.epicID = epicID;
    }


    //для обновления данных
    public Subtask(Integer id, String name, String description, Status status, int epicID) {
        super(id, name, description, status);
        this.epicID = epicID;
    }

    public Subtask(Integer id, String name, String description, Status status, Integer duration, String startTime, int epicID) {
        super(id, name, description, status, duration, startTime);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return super.toString() + epicID;
    }

public static Subtask fromString(String value) {
        String[] subtasksFromFile = value.split(",");
        Integer duration = Integer.valueOf(subtasksFromFile[5]);
        String startTime = subtasksFromFile[6];
        Integer id = Integer.valueOf(subtasksFromFile[0]);
        String name = subtasksFromFile[2];
        String description = subtasksFromFile[4];
        Status status = Status.valueOf(subtasksFromFile[3]);
        Integer epicID = Integer.valueOf(subtasksFromFile[7]);


        Subtask subtask = new Subtask(id, name, description, status, duration, startTime, epicID);
        return subtask;
    }

}
