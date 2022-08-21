package practicum.exceptions;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(final IOException message) {
        super(message);
    }


}
