package pl.edu.agh.transaction.exception;

public class IllegalDatabaseState extends RuntimeException {
    public IllegalDatabaseState() {}

    public IllegalDatabaseState(String message) {
        super(message);
    }

    public IllegalDatabaseState(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDatabaseState(Throwable cause) {
        super(cause);
    }
}
