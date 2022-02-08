package pl.edu.agh.transaction.premium.paymentProvider.paypalEnvironment;

import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Qualifier("sandbox")
public class PayPalEnvironmentSandbox implements PayPalEnvironment {
    private final PayPalHttpClient httpClient;

    @Autowired
    public PayPalEnvironmentSandbox(@Value("${PAYPAL_SANDBOX_CLIENT_ID:}") String CLIENT_ID,
                                    @Value("${PAYPAL_SANDBOX_SECRET:}") String SECRET) {
        if(!CLIENT_ID.equals("") && !SECRET.equals("")) {
            com.paypal.core.PayPalEnvironment environment = new com.paypal.core.PayPalEnvironment.Sandbox(CLIENT_ID, SECRET);
            httpClient = new PayPalHttpClient(environment);
        } else
            httpClient = null;
    }

    public PayPalHttpClient getHttpClient() {
        return httpClient;
    }
}
