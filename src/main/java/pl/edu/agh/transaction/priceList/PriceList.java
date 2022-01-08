package pl.edu.agh.transaction.priceList;

import org.javatuples.Pair;
import org.springframework.stereotype.Component;
import pl.edu.agh.transaction.exception.PriceListLoadException;
import pl.edu.agh.transaction.priceList.priceListParserStrategy.PriceListParser;
import pl.edu.agh.transaction.priceList.priceListParserStrategy.PriceListParserXlsx;

import java.util.List;

@Component
public class PriceList {
    private final List<Pair<Integer, Double>> prices;

    public PriceList() {
        PriceListParser priceListParser = new PriceListParserXlsx();
        prices = priceListParser.parsePriceList();
    }

    public List<Pair<Integer, Double>> getPrices() {
        return prices;
    }

    public Double getPriceForMonthNumber(int monthNumber) throws PriceListLoadException, IllegalArgumentException {
        if(prices == null)
            throw new PriceListLoadException("Price list wasn't loaded");
        for(Pair<Integer, Double> pair : prices)
            if(pair.getValue0() == monthNumber)
                return pair.getValue1();
        throw new IllegalArgumentException("Wrong month number");
    }
}
