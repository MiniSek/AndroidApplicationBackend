package pl.edu.agh.transaction.premium;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;
import pl.edu.agh.transaction.order.orderDao.PaymentOrderDaoServiceLayer;
import pl.edu.agh.transaction.order.orderModels.PaymentOrder;

import java.io.IOException;

@Service
public class PremiumTransaction {
    private final PaymentProvider paymentProvider;
    private final PaymentOrderDaoServiceLayer paymentOrderDaoServiceLayer;

    private final String INTENT;
    private final String CURRENCY;

    @Autowired
    public PremiumTransaction(PaymentProvider paymentProvider, PaymentOrderDaoServiceLayer paymentOrderDaoServiceLayer,
                              @Value("${PAYPAL_INTENT}") String INTENT,
                              @Value("${PAYPAL_CURRENCY}") String CURRENCY) {
        this.paymentProvider = paymentProvider;
        this.paymentOrderDaoServiceLayer = paymentOrderDaoServiceLayer;

        this.INTENT = INTENT;
        this.CURRENCY = CURRENCY;
    }

    public String getPaymentLink(Integer premiumMonthNumber, Double premiumPrice) throws IOException {
        String orderId = paymentProvider.createOrder(INTENT, CURRENCY, premiumPrice);

        paymentOrderDaoServiceLayer.addOrder(orderId, premiumMonthNumber, premiumPrice, new DateTime());

        return paymentProvider.getPaymentLink(orderId);
    }

    public Integer getPremiumMonthNumber(String orderId) throws IllegalDatabaseState, ObjectNotFoundException {
        PaymentOrder paymentOrder = paymentOrderDaoServiceLayer.getOrder(orderId);
        return paymentOrder.getPremiumMonthNumber();
    }

    public Double getPremiumPrice(String orderId) throws IllegalDatabaseState, ObjectNotFoundException {
        PaymentOrder paymentOrder = paymentOrderDaoServiceLayer.getOrder(orderId);
        return paymentOrder.getPremiumPrice();
    }

    public void approvePayment(String orderId) throws IllegalDatabaseState, ObjectNotFoundException, IOException {
        paymentOrderDaoServiceLayer.delete(orderId);

        paymentProvider.captureOrder(orderId);
    }
}
