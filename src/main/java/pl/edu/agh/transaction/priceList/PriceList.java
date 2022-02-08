package pl.edu.agh.transaction.priceList;

import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.edu.agh.transaction.exception.PriceListLoadException;
import pl.edu.agh.transaction.priceList.priceListParser.PriceListParser;

import java.util.List;

@Component
public class PriceList {
    private final List<Triplet<String, Integer, Double>> prices;

    @Autowired
    public PriceList(@Qualifier("txtParser") PriceListParser parser) {
        prices = parser.parsePriceList();
    }

    public List<Triplet<String, Integer, Double>> getPrices() throws PriceListLoadException {
        if(prices == null)
            throw new PriceListLoadException("Price list wasn't loaded");
        return prices;
    }

    public Double getPrice(String id) throws PriceListLoadException, IllegalArgumentException {
        if(prices == null)
            throw new PriceListLoadException("Price list wasn't loaded");
        for(Triplet<String, Integer, Double> triplet : prices)
            if(triplet.getValue0().equals(id))
                return triplet.getValue2();
        throw new IllegalArgumentException("Wrong id");
    }

    public Integer getMonthNumber(String id) throws PriceListLoadException, IllegalArgumentException {
        if(prices == null)
            throw new PriceListLoadException("Price list wasn't loaded");
        for(Triplet<String, Integer, Double> triplet : prices)
            if (triplet.getValue0().equals(id))
                return triplet.getValue1();
        throw new IllegalArgumentException("Wrong id");
    }
}
