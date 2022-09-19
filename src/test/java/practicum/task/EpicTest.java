package practicum.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.constants.Status;
import practicum.service.HistoryManagerService;
import practicum.service.TaskManagerService;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static practicum.utility.Managers.getInMemoryTaskManager;

class EpicTest {
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;
    private TaskManagerService inMemoryTaskManager;
    private HistoryManagerService inMemoryHistoryManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = getInMemoryTaskManager();
        inMemoryHistoryManager = inMemoryTaskManager.getInMemoryHistoryManager();
        epic = new Epic("Улучшить навыки программирования", "Написание кода и изучение теории");
        inMemoryTaskManager.addEpic(epic);
        subtask1 = new Subtask("Завершить работу над 7-им спринтом", "Закончить теоретическую и практическую части", Status.NEW, 6000, "10:09:2022; 08:00", epic.getId());
        subtask2 = new Subtask("Влажная уборка", "Помыть пол и протереть пыль", Status.NEW, 80, "08:09:2022; 08:00", epic.getId());
    }

    @Test
    public void should_return_status_wew_for_empty_list_of_subtask() {
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void should_return_status_new_for_list_of_subtasks_wits_statusNew() {
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void should_return_status_done_for_list_of_subtasks_wits_statusDone() {
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void should_return_status_inProgress_for_list_of_subtasks_wits_status_done_and_new() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void should_return_status_inProgress_for_list_of_subtasks_wits_status_inProgress() {
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void should_Return_Start_Time_Of_The_First_Subtask() {
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        assertEquals(epic.getStartTime(), subtask2.getStartTime());
    }

    @Test
    public void should_return_rnd_rime_of_the_latest_subtask() {
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        assertEquals(epic.getEndTime(), subtask1.getEndTime());
    }

    @Test
    public void should_return_duration_between_the_earliest_and_the_latest_subtask() {
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        assertEquals(epic.getDuration(), Duration.between(subtask2.getStartTime(), subtask1.getEndTime()));
    }
}