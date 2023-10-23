package com.gt.tablewriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;

public class CsvTableWriter extends WithDataFormatTableWriter {

  @Getter
  @Setter
  String separator = ",";

  @Getter
  @Setter
  String EOL = "\n";

  boolean first = true;

  @Getter
  @Setter
  OutputStream internalStream = null;

  public CsvTableWriter() {
    super();
    this.prepare();
  }

  public CsvTableWriter(Properties properties) {
    super(properties);
    this.prepare();
  }

  public CsvTableWriter(Properties properties, OutputStream outputStream) {
    super(properties);
    this.internalStream = outputStream;
    this.prepare();
  }

  public CsvTableWriter(OutputStream outputStream) {
    super();
    this.internalStream = outputStream;
    this.prepare();
  }

  public void prepare() {
    super.prepare();

    this.setSeparator(properties.getProperty("SEPARATOR", this.getSeparator()));
    this.setEOL(properties.getProperty("EOL", this.getEOL()));

    if (internalStream == null) {
      internalStream = new ByteArrayOutputStream();
    }
  }

  public void addNewLine() {
    this.write(getEOL());
    first = true;
  }

  public void close() {
    try {
      internalStream.close();
    } catch (IOException e) {
      Logger
        .getLogger(HtmlTableWriter.class.getName())
        .log(Level.SEVERE, "Error al cerrar tablewriter", e);
    }
  }

  private void addSeparator() {
    if (!first) {
      write(separator);
    }
  }

  protected void internalAddField(String formatedField, Class<?> clazz) {
    boolean containSeparator = false;

    if (
      !formatedField.startsWith("\"", 0) &&
      formatedField.contains(this.separator)
    ) {
      containSeparator = true;
    }

    try {
      addSeparator();
      if (containSeparator) {
        write("\"");
      }
      write(formatedField);
      if (containSeparator) {
        write("\"");
      }
    } catch (Exception e) {
      Logger
        .getLogger(AbstractTableWriter.class.getName())
        .log(Level.SEVERE, "Error escribiendo en outputStream", e);
    }

    first = false;
  }

  private void write(String string) {
    try {
      internalStream.write(string.getBytes());
    } catch (Exception e) {
      Logger
        .getLogger(AbstractTableWriter.class.getName())
        .log(Level.SEVERE, "Error escribiendo en outputStream", e);
    }
  }

  public void writeTo(OutputStream outputStream) throws IOException {
    if (internalStream instanceof ByteArrayOutputStream) {
      outputStream.write(
        ((ByteArrayOutputStream) internalStream).toByteArray()
      );
    } else {
      throw new IllegalStateException(
        "internalStream no es ByteArrayOutputStream"
      );
    }
  }
}
