package pl.edu.agh.transaction.premium.paypal;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class Credentials {
    static String clientId = "AULU5sCxqQT3IEKc2wEqY4MVt3TAnbx5BmbLgJFHVkDpf-OPVp3rA_SF0WIbKsvEZHXH4eiM2pdAuVWD";
    static String secret = "EIYAKw4f5S9VyX8F3sLMHlt39-TxNXdH1p7w36p6zl2VohuvB5KMJLOb2_eF6W_To_faq03grqrS9_TD";

    // Creating a sandbox environment
    private static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, secret);

    // Creating a client for the environment
    public static PayPalHttpClient client = new PayPalHttpClient(environment);
}
