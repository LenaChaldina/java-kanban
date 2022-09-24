package practicum.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import practicum.http.client.KVTaskClient;
import practicum.service.*;

public class Managers {
    private Managers(){}
    public static HistoryManagerService getInMemoryHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManagerService getInMemoryTaskManager() {
        return new InMemoryTaskManager(getInMemoryHistoryManager());
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
       // gsonBuilder.registerTypeAdapter(LocalDateTime.class /*, new LocalDateTimeAdapter()*/);
        return gsonBuilder.create();
    }

    public static KVTaskClient getKVTaskClient() {
        return new KVTaskClient("http://localhost:8078/");
    }

    public static HttpTaskManager getHttpTaskManager(Gson gson) {
        return new HttpTaskManager(getInMemoryHistoryManager(), getKVTaskClient(), gson);
    }

}

