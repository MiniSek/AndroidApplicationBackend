package pl.edu.agh.transaction.priceList.priceListParser;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Lazy
@Qualifier("xlsxParser")
public class PriceListParserXlsx implements PriceListParser {

    @Value("${PRICE_LIST_FILE_REL_PATH_XLSX:}")
    private String PRICE_LIST_FILE_REL_PATH;

    @Override
    public List<Triplet<String, Integer, Double>> parsePriceList() {
        Workbook workbook;
        try {
            String projectDirectoryPath = new File(".").getCanonicalPath();
            String fileLocation = projectDirectoryPath + PRICE_LIST_FILE_REL_PATH;
            FileInputStream file = new FileInputStream(fileLocation);
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        List<Triplet<String, Integer, Double>> prices = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);

        String id;
        int monthNumber, i = 0;
        double price;

        for(Row row : sheet) {
            if(i > 0) {
                if(row.getCell(0) != null && row.getCell(1) != null && row.getCell(2) != null) {
                    id = row.getCell(0).toString();
                    monthNumber = (int) Double.parseDouble(row.getCell(1).toString());
                    price = Double.parseDouble(row.getCell(2).toString());
                    prices.add(new Triplet<>(id, monthNumber, price));
                }
            }
            i++;
        }

        return prices;
    }
}