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

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }


}
