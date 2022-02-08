package pl.edu.agh.transaction.premium.paymentProvider.paypalEnvironment;

import com.paypal.core.PayPalHttpClient;

public interface PayPalEnvironment {
    PayPalHttpClient getHttpClient();
}
