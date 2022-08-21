package practicum.task;

import practicum.constants.Status;
import practicum.constants.TypeTasks;

public  class Subtask extends Task {
    private final int epicID;

    public Subtask(String name, String description, Status status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    //для обновления данных
    public Subtask(Integer id, String name, String description, Status status, int epicID) {
        super(id, name, description, status);
        this.epicID = epicID;
    }


    public int getEpicID() {
        return epicID;
    }

    @Override
    public TypeTasks getTaskType() {
        return TypeTasks.SUBTASK;
    }

    @Override
    public String toString() {
        return "tz3.task.Subtask{" +
                "epicID=" + epicID +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }


}
