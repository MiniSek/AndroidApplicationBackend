package pl.edu.agh.transaction.ServiceLayer.premium;

import java.io.IOException;

public interface PaymentProvider {
    String createOrder(String intent, String currency, Double price) throws IOException;
    String getPaymentLink(String orderId) throws IOException;
    void captureOrder(String orderId) throws IOException;
    String getExpirationTime(String orderId) throws IOException;
}
