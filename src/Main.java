public class Main {

    public static void main(String[] args) {
        TaskManagerService taskManagerService = new TaskManagerService();

        Task task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW);
        Task task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS);
        taskManagerService.createTask(task);
        taskManagerService.createTask(task2);

        Epic epic = new Epic("Улучшить наыки программирования", "Написание кода и изучение теории");
        Epic epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");
        taskManagerService.createEpic(epic);
        taskManagerService.createEpic(epic2);

        Subtask subtask = new Subtask("Завершить работу над 3-им спринтом", "Закончить теоритическую и практическую части", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Влажная уборка", "Помыть пол, протетереть пыль", Status.DONE, epic2.getId());
        Subtask subtask3 = new Subtask("сухая уборка", "Пропылесосить, разложить вещи", Status.IN_PROGRESS, epic2.getId());
        taskManagerService.createSubtask(subtask);
        taskManagerService.createSubtask(subtask2);
        taskManagerService.createSubtask(subtask3);

        PrintConsoleService printConsoleService = new PrintConsoleService();
        ///тестинг
        System.out.println("Первоначальное заполнение: ");
        printConsoleService.printTasks(taskManagerService);
        printConsoleService.printEpics(taskManagerService);
        printConsoleService.printSubtasks(taskManagerService);
        System.out.println("Обновление: ");
        Task task3 = new Task(0, "Прогнать ночных кабанов", "Кабаны уходят, если чуят опасность", Status.NEW);
        taskManagerService.updateTask(task3);
        Subtask subtask4 = new Subtask(4, "Подготовка к собеседованию", "Начать познавать алгоритмы", Status.IN_PROGRESS, epic.getId());
        taskManagerService.updateSubtask(subtask4);
        Epic epic3 = new Epic(2, "Переехать в Дублин", "Релоцироваться как сотрудник");
        taskManagerService.updateEpic(epic3);
        printConsoleService.printTasks(taskManagerService);
        printConsoleService.printEpics(taskManagerService);
        printConsoleService.printSubtasks(taskManagerService);
        System.out.println("Удаление по идентификатору: ");
        taskManagerService.removeTask(0);
        taskManagerService.removeSubtask(4);
        taskManagerService.removeEpic(3);
        printConsoleService.printTasks(taskManagerService);
        printConsoleService.printEpics(taskManagerService);
        printConsoleService.printSubtasks(taskManagerService);
    }
}
