package pl.edu.agh.transaction.ServiceLayer.priceListParserStrategy;

import org.javatuples.Triplet;

import java.util.List;

public interface PriceListParser {
    List<Triplet<String, Integer, Double>> parsePriceList();
}
