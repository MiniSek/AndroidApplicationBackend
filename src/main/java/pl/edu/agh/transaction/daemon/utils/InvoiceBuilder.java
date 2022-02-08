package pl.edu.agh.transaction.daemon.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.transaction.exception.InvoiceCreatingException;
import pl.edu.agh.transaction.invoice.invoiceModels.Invoice;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

@Component
public class InvoiceBuilder {
    private int invoiceNumber = 0;

    private final String INVOICE_AUTHOR;
    private final String INVOICE_TITLE_PREFIX;

    @Autowired
    public InvoiceBuilder(@Value("${INVOICE_AUTHOR}") String INVOICE_AUTHOR,
                          @Value("${INVOICE_TITLE_PREFIX}") String INVOICE_TITLE_PREFIX) {

        this.INVOICE_AUTHOR = INVOICE_AUTHOR;
        this.INVOICE_TITLE_PREFIX = INVOICE_TITLE_PREFIX;
    }

    public String createPdfInvoice(String clientNameAndSurname, List<Invoice> invoiceList)
            throws InvoiceCreatingException {

        DateTime todayDate = new DateTime();

        invoiceNumber++;
        String invoiceNumberStr = "";
        if(todayDate.getMonthOfYear()+1 < 10)
            invoiceNumberStr = invoiceNumber + "-" + "0" + todayDate.getMonthOfYear()+1 + "-" + todayDate.getYear();
        else
            invoiceNumberStr = invoiceNumber + "-" + todayDate.getMonthOfYear()+1 + "-" + todayDate.getYear();

        String filePath = System.getProperty("user.dir") + "\\data\\invoices\\" + invoiceNumberStr + ".pdf";
        String formattedInvoiceNumber = invoiceNumberStr.replace("-", "/");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            PdfPTable pdfPTable = new PdfPTable(2);
            document.add(pdfPTable);

            document.addTitle(INVOICE_TITLE_PREFIX + formattedInvoiceNumber);
            document.addAuthor(INVOICE_AUTHOR);

            Paragraph dateParagraph = new Paragraph("Wystawione dnia: " + new LocalDate());
            dateParagraph.setAlignment(Element.ALIGN_RIGHT);
            dateParagraph.setSpacingAfter(12);
            document.add(dateParagraph);

            Paragraph titleParagraph = new Paragraph(INVOICE_TITLE_PREFIX + formattedInvoiceNumber);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleParagraph.setSpacingAfter(18);
            document.add(titleParagraph);

            Paragraph clientParagraph = new Paragraph("Odbiorca:\n" + clientNameAndSurname);
            clientParagraph.setAlignment(Element.ALIGN_RIGHT);
            clientParagraph.setSpacingAfter(20);
            document.add(clientParagraph);

            PdfPTable table = new PdfPTable(3);
            table.setTotalWidth(500);
            float[] columnWidths = {50, 350, 100};
            table.setTotalWidth(columnWidths);
            table.setLockedWidth(true);

            table.addCell(new PdfPCell(new Phrase("LP")));
            table.addCell(new PdfPCell(new Phrase("Nazwa towaru/uslugi")));
            table.addCell(new PdfPCell(new Phrase("Wartosc brutto")));
            table.setHeaderRows(1);

            int lp = 1;
            for (Invoice invoice : invoiceList) {
                table.addCell(String.valueOf(lp));
                table.addCell("Konto PREMIUM na okres " +
                        invoice.getSubscriptionStartDate() + "-" + invoice.getSubscriptionEndDate());
                table.addCell(invoice.getPrice() + "zl");
                lp++;
            }
            document.add(table);

            Paragraph signatureParagraph = new Paragraph("Osoba upowazniona do wystawienia faktury:\nDr Rafal Mrowka");
            signatureParagraph.setAlignment(Element.ALIGN_RIGHT);
            signatureParagraph.setSpacingBefore(20);
            document.add(signatureParagraph);

            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            throw new InvoiceCreatingException(e);
        }

        return filePath;
    }

    public void clearInvoiceNumber() {
        this.invoiceNumber = 0;
    }
}
