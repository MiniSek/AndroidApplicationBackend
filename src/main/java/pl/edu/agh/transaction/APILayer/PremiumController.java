package pl.edu.agh.transaction.APILayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.transaction.ServiceLayer.premium.PremiumService;

@RestController
@RequestMapping(path="api/v1")
public class PremiumController {
    private final PremiumService premiumService;

    @Autowired
    public PremiumController(PremiumService premiumService) {
        this.premiumService = premiumService;
    }

    @PostMapping(value = "buy_premium")
    public ResponseEntity<String> buyPremium(@RequestParam("premiumId") String premiumId) {
        return premiumService.buyPremium(premiumId);
    }

    @PutMapping(value = "approve_payment")
    public ResponseEntity<String> approvePayment(@RequestParam("orderId") String orderId) {
        return premiumService.approvePayment(orderId);
    }
}
