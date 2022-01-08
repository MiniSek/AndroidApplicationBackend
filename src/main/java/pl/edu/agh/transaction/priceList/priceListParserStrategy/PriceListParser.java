package pl.edu.agh.transaction.priceList.priceListParserStrategy;

import org.javatuples.Pair;

import java.util.List;

public interface PriceListParser {
    List<Pair<Integer, Double>> parsePriceList();
}
