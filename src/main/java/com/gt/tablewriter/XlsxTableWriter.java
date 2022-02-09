package com.gt.tablewriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxTableWriter extends AbstractTableWriter {

	Workbook wb;
	Sheet sheet;
	Row row;

	int rows = 0;
	int cells = 0;

	CellStyle dateCellStyle;
	CellStyle integerCellStyle;
	CellStyle numericCellStyle;

	public void open() {

		super.open();

		if (getProperties().getProperty("EXCEL_FORMAT", "xlsx").toLowerCase().equals("xls")) {
			wb = new HSSFWorkbook();
		} else {
			wb = new XSSFWorkbook();
		}

		dateCellStyle = wb.createCellStyle();
		dateCellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(getDateFormat()));

		integerCellStyle = wb.createCellStyle();
		integerCellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(getIntegerFormat()));

		numericCellStyle = wb.createCellStyle();
		numericCellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(getDecimalFormat()));

		sheet = wb.createSheet(getProperties().getProperty("SHEET_NAME", "Hoja1"));
		addNewLine();
	}

	public void addNewLine() {
		row = sheet.createRow(rows++);
		cells = 0;
	}

	public void addField(Integer value) {
		Cell cell = row.createCell(cells++, CellType.NUMERIC);
		if (value != null) {
			cell.setCellValue(value.doubleValue());
		}
		cell.setCellStyle(integerCellStyle);
	}

	public void addField(Long value) {
		Cell cell = row.createCell(cells++, CellType.NUMERIC);
		if (value != null) {
			cell.setCellValue(value.doubleValue());
		}
		cell.setCellStyle(integerCellStyle);
	}

	public void addField(Double value) {
		Cell cell = row.createCell(cells++, CellType.NUMERIC);
		if (value != null) {
			cell.setCellValue(value.doubleValue());
		}
		cell.setCellStyle(numericCellStyle);
	}

	public void addField(String value) {
		Cell cell = row.createCell(cells++);
		if (value != null) {
			cell.setCellValue(value);
		}
	}

	public void addField(Date value) {
		Cell cell = row.createCell(cells++);
		if (value != null) {
			cell.setCellValue(value);
		}
		cell.setCellStyle(dateCellStyle);
	}

	public void addField(Boolean value) {
		Cell cell = row.createCell(cells++);
		if (value != null) {
			cell.setCellValue(value);
		}
	}

	public void writeTo(OutputStream outputStream) throws IOException {
		try {
			wb.write(outputStream);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(XlsxTableWriter.class.getName()).log(Level.SEVERE,
					"Error al escribir excel en outputStream", ex);
		}
	}

	public void close() {
		try {
			wb.close();
		} catch (IOException ex) {
			Logger.getLogger(XlsxTableWriter.class.getName()).log(Level.SEVERE, "error al cerrar excel", ex);
		}
	}

}
