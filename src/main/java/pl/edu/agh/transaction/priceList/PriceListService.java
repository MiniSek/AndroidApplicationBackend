package pl.edu.agh.transaction.priceList;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceListService {
    private final PriceList priceList;

    @Autowired
    public PriceListService(PriceList priceList) {
        this.priceList = priceList;
    }

    public ResponseEntity<List<Pair<Integer, Double>>> getPrices() {
        List<Pair<Integer, Double>> prices = priceList.getPrices();
        HttpStatus httpStatus;
        if (prices == null)
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        else
            httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(prices,httpStatus);
    }
}
