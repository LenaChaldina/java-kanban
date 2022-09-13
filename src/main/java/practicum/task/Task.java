package practicum.task;

import practicum.constants.Status;
import practicum.constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected Status status;
    //duration — продолжительность задачи, оценка того, сколько времени она займёт в минутах (число);
    protected Duration duration;
    //startTime — дата, когда предполагается приступить к выполнению задачи.
    protected LocalDateTime startTime;

    //для сабтаска - статус вводит пользователь
    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    //если необходимо ввести дату старта и продолжительность задачи
    public Task(String name, String description, Status status, Integer duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("dd:MM:yyyy; HH:mm"));
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

    //обновление + дата старта и продолжительность задачи
    public Task(Integer id, String name, String description, Status status, Integer duration, String startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("dd:MM:yyyy; HH:mm"));
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    //getEndTime() — время завершения задачи, которое рассчитывается исходя из startTime и duration.
    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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
        if (duration != null) {
            return id + "," + getTaskType() + "," + name + "," + status + "," + description + "," +
                    duration.toMinutes() + "," + startTime.format(DateTimeFormatter.ofPattern("dd:MM:yyyy; HH:mm")) + ",";
        } else {
            return id + "," + getTaskType() + "," + name + "," + status + "," + description + "," + duration + "," + startTime + ",";
        }
    }

    public static Task fromString(String value) {
        String[] tasksFromFile = value.split(",");
        Integer duration = Integer.valueOf(tasksFromFile[5]);
        String startTime = tasksFromFile[6];
        Integer id = Integer.valueOf(tasksFromFile[0]);
        String name = tasksFromFile[2];
        String description = tasksFromFile[4];
        Status status = Status.valueOf(tasksFromFile[3]);


        Task task = new Task(id, name, description, status, duration, startTime);
        return task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return name.equals(task.name) && description.equals(task.description) && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, duration, startTime);
    }

}
