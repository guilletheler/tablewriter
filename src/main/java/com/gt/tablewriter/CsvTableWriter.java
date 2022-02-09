package com.gt.tablewriter;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;

public class CsvTableWriter implements ITableWriter {

	@Getter
	@Setter
	String fileName = "";

	@Getter
	@Setter
	PrintStream printStream;

	@Getter
	@Setter
	String separator = ",";

	@Getter
	@Setter
	String dateFormat = "yyyy/MM/dd HH:mm:ss";

	@Getter
	@Setter
	String decimalFormat = "0.00";

	@Getter
	@Setter
	String integerFormat = "0";

	SimpleDateFormat sdf = null;
	DecimalFormat decf = null;
	DecimalFormat intf = null;

	boolean first = true;

	public void open() {
		if (printStream == null) {
			try {
				printStream = new PrintStream(fileName);
			} catch (FileNotFoundException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Error abriendo archivo " + fileName + " para escritura");
			}
		}
		sdf = new SimpleDateFormat(dateFormat);
		decf = new DecimalFormat(decimalFormat);
		intf = new DecimalFormat(integerFormat);
	}

	public void addRecord() {
		printStream.println();
		first = true;
	}

	public void addField(Integer value) {
		addSeparator();
		if (value != null) {
			printStream.print(intf.format(value));
		}
		first = false;
	}

	public void addField(Long value) {
		addSeparator();
		if (value != null) {
			printStream.print(intf.format(value));
		}
		first = false;
	}

	public void addField(Double value) {
		addSeparator();
		if (value != null) {
			printStream.print(decf.format(value));
		}
		first = false;

	}

	public void addField(String value) {
		addSeparator();
		if (value != null) {
			printStream.print(value);
		}
		first = false;
	}

	public void addField(Date value) {
		addSeparator();
		if (value != null) {
			printStream.print(sdf.format(value));
		}
		first = false;
	}

	@Override
	public void addField(Boolean value) {
		addSeparator();
		if (value != null) {
			printStream.print(value ? "Si" : "No");
		}
		first = false;
	}

	public void close() {
		printStream.close();
	}

	private void addSeparator() {
		if (!first) {
			printStream.print(separator);
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
