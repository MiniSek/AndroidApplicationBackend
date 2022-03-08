package pl.edu.agh.transaction.premium;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1")
public class PremiumController {
    private final PremiumService premiumService;

    @Autowired
    public PremiumController(PremiumService premiumService) {
        this.premiumService = premiumService;
    }

    @PostMapping(value = "buy_premium")
    public ResponseEntity<String> buyPremium(@RequestBody ObjectNode buyPremiumRequest) {
        String premiumId = buyPremiumRequest.get("premiumId").toString();
        return premiumService.buyPremium(premiumId.substring(1, premiumId.length()-1));
    }

    @PostMapping(value = "approve_payment")
    public ResponseEntity<String> approvePayment(@RequestBody ObjectNode approvePaymentRequest) {
        String orderId = approvePaymentRequest.get("orderId").toString();
        return premiumService.approvePayment(orderId.substring(1, orderId.length()-1));
    }
}