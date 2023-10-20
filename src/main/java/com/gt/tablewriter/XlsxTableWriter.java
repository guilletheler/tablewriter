package com.gt.tablewriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxTableWriter extends AbstractTableWriter {

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

  public void prepare() {
    super.prepare();

    if (
      getProperties()
        .getProperty("EXCEL_FORMAT", "xlsx")
        .equalsIgnoreCase("xls")
    ) {
      wb = new HSSFWorkbook();
    } else {
      wb = new XSSFWorkbook();
    }

    dateCellStyle = wb.createCellStyle();
    dateCellStyle.setDataFormat(
      wb.getCreationHelper().createDataFormat().getFormat(getDateFormat())
    );
    dateCellStyle.setAlignment(HorizontalAlignment.LEFT);

    integerCellStyle = wb.createCellStyle();
    integerCellStyle.setDataFormat(
      wb.getCreationHelper().createDataFormat().getFormat(getIntegerFormat())
    );

    numericCellStyle = wb.createCellStyle();
    numericCellStyle.setDataFormat(
      wb.getCreationHelper().createDataFormat().getFormat(getDecimalFormat())
    );

    sheet = wb.createSheet(getProperties().getProperty("SHEET_NAME", "Hoja1"));
    addNewLine();
  }

  public boolean isOpen() {
    return wb != null;
  }

  public void addNewLine() {
    curRow = sheet.createRow(nextRowNumber++);
    nextCellNumber = 0;
  }

  public void addField(Integer value) {
    Cell cell = createNewCell(CellType.NUMERIC);
    if (value != null) {
      cell.setCellValue(value.doubleValue());
    }
    cell.setCellStyle(integerCellStyle);
  }

  public void addField(Long value) {
    Cell cell = createNewCell(CellType.NUMERIC);
    if (value != null) {
      cell.setCellValue(value.doubleValue());
    }
    cell.setCellStyle(integerCellStyle);
  }

  public void addField(Double value) {
    Cell cell = createNewCell(CellType.NUMERIC);
    if (value != null) {
      cell.setCellValue(value.doubleValue());
    }
    cell.setCellStyle(numericCellStyle);
  }

  public void addField(String value) {
    Cell cell = createNewCell();
    if (value != null) {
      cell.setCellValue(value);
    }
  }

  public void addField(Date value) {
    Cell cell = createNewCell();
    if (value != null) {
      cell.setCellValue(value);
    }
    cell.setCellStyle(dateCellStyle);
  }

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
