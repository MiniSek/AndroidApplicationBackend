package pl.edu.agh.transaction.exception;

public class PriceListLoadException extends RuntimeException{
    public PriceListLoadException() {
    }

    public PriceListLoadException(String message) {
        super(message);
    }

    public PriceListLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PriceListLoadException(Throwable cause) {
        super(cause);
    }
}
