package practicum.task;

import practicum.constants.Status;
import practicum.constants.TaskType;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected Status status;


    //для сабтаска - статус вводит пользователь
    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    //для эпика - статус обновляется самостоятельно
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    //для 2.5 Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public Task(Integer id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    //если эпик пришел вместе с ID
    public Task(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    @Override
    public String toString() {
        return id + "," + getTaskType() + "," + name + "," + status + "," + description + ",";
    }

    public static Task fromString(String value) {
        String[] tasksFromFile = value.split(",");
        Integer id = Integer.valueOf(tasksFromFile[0]);
        String name = tasksFromFile[2];
        String description = tasksFromFile[4];
        Status status = Status.valueOf(tasksFromFile[3]);

        Task task = new Task(id, name, description, status);
        return task;
    }
}
