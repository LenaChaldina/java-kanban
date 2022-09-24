package practicum.service;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

class InMemoryTaskManagerTest extends TaskManagerServiceTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() throws IOException {
        super.beforeEach();
    }
}