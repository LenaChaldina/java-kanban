package practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.constants.Status;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static practicum.utility.Managers.getInMemoryTaskManager;

class InMemoryHistoryManagerTest {
    private TaskManagerService inMemoryTaskManager;
    private HistoryManagerService inMemoryHistoryManager;
    private Task task;
    private Task task2;
    private Task task3;
    private Epic epic;
    private Epic epic2;
    private Subtask subtask;
    private Subtask subtask2;
    private Subtask subtask3;
    private List<Task> tasksTest;
    private List<Task> tasksTestForDuplicates;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = getInMemoryTaskManager();
        inMemoryHistoryManager = inMemoryTaskManager.getInMemoryHistoryManager();
        tasksTest = new ArrayList<>();
        tasksTestForDuplicates = new ArrayList<>();

        task = new Task("Посещение бассейна", "Позаниматься плаванием перед работой", Status.NEW, 60, "07:09:2022; 08:00");
        task2 = new Task("Выбор платья", "Подобрать красивый наряд для ужина", Status.IN_PROGRESS, 30, "07:09:2022; 19:10");

        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.addTask(task2);

        epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        epic2 = new Epic("Убора квартиры", "Включает влажную и сухую уборку");

        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addEpic(epic2);

        subtask = new Subtask("Завершить работу над 7-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, 6000, "10:09:2022; 08:00", epic.getId());
        subtask2 = new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.DONE, 80, "08:09:2022; 08:00", epic2.getId());
        subtask3 = new Subtask("сухая уборка", "Пропылесосить и разложить вещи", Status.IN_PROGRESS, 40, "07:09:2022; 12:00", epic2.getId());

        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);

        tasksTest.add(task);
        tasksTest.add(task2);
        tasksTest.add(epic);
        tasksTest.add(epic2);
        tasksTest.add(subtask);
        tasksTest.add(subtask2);
        tasksTest.add(subtask3);

        tasksTestForDuplicates.add(task);
        tasksTestForDuplicates.add(task2);
        tasksTestForDuplicates.add(epic);
        tasksTestForDuplicates.add(epic2);
        tasksTestForDuplicates.add(epic2);
        tasksTestForDuplicates.add(subtask);
        tasksTestForDuplicates.add(subtask);
        tasksTestForDuplicates.add(subtask);
        tasksTestForDuplicates.add(subtask2);
        tasksTestForDuplicates.add(subtask3);
    }

    @Test
    public void getTasksHistory() {
        assertTrue(inMemoryHistoryManager.getTasksHistory().isEmpty());

        inMemoryTaskManager.getTask(0);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getEpic(3);
        inMemoryTaskManager.getSubtask(4);
        inMemoryTaskManager.getSubtask(5);
        inMemoryTaskManager.getSubtask(6);

        assertEquals(tasksTest, inMemoryHistoryManager.getTasksHistory());
        assertNotEquals(tasksTestForDuplicates, inMemoryHistoryManager.getTasksHistory());
    }

    @Test
    public void addTask() {
        assertDoesNotThrow(() -> inMemoryHistoryManager.addTask(null));
        task3 = new Task("--", "--", Status.NEW, 60, "07:09:2022; 23:00");
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.getTask(7);
        inMemoryTaskManager.getTask(20);
        assertEquals(1, inMemoryHistoryManager.getTasksHistory().size());
    }

    @Test
    public void removeTask() {
        //с. Удаление из истории: начало, середина, конец.
        inMemoryTaskManager.getTask(0);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getEpic(3);
        inMemoryTaskManager.getSubtask(4);
        inMemoryTaskManager.getSubtask(5);
        inMemoryTaskManager.getSubtask(6);

        inMemoryTaskManager.removeTask(0);
        inMemoryTaskManager.removeTask(3);
        inMemoryTaskManager.removeTask(6);

        assertEquals(4, inMemoryHistoryManager.getTasksHistory().size());
        assertTrue(inMemoryHistoryManager.getTasksHistory().contains(epic));
        assertTrue(inMemoryHistoryManager.getTasksHistory().contains(subtask));
    }
}