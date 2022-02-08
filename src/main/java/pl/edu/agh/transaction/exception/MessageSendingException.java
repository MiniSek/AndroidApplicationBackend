package pl.edu.agh.transaction.exception;

public class MessageSendingException extends RuntimeException {
    public MessageSendingException() {
    }

    public MessageSendingException(String message) {
        super(message);
    }

    public MessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageSendingException(Throwable cause) {
        super(cause);
    }
}
