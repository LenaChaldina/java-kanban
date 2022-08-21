package practicum.service;

import practicum.constants.Status;
import practicum.exceptions.ManagerSaveException;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//вторая реализация менеджера = хранит информацию в файле
public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManagerService {
    private static String fileName = "resources/tasks.csv";

    //Пусть новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле.
    public FileBackedTasksManager(HistoryManagerService inMemoryHistoryManager, String fileName) {
        super(inMemoryHistoryManager);
        this.fileName = fileName;
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
            bufferedWriter.write(toStringForTasksOrEpics(value));
            bufferedWriter.newLine();
        }
    }

    private void addSubtasksToFile(BufferedWriter bufferedWriter, Collection<Subtask> subtasks) throws IOException {
        for (Subtask value : subtasks) {
            bufferedWriter.write(toStringForSubtask(value));
            bufferedWriter.newLine();
        }
    }

    //Напишите метод сохранения задачи в строку String toString(Task task) или переопределите базовый.
    public <T extends Task> String toStringForTasksOrEpics(T task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + ",";
    }

    public String toStringForSubtask(Subtask subtask) {
        return subtask.getId() + "," + subtask.getTaskType() + "," + subtask.getName() + "," + subtask.getStatus() + ","
                + subtask.getDescription() + "," + subtask.getEpicID();
    }

    // Напишите метод создания задачи из строки Task fromString(String value)
    // раскидать задачки по полям
    public static Task fromStringForTasks(String value) {
        String[] tasksFromFile = value.split(",");
        Integer id = Integer.valueOf(tasksFromFile[0]);
        String name = tasksFromFile[2];
        String description = tasksFromFile[4];
        Status status = Status.valueOf(tasksFromFile[3]);

        Task task = new Task(id, name, description, status);
        return task;
    }

    public static Epic fromStringForEpics(String value) {
        String[] epicsFromFile = value.split(",");
        Integer id = Integer.valueOf(epicsFromFile[0]);
        String name = epicsFromFile[2];
        String description = epicsFromFile[4];

        Epic epic = new Epic(id, name, description);
        return epic;
    }

    public static Subtask fromStringForSubtasks(String value) {
        String[] subtasksFromFile = value.split(",");
        Integer id = Integer.valueOf(subtasksFromFile[0]);
        String name = subtasksFromFile[2];
        String description = subtasksFromFile[4];
        Status status = Status.valueOf(subtasksFromFile[3]);
        Integer epicID = Integer.valueOf(subtasksFromFile[5]);

        Subtask subtask = new Subtask(id, name, description, status, epicID);
        return subtask;
    }

    // cохранение менеджера истории
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

                if (!line.startsWith("id")) {
                    if (line.contains(",TASK")) {
                        Task task = fromStringForTasks(line);
                        inMemoryTaskManager.addTask(task);
                    } else if (line.contains("EPIC")) {
                        Epic epic = fromStringForEpics(line);
                        inMemoryTaskManager.addEpic(epic);
                    } else if (line.contains("SUBTASK")) {
                        Subtask subtask = fromStringForSubtasks(line);
                        inMemoryTaskManager.addSubtask(subtask);
                    }
                }
                if (line.isBlank()) {
                    String line1 = bufferedReader.readLine();
                    List<Integer> history = historyFromString(line1);
                    for (Integer hist : history) {
                        inMemoryTaskManager.getTask(hist);
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

    ;

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
