package practicum.constants;

public enum TaskType {
    TASK,
    EPIC,
    SUBTASK,
    HISTORY;

    public static TaskType fromString(String value) {
        if (value.contains(",TASK")) {
            return TaskType.TASK;
        } else if (value.contains("EPIC")) {
            return TaskType.EPIC;
        } else if (value.contains("SUBTASK")) {
            return TaskType.SUBTASK;
        } else {
            return HISTORY;
        }
    }
}