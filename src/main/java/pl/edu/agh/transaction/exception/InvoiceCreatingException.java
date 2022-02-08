package pl.edu.agh.transaction.exception;

public class InvoiceCreatingException extends RuntimeException {
    public InvoiceCreatingException() {
    }

    public InvoiceCreatingException(String message) {
        super(message);
    }

    public InvoiceCreatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvoiceCreatingException(Throwable cause) {
        super(cause);
    }
}
