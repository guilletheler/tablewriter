package com.gt.tablewriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class CsvTableWriter extends AbstractTableWriter {

	@Getter
	@Setter
	String separator = ",";

	@Getter
	@Setter
	String EOL = "\n";

	@Getter
	@Setter
	DateFormat sdf = null;

	@Getter
	@Setter
	DecimalFormat decf = null;

	@Getter
	@Setter
	DecimalFormat intf = null;

	boolean first = true;

	@Getter
	@Setter
	OutputStream internalStream = null;

	public CsvTableWriter(OutputStream outputStream) {
		this.internalStream = outputStream;
	}

	public void open() {

		super.open();

		if (internalStream == null) {
			internalStream = new ByteArrayOutputStream();
		}

		this.setSeparator(properties.getProperty("SEPARATOR", this.getSeparator()));
		this.setEOL(properties.getProperty("EOL", this.getEOL()));

		sdf = new SimpleDateFormat(this.getDateFormat());

		decf = new DecimalFormat(this.getDecimalFormat());
		intf = new DecimalFormat(this.getIntegerFormat());

	}

	public void addNewLine() {
		this.write(getEOL());
		first = true;
	}

	public void addField(Integer value) {
		addSeparator();
		if (value != null) {
			write(intf.format(value));
		}
		first = false;
	}

	public void addField(Long value) {
		addSeparator();
		if (value != null) {
			write(intf.format(value));
		}
		first = false;
	}

	public void addField(Double value) {
		addSeparator();
		if (value != null) {
			write(decf.format(value));
		}
		first = false;

	}

	public void addField(String value) {
		addSeparator();
		if (value != null) {
			write(value);
		}
		first = false;
	}

	public void addField(Date value) {
		addSeparator();
		if (value != null) {
			write(sdf.format(value));
		}
		first = false;
	}

	@Override
	public void addField(Boolean value) {
		addSeparator();
		if (value != null) {
			write(value ? "Si" : "No");
		}
		first = false;
	}

	public void close() {
		try {
			internalStream.close();
		} catch (IOException e) {
			Logger.getLogger(CsvTableWriter.class.getName()).log(Level.SEVERE, "Error al cerrar tablewriter", e);
		}
	}

	private void addSeparator() {
		if (!first) {
			write(separator);
		}
	}

	private void write(String string) {
		try {
			internalStream.write(string.getBytes());
		} catch (Exception e) {
			Logger.getLogger(AbstractTableWriter.class.getName()).log(Level.SEVERE, "Error escribiendo en outputStream",
					e);
		}
	}

	public void writeTo(OutputStream outputStream) throws IOException {
		if (outputStream instanceof ByteArrayOutputStream) {
			outputStream.write(((ByteArrayOutputStream) internalStream).toByteArray());
		} else {
			throw new IllegalStateException("internalStream no es ByteArrayOutputStream");
		}
	}
}
