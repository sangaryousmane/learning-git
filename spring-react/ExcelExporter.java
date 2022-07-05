package com.example.springreact.ems.exporters;

import com.example.springreact.ems.entities.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExcelExporter extends AbstractExporter {

    private final XSSFWorkbook xssfWorkbook;
    private XSSFSheet sheet;

    public ExcelExporter() {
        xssfWorkbook = new XSSFWorkbook();
    }

    private void writeHeadLine() {
        sheet = xssfWorkbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setBold(true);
        xssfFont.setFontHeight(18);
        cellStyle.setFont(xssfFont);

        // call the createCell() function
        createCell(row, 0, "USER ID", cellStyle);
        createCell(row, 1, "NAME", cellStyle);
        createCell(row, 2, "EMAIL", cellStyle);
        createCell(row, 3, "ROLE", cellStyle);
        createCell(row, 4, "ENABLED", cellStyle);
    }

    private void createCell(XSSFRow row, int colIndex,
                            Object value, CellStyle cellStyle) {
        XSSFCell cell = row.createCell(colIndex);
        sheet.autoSizeColumn(colIndex);

        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        cell.setCellStyle(cellStyle);
    }

    public void exporter(List<User> allUsers,
                       HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, ".xlsx", "application/octet-stream");
        writeHeadLine();
        writeDataLines(allUsers);

        ServletOutputStream outputStream = response.getOutputStream();
        xssfWorkbook.write(outputStream);
        xssfWorkbook.close();
        outputStream.close();
    }

    private void writeDataLines(List<User> allUsers) {
        int rowIndex = 1;
        XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for (User user : allUsers) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int colIndex = 0;
            createCell(row, colIndex++, user.getId(), cellStyle);
            createCell(row, colIndex++, user.getName(), cellStyle);
            createCell(row, colIndex++, user.getEmail(), cellStyle);
            createCell(row, colIndex++, user.getRoles().toString(), cellStyle);
            createCell(row, colIndex++, user.isEnabled(), cellStyle);
        }
    }
}
