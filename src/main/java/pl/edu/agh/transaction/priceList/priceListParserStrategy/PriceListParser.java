package pl.edu.agh.transaction.priceList.priceListParserStrategy;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.List;

public interface PriceListParser {
    List<Triplet<String, Integer, Double>> parsePriceList();
}
