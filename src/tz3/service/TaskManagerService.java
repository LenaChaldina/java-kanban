package tz3.service;

import tz3.constants.Status;
import tz3.task.Epic;
import tz3.task.Subtask;
import tz3.task.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManagerService {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    private int generator = 0;

    public HashMap<Integer, Task> getResultTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getResultSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getResultEpics() {
        return epics;
    }

    public void addTask(Task task) {
        int taskId = generator++;
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    public void addEpic(Epic epic) {
        int epicId = generator++;
        epic.setId(epicId);
        epics.put(epicId, epic);
    }

    public void addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicID();
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return;
        }

        //сохраняем подзадачу
        int subtaskId = generator++;
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);
        //добавляем эпик в подзадачу
        epic.addSubTask(subtaskId);
        updateEpicStatus(epic);
    }

    //Получение списка всех задач.
    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskValues = new ArrayList<>();
        for (Task task : tasks.values()) {
            taskValues.add(task);
        }
        return taskValues;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicValues = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicValues.add(epic);
        }
        return epicValues;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtaskValues = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtaskValues.add(subtask);
        }
        return subtaskValues;
    }

    public void updateEpicStatus(Epic epic) {
        //логика определения статуса эпика
        /*если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
        если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
        во всех остальных случаях статус должен быть IN_PROGRESS.*/
        int countSubtasks = getEpicSubtasks(epic).size();
        int counterNEW = 0;
        int counterDONE = 0;

        if (getEpicSubtasks(epic).isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        for (Subtask sub : getEpicSubtasks(epic)) {
            if (sub.getStatus().equals(Status.NEW)) {
                counterNEW += 1;
            } else if (sub.getStatus().equals(Status.DONE)) {
                counterDONE += 1;
            }
        }
        if (counterNEW == countSubtasks) {
            epic.setStatus(Status.NEW);
        } else if (counterDONE == countSubtasks) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        ArrayList<Integer> subTaskIds = epic.getSubTaskIds();
        for (Integer subTaskId : subTaskIds) {
            if (subtasks.keySet().contains(subTaskId)) {
                epicSubtasks.add(subtasks.get(subTaskId));
            }
        }
        return epicSubtasks;
    }

    //Удаление всех задач.
    public void removeTasks() {
        tasks.clear();
    }

    public void removeSubtasks() {
        subtasks.clear();
        //обнуляем у всех эпиков список сабтасков
        for (Epic epic : epics.values()) {
            epic.getSubTaskIds().clear();
        }
        //после удаления всех сабтасков все статусы эпиков должны стать NEW
        for(Epic epic: epics.values()) {
            epic.setStatus(Status.NEW);
        }
    }

    public void removeEpics() {
        //сабтаски без эпика не нужны
        subtasks.clear();
        epics.clear();
    }

    //Получение по идентификатору.
    public Task getTask(Integer taskId) {
        Task tmpTask = tasks.get(taskId);
            if (tasks.keySet().contains(taskId)) {
                tmpTask = tasks.get(taskId);
            }
        return tmpTask;
    }

    public Subtask getSubtask(Integer subtaskId) {
        Subtask tmpSubtask = subtasks.get(subtaskId);
        if (subtasks.keySet().contains(subtaskId)) {
                tmpSubtask = subtasks.get(subtaskId);
        }
        return tmpSubtask;
    }

    public Epic getEpic(Integer epicId) {
        Epic tmpEpic = epics.get(epicId);
        if (epics.keySet().contains(epicId)) {
                tmpEpic = epics.get(epicId);
        }
        return tmpEpic;
    }

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        Integer inputTaskId = task.getId();

        //проверка на то, что такой id уже есть
        if (tasks.keySet().contains(inputTaskId)) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        Integer inputSubtaskId = subtask.getId();

        //если такой сабтаск уже есть в мапе
        if (subtasks.keySet().contains(inputSubtaskId)) {
            subtasks.put(subtask.getId(), subtask);
            Integer epicID = subtask.getEpicID();
            //добавить в эпик id сабтаска
            getEpic(epicID).addSubTask(subtask.getId());
            //проверка статуса соответсвующего эпика при добавлении сабтаска
            updateEpicStatus(getEpic(epicID));
        }

    }

    public void updateEpic(Epic epic) {
        Integer inputEpic = epic.getId();

        if (epics.keySet().contains(inputEpic)) {
            epics.put(inputEpic, epic);
            //добавляем статус
            updateEpicStatus(epic);
        }
    }

    //Удаление по идентификатору.
    public void removeTask(Integer taskId) {
        tasks.remove(taskId);
    }

    public void removeSubtask(Integer subtaskId) {
        Integer epicNum = 0;

        if (subtasks.containsKey(subtaskId)) {
            epicNum = subtasks.get(subtaskId).getEpicID();
            //удалить из эпика id удаленного сабтаска
            getEpic(epicNum).getSubTaskIds().remove(subtaskId);
            //обновление статуса соответсвующего эпика
            updateEpicStatus(getEpic(epicNum));
            subtasks.remove(subtaskId);
        }
    }

    public void removeEpic(Integer epicId) {
        if (epics.containsKey(epicId)) {
            //удалить все сабтаски удаленного эпика
            for (Integer sub : getEpic(epicId).getSubTaskIds()) {
                subtasks.remove(sub);
            }
            //удалить эпик
            epics.remove(epicId);
        }
    }


}