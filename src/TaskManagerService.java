import java.util.HashMap;
import java.util.ArrayList;

public class TaskManagerService {
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();

    private int generator = 0;

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
            for (Integer subtask : subtasks.keySet()) {
                if (subtask.equals(subTaskId)) {
                    epicSubtasks.add(subtasks.get(subtask));
                }
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
    }

    public void removeEpics() {
        //сабтаски без эпика не нужны
        subtasks.clear();
        epics.clear();
    }

    //Получение по идентификатору.
    public Task getTask(Integer taskId) {
        Task tmpTask = new Task("", null, null);
        for (Integer task : tasks.keySet()) {
            if (task.equals(taskId)) {
                tmpTask = tasks.get(task);
                break;
            }
        }
        return tmpTask;
    }

    public Subtask getSubtask(Integer subtaskId) {
        Subtask tmpSubtask = new Subtask(null, null, null, 0);
        for (Integer subtask : subtasks.keySet()) {
            if (subtask.equals(subtaskId)) {
                tmpSubtask = subtasks.get(subtask);
                break;
            }
        }
        return tmpSubtask;
    }

    public Epic getEpic(Integer epicId) {
        Epic tmpEpic = new Epic(null, null);
        for (Integer epic : epics.keySet()) {
            if (epic.equals(epicId)) {
                tmpEpic = epics.get(epic);
                break;
            }
        }
        return tmpEpic;
    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    public void createTask(Task task) {
        addTask(task);
    }

    public void createEpic(Epic epic) {
        addEpic(epic);
    }

    public void createSubtask(Subtask subtask) {
        addSubtask(subtask);
    }

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        Integer inputTaskId = task.getId();
        //проверка на то, что такой id уже есть
        for (Integer taskId : tasks.keySet()) {
            if (taskId.equals(inputTaskId)) {
                tasks.put(task.getId(), task);
            }
        }
    }

    public void updateSubtask(Subtask subtask) {
        Integer inputSubtaskId = subtask.getId();
        for (Integer sub : subtasks.keySet()) {
            //если такой сабтаск уже есть в мапе
            if (sub.equals(inputSubtaskId)) {
                subtasks.put(subtask.getId(), subtask);
                Integer epicID = subtask.getEpicID();
                //добавить в эпик id сабтаска
                getEpic(epicID).addSubTask(subtask.getId());
                //проверка статуса соответсвующего эпика при добавлении сабтаска
                updateEpicStatus(getEpic(epicID));
            }
        }
    }

    public void updateEpic(Epic epic) {
        Integer inputEpic = epic.getId();
        for (Integer ep : epics.keySet()) {
            if (ep.equals(inputEpic)) {
                epics.put(inputEpic, epic);
                //добавляем статус
                updateEpicStatus(epic);
            }
        }
    }

    //Удаление по идентификатору.
    public void removeTask(Integer taskId) {
        for (Integer task : tasks.keySet()) {
            if (task.equals(taskId)) {
                tasks.remove(task);
                break;
            }
        }
    }

    public void removeSubtask(Integer subtaskId) {
        Integer epicNum = 0;
        for (Integer subtask : subtasks.keySet()) {
            if (subtask.equals(subtaskId)) {
                epicNum = subtasks.get(subtask).getEpicID();
                //удалить из эпика id удаленного сабтаска
                getEpic(epicNum).getSubTaskIds().remove(subtaskId);
                //обновление статуса соответсвующего эпика
                updateEpicStatus(getEpic(epicNum));
                subtasks.remove(subtask);
                break;
            }
        }
    }

    public void removeEpic(Integer epicId) {
        for (Integer epic : epics.keySet()) {
            if (epic.equals(epicId)) {
                //удалить все сабтаски удаленного эпика
                for (Integer sub : getEpic(epicId).getSubTaskIds()) {
                    subtasks.remove(sub);
                }
                //удалить эпик
                epics.remove(epic);
                break;
            }
        }

    }
}