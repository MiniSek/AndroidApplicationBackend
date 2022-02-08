package pl.edu.agh.transaction.premium.paymentProvider.paypalEnvironment;

import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Qualifier("live")
public class PayPalEnvironmentLive implements PayPalEnvironment {
    private final PayPalHttpClient httpClient;

    @Autowired
    public PayPalEnvironmentLive(@Value("${PAYPAL_BUSINESS_CLIENT_ID:}") String CLIENT_ID,
                                 @Value("${PAYPAL_BUSINESS_SECRET:}") String SECRET) {
        if(!CLIENT_ID.equals("") && !SECRET.equals("")) {
            com.paypal.core.PayPalEnvironment environment = new com.paypal.core.PayPalEnvironment.Live(CLIENT_ID, SECRET);
            httpClient = new PayPalHttpClient(environment);
        } else
            httpClient = null;
    }

    public PayPalHttpClient getHttpClient() {
        return httpClient;
    }
}
