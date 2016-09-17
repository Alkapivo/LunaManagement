import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class PDFCreator {

    public static void createPDF(TableView tablePurchaseView, String billName, String billDate, String labelSumNetto, String labelSumPrice, File pdfFile) {
        try {
            Document document = new Document(PageSize.A4,24f,24f, 36f, 36f);
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            int columnSize = tablePurchaseView.getColumns().size();
            int rowSize = tablePurchaseView.getItems().size();

            Paragraph pCell;
            PdfPCell cell;
            String cellData;

            PdfPTable table = new PdfPTable(columnSize);
            table.setWidths(new float[] { 5,18,5,9,9,9,9,9,9,9,9 });
            table.setWidthPercentage(100f);

            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font columnFont = new Font(helvetica, 8, Font.BOLD);
            Font cellFont = new Font(helvetica, 8, Font.NORMAL);
            Font titleFont = new Font(helvetica, 16, Font.NORMAL);
            Font dateFont = new Font(helvetica, 13, Font.NORMAL, BaseColor.GRAY);
            LineSeparator ls = new LineSeparator(0.5F,100f,BaseColor.DARK_GRAY,Element.ALIGN_BASELINE,0f);

            String[] columnNames = {"L.p.", "Nazwa produktu", "Ilość", "Jed. netto", "Jed. VAT [%]", "Netto", "Jed. brutto", "Jed. marża [%]", "Jed. cena", "Jed. cena r.", "Cena ręczna"};

            PdfPTable head = new PdfPTable(3);
            head.setWidthPercentage(100f);

            pCell = new Paragraph(billName, titleFont);
            pCell.setAlignment(Element.ALIGN_LEFT);
            cell = new PdfPCell();
            cell.setBorderWidth(0f);
            cell.addElement(pCell);
            head.addCell(cell);

            cell = new PdfPCell();
            cell.setBorderWidth(0f);
            head.addCell(cell);

            pCell = new Paragraph(billDate, dateFont);
            pCell.setAlignment(Element.ALIGN_RIGHT);
            cell = new PdfPCell();
            cell.setBorderWidth(0f);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.addElement(pCell);
            head.addCell(cell);

            head.setSpacingAfter(6f);
            document.add(head);

            document.add(new Chunk(ls));

            for (int i=0; i<columnSize; i++) {
                cellData = columnNames[i];
                pCell = new Paragraph(cellData, columnFont);
                cell = new PdfPCell();
                cell.setBackgroundColor(BaseColor.GRAY);
                pCell.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(pCell);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderWidth(0.5f);
                cell.setBorderColor(BaseColor.DARK_GRAY);

                table.addCell(cell);
            }

            java.util.List<TableColumn> list  = new ArrayList<>(tablePurchaseView.getColumns());
            for (int i=0; i<rowSize; i++) {
                for (int j=0; j<columnSize; j++) {
                    cellData = list.get(j).getCellData(i).toString();
                    pCell = new Paragraph(cellData, cellFont);
                    cell = new PdfPCell();

                    if((i%2)==0)
                        cell.setBackgroundColor(BaseColor.WHITE);
                    else
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

                    pCell.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(pCell);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setBorderWidth(0.25f);
                    cell.setBorderColor(BaseColor.DARK_GRAY);
                    table.addCell(cell);
                }
            }

            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            document.add(table);

            document.add(new Chunk(ls));

            PdfPTable footer = new PdfPTable(4);
            footer.setWidthPercentage(100f);

            pCell = new Paragraph("Suma netto: ", dateFont);
            pCell.setAlignment(Element.ALIGN_LEFT);
            cell = new PdfPCell();
            cell.setBorderWidth(0f);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.addElement(pCell);
            footer.addCell(cell);

            pCell = new Paragraph(labelSumNetto, titleFont);
            pCell.setAlignment(Element.ALIGN_LEFT);
            cell = new PdfPCell();
            cell.setBorderWidth(0f);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.addElement(pCell);
            footer.addCell(cell);

            pCell = new Paragraph("Suma całkowita: ", dateFont);
            pCell.setAlignment(Element.ALIGN_LEFT);
            cell = new PdfPCell();
            cell.setBorderWidth(0f);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.addElement(pCell);
            footer.addCell(cell);

            pCell = new Paragraph(labelSumPrice, titleFont);
            pCell.setAlignment(Element.ALIGN_LEFT);
            cell = new PdfPCell();
            cell.setBorderWidth(0f);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.addElement(pCell);
            footer.addCell(cell);

            footer.setSpacingAfter(10f);
            document.add(footer);

            document.close();
        }
        catch (DocumentException d) {
            System.out.println("0");
        }
        catch (IOException e) {
            System.out.println("1");
        }
    }

}
