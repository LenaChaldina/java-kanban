package practicum.service;

import org.junit.jupiter.api.*;
import practicum.constants.Status;
import practicum.http.server.KVServer;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static practicum.utility.Managers.getInMemoryTaskManager;

public abstract class TaskManagerServiceTest<T extends TaskManagerService> {
    protected InMemoryTaskManager inMemoryTaskManager;
    protected HashMap<Integer, Task> testTasks;
    protected HashMap<Integer, Subtask> testSubtasks;
    protected HashMap<Integer, Epic> testEpics;
    protected List<Subtask> testEpicSubtasks;
    protected Task task1;
    protected Task task2;
    protected Epic epic;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Subtask subtask3;


    @BeforeEach
    public void beforeEach() throws IOException {
        inMemoryTaskManager = (InMemoryTaskManager) getInMemoryTaskManager();
        testTasks = new HashMap<>();
        testSubtasks = new HashMap<>();
        testEpics = new HashMap<>();
        testEpicSubtasks = new ArrayList<>();

        task1 = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW, 60, "07:09:2022; 08:00");
        task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS, 30, "07:09:2022; 19:10");

        epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории", Status.NEW);

        subtask1 = new Subtask("Завершить работу над 7-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, 20, "10:09:2022; 08:00", 0);
        subtask2 = new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.DONE, 20, "10:09:2022; 19:00", 0);
        subtask3 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, 40, "10:09:2022; 17:00", 0);

        testTasks.put(0, task1);
        testTasks.put(1, task2);
        testEpics.put(0, epic);
        testSubtasks.put(1, subtask1);
        testSubtasks.put(2, subtask2);
        testSubtasks.put(3, subtask3);

        testEpicSubtasks.add(subtask1);
        testEpicSubtasks.add(subtask2);
        testEpicSubtasks.add(subtask3);
    }

    @Test
    public void addTask() {
        assertDoesNotThrow(() -> inMemoryTaskManager.addTask(null));
        assertTrue(inMemoryTaskManager.getResultTasks().isEmpty());
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        assertEquals(testTasks, inMemoryTaskManager.getResultTasks());
    }

    @Test
    public void addEpic() {
        assertDoesNotThrow(() -> inMemoryTaskManager.addEpic(null));
        assertTrue(inMemoryTaskManager.getEpics().isEmpty());
        inMemoryTaskManager.addEpic(epic);
        assertEquals(testEpics, inMemoryTaskManager.getResultEpics());
    }

    @Test
    public void addSubtask() {
        assertDoesNotThrow(() -> inMemoryTaskManager.addSubtask(null));
        assertTrue(inMemoryTaskManager.getSubtasks().isEmpty());
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        assertEquals(testSubtasks, inMemoryTaskManager.getResultSubtasks());
    }

    @Test
    @DisplayName("Проверка получения тасок")
    void getTasks() {
        List<Task> tasks = inMemoryTaskManager.getTasks();
        assertTrue(tasks.isEmpty());
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        tasks = inMemoryTaskManager.getTasks();
        List<Task> finalTasks = tasks;
        Assertions.assertAll(
                () -> Assertions.assertEquals(finalTasks.size(), 2),
                () -> Assertions.assertTrue(finalTasks.contains(task1)),
                () -> Assertions.assertTrue(finalTasks.contains(task2))
        );
    }

    @Test
    @DisplayName("Проверка получения эпиков")
    void getEpics() {
        List<Epic> epics = inMemoryTaskManager.getEpics();
        assertTrue(epics.isEmpty());
        inMemoryTaskManager.addEpic(epic);
        epics = inMemoryTaskManager.getEpics();
        List<Epic> finalEpics = epics;
        Assertions.assertAll(
                () -> Assertions.assertEquals(finalEpics.size(), 1),
                () -> Assertions.assertTrue(finalEpics.contains(epic))
        );
    }

    @Test
    @DisplayName("Проверка получения сабтасков")
    void getSubtasks() {
        List<Subtask> subtasks = inMemoryTaskManager.getSubtasks();
        assertTrue(subtasks.isEmpty());
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        subtasks = inMemoryTaskManager.getSubtasks();
        List<Subtask> finalSubtasks = subtasks;
        Assertions.assertAll(
                () -> Assertions.assertEquals(finalSubtasks.size(), 3),
                () -> Assertions.assertTrue(finalSubtasks.contains(subtask1)),
                () -> Assertions.assertTrue(finalSubtasks.contains(subtask2)),
                () -> Assertions.assertTrue(finalSubtasks.contains(subtask3))
        );
    }

    @Test
    public void updateEpicStatus() {
        assertDoesNotThrow(() -> inMemoryTaskManager.updateEpicStatus(null));
        //если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(epic.getStatus(), Status.NEW);
        //если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
        inMemoryTaskManager.addSubtask(new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.DONE, 20, "10:09:2022; 19:00", epic.getId()));
        assertEquals(epic.getStatus(), Status.DONE);
        //во всех остальных случаях статус должен быть IN_PROGRESS
        inMemoryTaskManager.addSubtask(new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, 40, "10:09:2022; 17:00", epic.getId()));
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void getEpicSubtasks() {
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);

        assertEquals(testEpicSubtasks, inMemoryTaskManager.getEpicSubtasks(epic));

    }

    @Test
    public void removeTasks() {
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.removeTasks();
        assertTrue(inMemoryTaskManager.getResultTasks().isEmpty());
    }

    @Test
    public void removeEpics() {
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.removeEpics();
        assertTrue(inMemoryTaskManager.getResultEpics().isEmpty());
    }

    @Test
    public void removeSubtasks() {
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.removeSubtasks();
        assertTrue(inMemoryTaskManager.getResultSubtasks().isEmpty());
    }

    @Test
    public void getTask() {
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        assertNull(inMemoryTaskManager.getTask(5));
        assertEquals(task1, inMemoryTaskManager.getTask(0));
    }

    @Test
    public void getEpic() {
        inMemoryTaskManager.addEpic(epic);
        assertNull(inMemoryTaskManager.getEpic(5));
        assertEquals(epic, inMemoryTaskManager.getEpic(0));
    }

    @Test
    public void getSubtask() {
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        assertNull(inMemoryTaskManager.getSubtask(5));
        assertEquals(subtask1, inMemoryTaskManager.getSubtask(1));
    }

    @Test
    public void updateTask() {
        assertDoesNotThrow(() -> inMemoryTaskManager.updateTask(null));
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        task1 = new Task(0, "--", "--", Status.NEW, 60, "07:09:2022; 08:00");
        inMemoryTaskManager.updateTask(task1);
        assertEquals(task1, inMemoryTaskManager.getTask(0));
        task1 = new Task(5, "--", "--", Status.NEW, 60, "07:09:2022; 08:00");
        inMemoryTaskManager.updateTask(task1);
        assertNull(inMemoryTaskManager.getTask(5));
    }

    @Test
    public void updateEpic() {
        assertDoesNotThrow(() -> inMemoryTaskManager.updateEpic(null));
        inMemoryTaskManager.addEpic(epic);
        epic = new Epic(0, "--я", "--");
        inMemoryTaskManager.updateEpic(epic);
        assertEquals(epic, inMemoryTaskManager.getEpic(0));
        epic = new Epic(5, "--я", "--");
        inMemoryTaskManager.updateEpic(epic);
        assertNull(inMemoryTaskManager.getEpic(5));
    }

    @Test
    public void updateSubtask() {
        assertDoesNotThrow(() -> inMemoryTaskManager.updateSubtask(null));
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        subtask1 = new Subtask(1, "--", "--", Status.NEW, 20, "10:09:2022; 08:00", epic.getId());
        inMemoryTaskManager.updateSubtask(subtask1);
        assertEquals(subtask1, inMemoryTaskManager.getSubtask(1));
        subtask1 = new Subtask(5, "--", "--", Status.NEW, 20, "10:09:2022; 08:00", epic.getId());
        inMemoryTaskManager.updateSubtask(subtask1);
        assertNull(inMemoryTaskManager.getSubtask(5));
    }

    @Test
    public void removeTask() {
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.removeTask(0);
        inMemoryTaskManager.removeTask(5);
        assertEquals(inMemoryTaskManager.getResultTasks().size(), 1);
        inMemoryTaskManager.removeTask(1);
        assertEquals(inMemoryTaskManager.getResultTasks().size(), 0);
    }

    @Test
    public void removeEpic() {
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.removeEpic(0);
        inMemoryTaskManager.removeEpic(5);
        assertEquals(inMemoryTaskManager.getResultEpics().size(), 0);
    }

    @Test
    public void removeSubtask() {
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.removeSubtask(1);
        inMemoryTaskManager.removeSubtask(5);
        assertEquals(inMemoryTaskManager.getResultSubtasks().size(), 2);
    }

    @Test
    public void updateEpicDuration() {
        assertDoesNotThrow(() -> inMemoryTaskManager.updateEpicDuration(null));
        //остальные кейсы тестирую в эпике
    }

    @Test
    public void isValid() {
        // валидация не имеет смысла, если у задачи не введено время
        task1 = new Task(0, "--", "--", Status.NEW);
        assertTrue(inMemoryTaskManager.isValid(task1));
        //если есть пересечения по времени
        task1 = new Task("--", "--", Status.NEW, 60, "07:09:2022; 08:01");
        task2 = new Task("--", "--", Status.IN_PROGRESS, 30, "07:09:2022; 08:10");
        assertTrue(inMemoryTaskManager.isValid(task1));
        assertFalse(inMemoryTaskManager.isValid(task2));
    }
}