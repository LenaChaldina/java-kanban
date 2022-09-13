package practicum.service;

import practicum.constants.Status;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManagerService {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    public Set<Task> taskAndSubtaskPriority = new TreeSet<>((o1, o2) -> {
        if ((o1.getStartTime() == null) && (o2.getStartTime() != null)) {
            return 1;
        } else if ((o1.getStartTime() != null) && (o2.getStartTime() == null)) {
            return -1;
        } else if ((o1.getStartTime() == null) && (o2.getStartTime() == null)) {
            return 0;
        } else {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    });
    public HashMap<Interval, Boolean> intervals = new HashMap<>();
    private int generator = 0;
    protected final HistoryManagerService inMemoryHistoryManager;

    public InMemoryTaskManager(HistoryManagerService inMemoryHistoryManager) {
        this.inMemoryHistoryManager = inMemoryHistoryManager;
    }

    @Override
    public HistoryManagerService getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public HashMap<Integer, Task> getResultTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getResultSubtasks() {
        return subtasks;
    }

    @Override
    public HashMap<Integer, Epic> getResultEpics() {
        return epics;
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            if (getValidations(task)) {
                int taskId = generator++;
                task.setId(taskId);
                taskAndSubtaskPriority.add(task);
                tasks.put(taskId, task);
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            int epicId = generator++;
            epic.setId(epicId);
            epics.put(epicId, epic);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask != null) {
            if (getValidations(subtask)) {
                int epicId = subtask.getEpicID();
                Epic epic = epics.get(epicId);

                if (epic == null) {
                    return;
                }

                //сохраняем подзадачу
                int subtaskId = generator++;
                subtask.setId(subtaskId);
                taskAndSubtaskPriority.add(subtask);
                subtasks.put(subtaskId, subtask);
                //добавляем эпик в подзадачу
                epic.addSubTask(subtaskId);
                updateEpicStatus(epic);
                updateEpicDuration(epic);
            }
        }
    }

    @Override
    //Получение списка всех задач.
    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskValues = new ArrayList<>();
        for (Task task : tasks.values()) {
            taskValues.add(task);
        }
        return taskValues;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicValues = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicValues.add(epic);
        }
        return epicValues;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtaskValues = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtaskValues.add(subtask);
        }
        return subtaskValues;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        //логика определения статуса эпика
        /*если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
        если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
        во всех остальных случаях статус должен быть IN_PROGRESS.*/
        if (epic != null) {
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
    }

    @Override
    //Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        ArrayList<Integer> subTaskIds = epic.getSubTaskIds();
        for (Integer subTaskId : subTaskIds) {
            if (subtasks.containsKey(subTaskId)) {
                epicSubtasks.add(subtasks.get(subTaskId));
            }
        }
        return epicSubtasks;
    }

    @Override
    //Удаление всех задач.
    public void removeTasks() {
        taskAndSubtaskPriority.removeAll(tasks.values());
        tasks.clear();
    }

    @Override
    public void removeSubtasks() {
        taskAndSubtaskPriority.removeAll(subtasks.values());
        subtasks.clear();
        //обнуляем у всех эпиков список сабтасков
        for (Epic epic : epics.values()) {
            epic.getSubTaskIds().clear();
        }
        //после удаления всех сабтасков все статусы эпиков должны стать NEW
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);

        }
    }

    @Override
    public void removeEpics() {
        //сабтаски без эпика не нужны
        taskAndSubtaskPriority.removeAll(subtasks.values());
        subtasks.clear();
        epics.clear();
    }

    @Override
    //Получение по идентификатору.
    public Task getTask(Integer taskId) {
        Task tmpTask = tasks.get(taskId);
        if (tmpTask != null) {
            //история просмотренных тасков
            inMemoryHistoryManager.addTask(tmpTask);
        }
        return tmpTask;
    }

    @Override
    public Subtask getSubtask(Integer subtaskId) {
        Subtask tmpSubtask = subtasks.get(subtaskId);
        if (tmpSubtask != null) {
            //история просмотренных сабтасков
            inMemoryHistoryManager.addTask(tmpSubtask);
        }
        return tmpSubtask;
    }

    @Override
    public Epic getEpic(Integer epicId) {
        Epic tmpEpic = epics.get(epicId);
        if (tmpEpic != null) {
            //история просмотренных эпиков
            inMemoryHistoryManager.addTask(tmpEpic);
        }
        return tmpEpic;
    }

    @Override
    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        if (task != null) {
            Integer inputTaskId = task.getId();

            //проверка на то, что такой id уже есть
            if (tasks.containsKey(inputTaskId)) {
                tasks.put(task.getId(), task);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            Integer inputSubtaskId = subtask.getId();

            //если такой сабтаск уже есть в мапе
            if (subtasks.containsKey(inputSubtaskId)) {
                subtasks.put(subtask.getId(), subtask);
                Integer epicID = subtask.getEpicID();
                //добавить в эпик id сабтаска
                getEpic(epicID).addSubTask(subtask.getId());
                //проверка статуса соответсвующего эпика при добавлении сабтаска
                updateEpicStatus(getEpic(epicID));
                updateEpicDuration(getEpic(epicID));
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null) {
            Integer inputEpic = epic.getId();

            if (epics.containsKey(inputEpic)) {
                epics.put(inputEpic, epic);
                //добавляем статус
                updateEpicStatus(epic);
                updateEpicDuration(epic);
            }
        }
    }

    @Override
    //Удаление по идентификатору.
    public void removeTask(Integer taskId) {
        if (getTask(taskId) != null) {
            taskAndSubtaskPriority.remove(getTask(taskId));
        }
        tasks.remove(taskId);
        //удаление из истории просморов
        inMemoryHistoryManager.removeTask(taskId);
    }

    @Override
    public void removeSubtask(Integer subtaskId) {
        Integer epicNum = 0;

        if (subtasks.containsKey(subtaskId)) {
            epicNum = subtasks.get(subtaskId).getEpicID();
            //удалить из эпика id удаленного сабтаска
            getEpic(epicNum).getSubTaskIds().remove(subtaskId);
            //обновление статуса соответсвующего эпика
            updateEpicStatus(getEpic(epicNum));
            updateEpicDuration(getEpic(epicNum));
            taskAndSubtaskPriority.remove(getSubtask(subtaskId));
            subtasks.remove(subtaskId);
            //удаление из истории просморов
            inMemoryHistoryManager.removeTask(subtaskId);
        }
    }

    @Override
    public void removeEpic(Integer epicId) {
        if (epics.containsKey(epicId)) {
            //удалить все сабтаски удаленного эпика
            for (Integer sub : getEpic(epicId).getSubTaskIds()) {
                //удаление из истории просморов
                inMemoryHistoryManager.removeTask(sub);
                //удаление
                taskAndSubtaskPriority.remove(getSubtask(sub));
                subtasks.remove(sub);
            }
            //удаление из истории просморов
            inMemoryHistoryManager.removeTask(epicId);
            //удалить эпик
            epics.remove(epicId);
        }
    }

    // Продолжительность эпика — сумма продолжительности всех его подзадач.
    // Время начала — дата старта самой ранней подзадачи, а время завершения — время окончания самой поздней из задач.
    // Новые поля duration и startTime этого класса будут расчётные — аналогично полю статус.
    // Для реализации getEndTime() удобно добавить поле endTime в Epic и рассчитать его вместе с другими полями.
    public void updateEpicDuration(Epic epic) {
        if (epic != null) {
            ArrayList<Subtask> subtasks = getEpicSubtasks(epic);
            if (!subtasks.isEmpty()) {
                // startTime - Время начала — дата старта самой ранней подзадачи
                Optional<LocalDateTime> startTime = Optional.ofNullable(subtasks.stream()
                        .min(Comparator.comparing(Subtask::getStartTime)).get().getStartTime());

                epic.setStartTime(startTime.get());

                Optional<LocalDateTime> endTime = Optional.ofNullable(subtasks.stream()
                        .max(Comparator.comparing(Subtask::getEndTime)).get().getEndTime());

                epic.setEndTime(endTime.get());

                // Продолжительность эпика — сумма продолжительности всех его подзадач.
                epic.setDuration(Duration.between(startTime.get(), endTime.get()));
            } else {
                epic.setStartTime(null);
                epic.setEndTime(null);
                epic.setDuration(null);
            }
        }
    }

    public Set<Task> getPrioritizedTasks() {
        return taskAndSubtaskPriority;
    }

    public boolean getValidations(Task task) {
        // валидация не имеет смысла, если у задачи не введено время
        if (task.getStartTime() != null) {
            Interval interval = new Interval(task.getStartTime(), task.getEndTime());
            if (intervals.containsKey(interval)) {
                System.out.println("Есть пересечение по времени!");
                return false;
            } else {
                Set<Interval> intervalsGrid = interval.getIntervals();
                for (Interval intervalGrid : intervalsGrid) {
                    intervals.put(intervalGrid, true);
                }
                return true;
            }
        }
        return true;
    }
}