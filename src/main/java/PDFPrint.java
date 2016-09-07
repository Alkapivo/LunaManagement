import org.apache.pdfbox.pdmodel.PDDocument;
import javax.print.PrintService;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;

public class PDFPrint {
    public static PrintService choosePrinter() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if(printJob.printDialog()) {
            return printJob.getPrintService();
        }
        else {
            return null;
        }
    }

    public static void printPDF(String fileName, PrintService printer) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(printer);
            PDDocument doc = PDDocument.load(fileName);
            doc.silentPrint(job);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (PrinterException e) {
            e.printStackTrace();
        }
    }
}
