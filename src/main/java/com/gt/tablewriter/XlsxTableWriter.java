package com.gt.tablewriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import lombok.Getter;

public class XlsxTableWriter extends AbstractTableWriter {

    public static final String PROPERTY_EXCEL_FORMAT = "EXCEL_FORMAT";
    public static final String PROPERTY_SHEET_NAME = "SHEET_NAME";

    @Getter
    Workbook wb;

    @Getter
    Sheet sheet;

    Row curRow;

    @Getter
    int nextRowNumber = 0;

    @Getter
    int nextCellNumber = 0;

    @Getter
    CellStyle dateCellStyle;

    @Getter
    CellStyle timeCellStyle;

    @Getter
    CellStyle dateTimeCellStyle;

    @Getter
    CellStyle integerCellStyle;

    @Getter
    CellStyle numericCellStyle;

    public XlsxTableWriter() {
        super();
        this.prepare();
    }

    public XlsxTableWriter(Properties properties) {
        super(properties);
        this.prepare();
    }

    @Override
    public void prepare() {
        super.prepare();

        if (getProperties()
                .getProperty(
                        PROPERTY_EXCEL_FORMAT, "xlsx")
                .equalsIgnoreCase("xls")) {
            wb = new HSSFWorkbook();
        } else {
            wb = new XSSFWorkbook();
        }

        dateCellStyle = wb.createCellStyle();
        dateCellStyle.setDataFormat(
                wb.getCreationHelper().createDataFormat().getFormat(getDateFormat()));
        dateCellStyle.setAlignment(HorizontalAlignment.LEFT);

        timeCellStyle = wb.createCellStyle();
        timeCellStyle.setDataFormat(
                wb.getCreationHelper().createDataFormat().getFormat(getTimeFormat()));
        timeCellStyle.setAlignment(HorizontalAlignment.LEFT);

        dateTimeCellStyle = wb.createCellStyle();
        dateTimeCellStyle.setDataFormat(
                wb.getCreationHelper().createDataFormat().getFormat(getDateTimeFormat()));
        dateTimeCellStyle.setAlignment(HorizontalAlignment.LEFT);

        integerCellStyle = wb.createCellStyle();
        integerCellStyle.setDataFormat(
                wb.getCreationHelper().createDataFormat().getFormat(getIntegerFormat()));

        numericCellStyle = wb.createCellStyle();
        numericCellStyle.setDataFormat(
                wb.getCreationHelper().createDataFormat().getFormat(getDecimalFormat()));

        sheet = wb.createSheet(getProperties().getProperty(PROPERTY_SHEET_NAME, "Hoja1"));
        addNewLine();
    }

    public boolean isOpen() {
        return wb != null;
    }

    @Override
    public void addNewLine() {
        curRow = sheet.createRow(nextRowNumber++);
        nextCellNumber = 0;
    }

    @Override
    public void addField(Long value) {
        Cell cell = createNewCell(CellType.NUMERIC);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
        cell.setCellStyle(integerCellStyle);
    }

    @Override
    public void addField(Double value) {
        Cell cell = createNewCell(CellType.NUMERIC);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
        cell.setCellStyle(numericCellStyle);
    }

    @Override
    public void addField(String value) {
        Cell cell = createNewCell();
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    @Override
    public void addField(Date value) {
        Cell cell = createNewCell();
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(dateTimeCellStyle);
    }

    @Override
    public void addField(Calendar value) {
        Cell cell = createNewCell();
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(dateTimeCellStyle);
    }

    @Override
    public void addField(LocalDate value) {
        Cell cell = createNewCell();
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(dateCellStyle);
    }

    @Override
    public void addField(LocalTime value) {
        Cell cell = createNewCell();
        if (value != null) {
            cell.setCellValue(value.atDate(LocalDate.now()));
        }
        cell.setCellStyle(timeCellStyle);
    }

    @Override
    public void addField(LocalDateTime value) {
        Cell cell = createNewCell();
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(dateTimeCellStyle);
    }

    @Override
    public void addField(ZonedDateTime value) {
        Cell cell = createNewCell();
        if (value != null) {
            cell.setCellValue(
                    Optional.ofNullable(value).map(zdt -> zdt.toLocalDateTime()).orElse(null));
        }
        cell.setCellStyle(dateTimeCellStyle);
    }

    @Override
    public void addField(Boolean value) {
        Cell cell = createNewCell(CellType.BOOLEAN);
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    private Cell createNewCell() {
        return createNewCell(null);
    }

    private Cell createNewCell(CellType cellType) {
        Cell cell;
        if (cellType != null) {
            cell = curRow.createCell(nextCellNumber, cellType);
        } else {
            cell = curRow.createCell(nextCellNumber);
        }

        nextCellNumber++;

        return cell;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        for (int x = 0; x <= sheet.getPhysicalNumberOfRows(); x++) {
            sheet.autoSizeColumn(x);
        }

        try {
            wb.write(outputStream);
        } catch (FileNotFoundException ex) {
            Logger
                    .getLogger(XlsxTableWriter.class.getName())
                    .log(Level.SEVERE, "Error al escribir excel en outputStream", ex);
        }
    }

    @Override
    public void close() {
        try {
            wb.close();
        } catch (IOException ex) {
            Logger
                    .getLogger(XlsxTableWriter.class.getName())
                    .log(Level.SEVERE, "error al cerrar excel", ex);
        }
    }

}
