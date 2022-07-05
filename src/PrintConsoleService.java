public class PrintConsoleService {
    public void printTasks(TaskManagerService taskManagerService) {
        System.out.println("Таски: ");
        for (Task task : taskManagerService.tasks.values()) {
            System.out.println(task);
        }
    }

    public void printEpics(TaskManagerService taskManagerService) {
        System.out.println("Эпики: ");
        for (Epic epic : taskManagerService.epics.values()) {
            System.out.println(epic);
        }
    }

    public void printSubtasks(TaskManagerService taskManagerService) {
        System.out.println("Сабтаски: ");
        for (Subtask subtask : taskManagerService.subtasks.values()) {
            System.out.println(subtask);
        }
    }
}
