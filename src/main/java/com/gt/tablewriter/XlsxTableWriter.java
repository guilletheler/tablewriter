package com.gt.tablewriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import lombok.Getter;
import lombok.Setter;

public class XlsxTableWriter implements ITableWriter {

	@Getter
	@Setter
	String fileName = "";

	Workbook wb;
	Sheet sheet;
	Row row;

	int rows = 0;
	int cells = 0;
	CellStyle dateCellStyle;
	CellStyle integerCellStyle;
	CellStyle numericCellStyle;

	public void open() {

		if (fileName.toLowerCase().endsWith(".xls")) {
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		}

		dateCellStyle = wb.createCellStyle();
		dateCellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("d/m/yy h:mm"));

		integerCellStyle = wb.createCellStyle();
		integerCellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("#,##0"));

		numericCellStyle = wb.createCellStyle();
		numericCellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("#,##0.00"));

		sheet = wb.createSheet("Hoja1");
		addRecord();
	}

	public void addRecord() {
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

	public void close() {
		try (OutputStream fileOut = new FileOutputStream(fileName)) {
			wb.write(fileOut);
		} catch (FileNotFoundException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al guardar xlsx", e);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al guardar xlsx", e);
		}
	}

	public void addRecord(Integer value) {
		addField(value);
		addRecord();
	}

	@Override
	public void addRecord(Long value) {
		addField(value);
		addRecord();
	}

	@Override
	public void addRecord(Double value) {
		addField(value);
		addRecord();
	}

	@Override
	public void addRecord(String value) {
		addField(value);
		addRecord();
	}

	@Override
	public void addRecord(Date value) {
		addField(value);
		addRecord();
	}

	@Override
	public void addRecord(Boolean value) {
		addField(value);
		addRecord();
	}

}
