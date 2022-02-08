package pl.edu.agh.transaction.premium.paymentProvider;

import java.io.IOException;

public interface PaymentProvider {
    String createOrder(Double price) throws IOException;
    String getPaymentLink(String orderId) throws IOException;
    void captureOrder(String orderId) throws IOException;
    String getCreationTime(String orderId) throws IOException;
}
