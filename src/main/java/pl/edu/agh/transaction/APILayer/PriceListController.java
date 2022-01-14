package pl.edu.agh.transaction.APILayer;

import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.transaction.ServiceLayer.PriceListService;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/pricelist")
public class PriceListController {
    private final PriceListService priceListService;

    @Autowired
    public PriceListController(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    @GetMapping
    public ResponseEntity<List<Triplet<String, Integer, Double>>> getPrices() {
        return priceListService.getPrices();
    }
}
