package pl.edu.agh.transaction.ServiceLayer.premium;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimplePayPalProvider implements PaymentProvider {
    private static String CLIENT_ID = "AULU5sCxqQT3IEKc2wEqY4MVt3TAnbx5BmbLgJFHVkDpf-OPVp3rA_SF0WIbKsvEZHXH4eiM2pdAuVWD";
    private static String SECRET = "EIYAKw4f5S9VyX8F3sLMHlt39-TxNXdH1p7w36p6zl2VohuvB5KMJLOb2_eF6W_To_faq03grqrS9_TD";

    private static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(CLIENT_ID, SECRET);
    private static PayPalHttpClient client = new PayPalHttpClient(environment);

    @Override
    public String createOrder(String intent, String currency, Double price) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent(intent);
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(
                new PurchaseUnitRequest().amountWithBreakdown(
                        new AmountWithBreakdown().currencyCode(currency).value(String.valueOf(price))));
        orderRequest.purchaseUnits(purchaseUnits);

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        HttpResponse<Order> response = SimplePayPalProvider.client.execute(request);

        Order order = response.result();
        return order.id();
    }

    @Override
    public String getPaymentLink(String orderId) throws IOException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = SimplePayPalProvider.client.execute(request);

        Order order = response.result();

        return order.links().get(1).href();
    }

    @Override
    public void captureOrder(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);

        HttpResponse<Order> response = SimplePayPalProvider.client.execute(request);
    }

    public String getExpirationTime(String orderId) throws IOException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = SimplePayPalProvider.client.execute(request);

        Order order = response.result();

        return order.expirationTime();
    }
}
