package practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerServiceTest<FileBackedTasksManager> {
    private File file;
    private HistoryManagerService inMemoryHistoryManager;
    private FileBackedTasksManager fileBackedTasksManager;

    @BeforeEach
    public void beforeEach() throws IOException {
        file = new File("src/test/java/resources/tasksForTest");
        super.beforeEach();
        inMemoryHistoryManager = inMemoryTaskManager.getInMemoryHistoryManager();

    }

    @Test
    public void loadFromFile() {
        fileBackedTasksManager = FileBackedTasksManager.loadFromFile(inMemoryTaskManager, inMemoryHistoryManager, file);
        List<Task> tasks = inMemoryTaskManager.getTasks();
        List<Epic> epics = inMemoryTaskManager.getEpics();
        List<Subtask> subtasks = inMemoryTaskManager.getSubtasks();
        assertEquals(2, tasks.size());
        assertEquals(2, epics.size());
        assertEquals(3, subtasks.size());
        assertEquals(tasks, inMemoryHistoryManager.getTasksHistory());
    }

    @Test
    public void save() {
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);

        inMemoryTaskManager.getTask(0);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getSubtask(3);
        inMemoryTaskManager.getSubtask(4);
        inMemoryTaskManager.getSubtask(5);

        try {
            List<String> testSave = Files.readAllLines(Paths.get(String.valueOf(file)));
            List<String> exampleSave = Files.readAllLines(Paths.get("src/test/java/resources/tasksForTest"));
            assertEquals(exampleSave, testSave);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}