package com.shopme.admin.user.export;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExport extends AbstractExporter {


    public void export(
            List<User> userList,
            HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf", ".pdf", "users_");

        Document document=new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph=new Paragraph("List Of Users", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        float[] widths={2.4F, 4.5F, 3.0F, 3.0F, 3.0F, 1.5F};
        PdfPTable pdfPTable=new PdfPTable(6);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setSpacingBefore(20);
        pdfPTable.setWidths(widths);
        writeTableHeader(pdfPTable);
        writeTableData(pdfPTable, userList);
        document.add(pdfPTable);
        document.close();
    }

    private void writeTableData(
            PdfPTable pdfPTable,
            List<User> userList) {

        for (User user: userList){
            pdfPTable.addCell(String.valueOf(user.getId()));
            pdfPTable.addCell(user.getEmail());
            pdfPTable.addCell(user.getFirstName());
            pdfPTable.addCell(user.getLastName());
            pdfPTable.addCell(user.getRoles().toString());
            pdfPTable.addCell(String.valueOf(user.isEnabled()));
        }
    }

    private void writeTableHeader(PdfPTable pdfPTable) {
        PdfPCell cell=new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font=FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("USER ID", font));
        pdfPTable.addCell(cell);

        cell.setPhrase(new Phrase("EMAIL", font));
        pdfPTable.addCell(cell);

        cell.setPhrase(new Phrase("FIRST NAME", font));
        pdfPTable.addCell(cell);

        cell.setPhrase(new Phrase("LAST NAME", font));
        pdfPTable.addCell(cell);

        cell.setPhrase(new Phrase("ROLES", font));
        pdfPTable.addCell(cell);

        cell.setPhrase(new Phrase("ENABLED", font));
        pdfPTable.addCell(cell);
    }
}
