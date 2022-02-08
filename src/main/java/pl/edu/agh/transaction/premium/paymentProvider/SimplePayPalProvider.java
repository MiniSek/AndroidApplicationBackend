package pl.edu.agh.transaction.premium.paymentProvider;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.transaction.exception.PayPalClientException;
import pl.edu.agh.transaction.premium.paymentProvider.paypalEnvironment.PayPalEnvironment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SimplePayPalProvider implements PaymentProvider {
    private final PayPalHttpClient client;

    private int orderIdx = 0;
    private final List<Order> cacheOrders = new ArrayList<>();

    private final String INTENT;
    private final String CURRENCY;

    @Autowired
    public SimplePayPalProvider(@Qualifier("sandbox") PayPalEnvironment paypalEnvironment,
                                @Value("${INTENT}") String INTENT,
                                @Value("${CURRENCY}") String CURRENCY) {

        this.client = paypalEnvironment.getHttpClient();

        this.INTENT = INTENT;
        this.CURRENCY = CURRENCY;
    }

    @Override
    public String createOrder(Double price) throws IOException, PayPalClientException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent(INTENT);
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(
                new PurchaseUnitRequest().amountWithBreakdown(
                        new AmountWithBreakdown().currencyCode(CURRENCY).value(String.valueOf(price))));
        orderRequest.purchaseUnits(purchaseUnits);

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        HttpResponse<Order> response;
        if(client != null)
            response = client.execute(request);
        else
            throw new PayPalClientException();

        Order order = response.result();

        cacheOrders.add(orderIdx, order);
        orderIdx = (orderIdx+1)%10;

        return order.id();
    }

    @Override
    public String getPaymentLink(String orderId) throws IOException, PayPalClientException {
        for(Order order : cacheOrders)
            if(order.id().equals(orderId))
                return order.links().get(1).href();

        OrdersGetRequest request = new OrdersGetRequest(orderId);

        HttpResponse<Order> response;
        if(client != null)
            response = client.execute(request);
        else
            throw new PayPalClientException();

        Order order = response.result();

        return order.links().get(1).href();
    }

    @Override
    public void captureOrder(String orderId) throws IOException, PayPalClientException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);

        if(client != null)
            client.execute(request);
        else
            throw new PayPalClientException();
    }

    public String getCreationTime(String orderId) throws IOException {
        for(Order order : cacheOrders)
            if(order.id().equals(orderId))
                return order.createTime();

        OrdersGetRequest request = new OrdersGetRequest(orderId);

        HttpResponse<Order> response;
        if(client != null)
            response = client.execute(request);
        else
            throw new PayPalClientException();

        Order order = response.result();
        return order.createTime();
    }
}