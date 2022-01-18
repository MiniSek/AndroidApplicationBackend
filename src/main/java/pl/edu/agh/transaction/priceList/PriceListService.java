package pl.edu.agh.transaction.priceList;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.exception.PriceListLoadException;

import java.util.List;

@Service
public class PriceListService {
    private final PriceList priceList;

    @Autowired
    public PriceListService(PriceList priceList) {
        this.priceList = priceList;
    }

    public ResponseEntity<List<Triplet<String, Integer, Double>>> getPrices() {
        try {
            return new ResponseEntity<>(priceList.getPrices(), HttpStatus.OK);
        } catch(PriceListLoadException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
