package pl.edu.agh.transaction.priceList.priceListParserStrategy;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javatuples.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PriceListParserXlsx implements PriceListParser {
    @Override
    public List<Pair<Integer, Double>> parsePriceList() {
        Workbook workbook;
        try {
            String projectDirectoryPath = new File(".").getCanonicalPath();
            String fileLocation = projectDirectoryPath + "\\data\\priceLists\\priceList.xlsx";
            FileInputStream file = new FileInputStream(fileLocation);
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        List<Pair<Integer, Double>> prices = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);

        int monthNumber = 0, i = 0, j;
        double price = 0;
        boolean hasContent;

        for (Row row : sheet) {
            if(i > 0) {
                j = 0;
                hasContent = false;
                for (Cell cell : row) {
                    if(!cell.toString().isEmpty()) {
                        hasContent = true;
                        if (j == 0)
                            monthNumber = (int) Double.parseDouble(cell.toString());
                        else if (j == 1)
                            price = Double.parseDouble(cell.toString());
                    }
                    j++;
                }
                if(hasContent)
                    prices.add(new Pair<>(monthNumber, price));
            }
            i++;
        }

        return prices;
    }
}
