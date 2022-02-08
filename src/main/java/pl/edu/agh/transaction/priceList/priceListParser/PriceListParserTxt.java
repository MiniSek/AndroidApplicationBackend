package pl.edu.agh.transaction.priceList.priceListParser;

import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
@Lazy
@Qualifier("txtParser")
public class PriceListParserTxt implements PriceListParser {

    @Value("${PRICE_LIST_FILE_REL_PATH_TXT:}")
    private String PRICE_LIST_FILE_REL_PATH;

    @Override
    public List<Triplet<String, Integer, Double>> parsePriceList() {
        Scanner sc;
        try {
            String projectDirectoryPath = new File(".").getCanonicalPath();
            File file = new File(projectDirectoryPath + PRICE_LIST_FILE_REL_PATH);
            sc = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        List<Triplet<String, Integer, Double>> prices = new ArrayList<>();
        String [] words;

        while(sc.hasNextLine()) {
            words = sc.nextLine().split(" ");

            if(words.length < 3)
                return null;
            prices.add(new Triplet<>(words[0], Integer.parseInt(words[1]), Double.parseDouble(words[2])));
        }

        return prices;
    }
}
