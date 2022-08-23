package practicum.service;

import practicum.constants.Status;
import practicum.constants.TaskType;
import practicum.exceptions.ManagerSaveException;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static practicum.utility.Managers.getInMemoryTaskManager;

//вторая реализация менеджера = хранит информацию в файле
public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManagerService {
    private static String fileName = "resources/tasks.csv";

    //Пусть новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле.
    public FileBackedTasksManager(HistoryManagerService inMemoryHistoryManager, String fileName) {
        super(inMemoryHistoryManager);
        this.fileName = fileName;
    }

    public static void main(String[] args) {
        //test2 FileBackedTasksManager
        testFileBackedTasksManager();
    }

    private static void testFileBackedTasksManager() {
        TaskManagerService inMemoryTaskManager = getInMemoryTaskManager();
        HistoryManagerService inMemoryHistoryManager = inMemoryTaskManager.getInMemoryHistoryManager();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(inMemoryHistoryManager, "resources/tasks.csv");
        PrintConsoleService printConsoleService = new PrintConsoleService();

        Task task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW);
        Task task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS);

        fileBackedTasksManager.addTask(task);
        fileBackedTasksManager.addTask(task2);

        Epic epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        Epic epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");

        fileBackedTasksManager.addEpic(epic);
        fileBackedTasksManager.addEpic(epic2);

        Subtask subtask = new Subtask("Завершить работу над 3-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.DONE, epic2.getId());
        Subtask subtask3 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, epic2.getId());

        fileBackedTasksManager.addSubtask(subtask);
        fileBackedTasksManager.addSubtask(subtask2);
        fileBackedTasksManager.addSubtask(subtask3);

        fileBackedTasksManager.getTask(0);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getEpic(2);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getSubtask(4);
        fileBackedTasksManager.getSubtask(5);
        fileBackedTasksManager.getSubtask(6);
        fileBackedTasksManager.getEpic(2);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getSubtask(4);
        fileBackedTasksManager.getSubtask(5);
        fileBackedTasksManager.getSubtask(12);

        fileBackedTasksManager = fileBackedTasksManager.loadFromFile(inMemoryTaskManager, inMemoryHistoryManager, new File("resources/tasks.csv"));

        printConsoleService.printTasks(inMemoryTaskManager);
        printConsoleService.printEpics(inMemoryTaskManager);
        printConsoleService.printSubtasks(inMemoryTaskManager);
        printConsoleService.printTasksHistory(inMemoryHistoryManager);
    }

    // файл с сохранёнными данными будет выглядеть так:
    // id,type,name,status,description,epic
    // 1,TASK,Task1,NEW,Description task1,
    // 2,EPIC,Epic2,DONE,Description epic2,
    // 3,SUBTASK,Sub Task2,DONE,Description sub task3,2
    //
    // 2,3

    //Создайте метод save без параметров — он будет сохранять текущее состояние менеджера в указанный файл
    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,name,status,description,epic");
            bufferedWriter.newLine();

            addTasksOrEpicsToFile(bufferedWriter, tasks.values());
            addTasksOrEpicsToFile(bufferedWriter, epics.values());
            addSubtasksToFile(bufferedWriter, subtasks.values());

            bufferedWriter.newLine();
            bufferedWriter.write(historyToString(inMemoryHistoryManager));

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private <T extends Task> void addTasksOrEpicsToFile(BufferedWriter bufferedWriter, Collection<T> tasks) throws IOException {
        for (Task value : tasks) {
            bufferedWriter.write(value.toString());
            bufferedWriter.newLine();
        }
    }

    private void addSubtasksToFile(BufferedWriter bufferedWriter, Collection<Subtask> subtasks) throws IOException {
        for (Subtask value : subtasks) {
            bufferedWriter.write(value.toString());
            bufferedWriter.newLine();
        }
    }

    // сохранение менеджера истории
    static String historyToString(HistoryManagerService historyManager) {
        StringBuilder tasks = new StringBuilder();
        for (Task task : historyManager.getTasksHistory()) {
            tasks.append(task.getId());
            tasks.append(",");
        }
        //формат вывода истории - без последней запятой
        if (tasks.length() > 0) {
            tasks.deleteCharAt(tasks.length() - 1);
        }
        return String.valueOf(tasks);
    }

    // восстановление менеджера истории из CSV
    static List<Integer> historyFromString(String value) {
        List<Integer> historyFromStr = new ArrayList<>();
        String[] history = value.split(",");
        for (String hist : history) {
            historyFromStr.add(Integer.valueOf(hist));
        }
        return historyFromStr;
    }

    // восстанавливает данные менеджера из файла при запуске программы.
    public static FileBackedTasksManager loadFromFile(TaskManagerService inMemoryTaskManager, HistoryManagerService inMemoryHistoryManager, File fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();

                if (!line.startsWith("id") && line != null) {
                    switch (TaskType.fromString(line)) {
                        case TASK:
                            Task task = Task.fromString(line);
                            inMemoryTaskManager.addTask(task);
                            break;
                        case EPIC:
                            Epic epic = Epic.fromString(line);
                            inMemoryTaskManager.addEpic(epic);
                            break;
                        case SUBTASK:
                            Subtask subtask = Subtask.fromString(line);
                            inMemoryTaskManager.addSubtask(subtask);
                            break;
                        default:
                            String historyLine = bufferedReader.readLine();
                            List<Integer> history = historyFromString(historyLine);
                            for (Integer hist : history) {
                                inMemoryTaskManager.getTask(hist);
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new FileBackedTasksManager(inMemoryHistoryManager, "resources/tasks.csv");
    }

    //переопределение модифицирующих операций
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    public void removeTask(Integer taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeSubtask(Integer subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }

    @Override
    public void removeEpic(Integer epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public Task getTask(Integer taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(Integer subtaskId) {
        Subtask subtask = super.getSubtask(subtaskId);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(Integer epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }
}
