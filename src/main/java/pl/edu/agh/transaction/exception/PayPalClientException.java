package pl.edu.agh.transaction.exception;

public class PayPalClientException extends RuntimeException {
    public PayPalClientException() {
    }

    public PayPalClientException(String message) {
        super(message);
    }

    public PayPalClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayPalClientException(Throwable cause) {
        super(cause);
    }
}
