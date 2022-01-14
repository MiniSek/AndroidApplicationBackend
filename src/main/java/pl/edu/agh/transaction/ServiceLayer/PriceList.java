package pl.edu.agh.transaction.ServiceLayer;

import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.transaction.Utils.ServerException.PriceListLoadException;
import pl.edu.agh.transaction.ServiceLayer.priceListParserStrategy.PriceListParser;
import pl.edu.agh.transaction.ServiceLayer.priceListParserStrategy.PriceListParserXlsx;

import java.util.List;

@Component
public class PriceList {
    private final List<Triplet<String, Integer, Double>> prices;

    @Autowired
    public PriceList(@Value("${PRICE_LIST_FILE_REL_PATH}") String PRICE_LIST_FILE_REL_PATH) {
        PriceListParser priceListParser = new PriceListParserXlsx(PRICE_LIST_FILE_REL_PATH);
        prices = priceListParser.parsePriceList();
    }

    public List<Triplet<String, Integer, Double>> getPrices() throws PriceListLoadException {
        if(prices == null)
            throw new PriceListLoadException("Price list wasn't loaded");
        return prices;
    }

    public Double getPrice(String id) throws PriceListLoadException, IllegalArgumentException {
        if(prices == null)
            throw new PriceListLoadException("Price list wasn't loaded");
        for(Triplet<String, Integer, Double> pair : prices)
            if(pair.getValue0().equals(id))
                return pair.getValue2();
        throw new IllegalArgumentException("Wrong id");
    }

    public Integer getMonthNumber(String id) throws PriceListLoadException, IllegalArgumentException {
        if(prices == null)
            throw new PriceListLoadException("Price list wasn't loaded");
        for(Triplet<String, Integer, Double> pair : prices)
            if(pair.getValue0().equals(id))
                return pair.getValue1();
        throw new IllegalArgumentException("Wrong id");
    }
}
