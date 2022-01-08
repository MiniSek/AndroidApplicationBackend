package pl.edu.agh.transaction.premium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1")
public class BuyPremiumController {
    private final BuyPremiumService buyPremiumService;

    @Autowired
    public BuyPremiumController(BuyPremiumService buyPremiumService) {
        this.buyPremiumService = buyPremiumService;
    }

    @PostMapping(value = "buy_premium")
    public ResponseEntity<String> buyPremium(@RequestParam("monthNumber") String monthNumber) {
        return buyPremiumService.buyPremium(monthNumber);
    }

    @PutMapping(value = "approve_payment")
    public ResponseEntity<String> approvePayment(@RequestParam("orderID") String orderID) {
        return buyPremiumService.captureOrder(orderID);
    }
}
