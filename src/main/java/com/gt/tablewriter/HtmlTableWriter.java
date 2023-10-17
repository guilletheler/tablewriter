package com.gt.tablewriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlTableWriter extends AbstractTableWriter {

  @Getter
  @Setter
  String template =
    "<!DOCTYPE html>\r\n" +
    "<html>\r\n" +
    "<head>\r\n" +
    "<style>\r\n" +
    "table {\r\n" +
    "  font-family: arial, sans-serif;\r\n" +
    "  border-collapse: collapse;\r\n" +
    "  width: 100%;\r\n" +
    "}\r\n" +
    "\r\n" +
    ".numeric-field {\r\n" +
    "  text-align: right;\r\n" +
    "}\r\n" +
    "\r\n" +
    ".date-field {\r\n" +
    "  text-align: left;\r\n" +
    "}\r\n" +
    "\r\n" +
    ".boolean-field {\r\n" +
    "  text-align: left;\r\n" +
    "}\r\n" +
    "\r\n" +
    ".string-field {\r\n" +
    "  text-align: left;\r\n" +
    "}\r\n" +
    "\r\n" +
    "td, th {\r\n" +
    "  border: 1px solid #dddddd;\r\n" +
    "  padding: 8px;\r\n" +
    "}\r\n" +
    "\r\n" +
    "tr:nth-child(even) {\r\n" +
    "  background-color: #dddddd;\r\n" +
    "}\r\n" +
    "</style>\r\n" +
    "</head>\r\n" +
    "<body>\r\n" +
    "\r\n" +
    "<table id=\"table-data\">\r\n" +
    "</table>\r\n" +
    "\r\n" +
    "</body>\r\n" +
    "</html>";

  @Getter
  @Setter
  DateFormat sdf = null;

  @Getter
  @Setter
  String numberCssClass = "numeric-field";

  @Getter
  @Setter
  String dateCssClass = "date-field";

  @Getter
  @Setter
  String booleanCssClass = "boolean-field";

  @Getter
  @Setter
  String stringCssClass = "string-field";

  @Getter
  @Setter
  DecimalFormat decf = null;

  @Getter
  @Setter
  DecimalFormat intf = null;

  OutputStream outputStream;

  protected Document doc;

  Element tableElement;

  Element curRow;

  public HtmlTableWriter() {
    super();
  }

  public HtmlTableWriter(Properties properties) {
    super(properties);
  }

  public HtmlTableWriter(Properties properties, OutputStream outputStream) {
    super(properties);
    this.outputStream = outputStream;
  }

  public HtmlTableWriter(OutputStream outputStream) {
    super();
    this.outputStream = outputStream;
  }

  public void open() {
    super.open();

    this.setTemplate(properties.getProperty("TEMPLATE", this.template));

    this.setNumberCssClass(
        properties.getProperty("NUMBER_CLASS", this.numberCssClass)
      );
    this.setDateCssClass(
        properties.getProperty("DATE_CLASS", this.dateCssClass)
      );
    this.setBooleanCssClass(
        properties.getProperty("BOOLEAN_CLASS", this.booleanCssClass)
      );
    this.setStringCssClass(
        properties.getProperty("STRING_CLASS", this.stringCssClass)
      );

    sdf = new SimpleDateFormat(this.getDateFormat());

    decf = new DecimalFormat(this.getDecimalFormat());
    intf = new DecimalFormat(this.getIntegerFormat());

    this.doc = Jsoup.parse(this.template);
    this.tableElement = this.doc.select("table#table-data").first();
  }

  public boolean isOpen() {
    return this.doc != null;
  }

  public void writeTitles(String[] titles) {
    if (!this.isOpen()) {
      this.open();
    }

    Element titlesElement = this.doc.createElement("tr");
    this.tableElement.appendChild(titlesElement);

    for (String title : titles) {
      Element titleElement = this.doc.createElement("th");
      titleElement.text(title);
      titlesElement.appendChild(titleElement);
    }
  }

  public void addNewLine() {
    this.curRow = this.doc.createElement("tr");
    this.tableElement.appendChild(curRow);
  }

  public void addField(Integer value) {
    if (value != null) {
      write(intf.format(value), this.numberCssClass);
    } else {
      write("", this.numberCssClass);
    }
  }

  public void addField(Long value) {
    if (value != null) {
      write(intf.format(value), this.numberCssClass);
    } else {
      write("", this.numberCssClass);
    }
  }

  public void addField(Double value) {
    if (value != null) {
      write(decf.format(value), this.numberCssClass);
    } else {
      write("", this.numberCssClass);
    }
  }

  public void addField(String value) {
    if (value != null) {
      write(value, stringCssClass);
    } else {
      write("", stringCssClass);
    }
  }

  public void addField(Date value) {
    if (value != null) {
      write(sdf.format(value), dateCssClass);
    } else {
      write("", dateCssClass);
    }
  }

  @Override
  public void addField(Boolean value) {
    if (value != null) {
      write((value ? "Si" : "No"), booleanCssClass);
    } else {
      write("", booleanCssClass);
    }
  }

  public void close() {}

  private void write(String formatedField, String cssClass) {
    Element newField = doc.createElement("td");

    Logger
      .getLogger(getClass().getName())
      .log(Level.INFO, "css class = " + cssClass);
    if (cssClass != null && !cssClass.isEmpty()) {
      newField.addClass(cssClass);
    }

    newField.text(formatedField);

    if (curRow == null) {
      this.addNewLine();
    }

    this.curRow.appendChild(newField);
  }

  public void writeTo(OutputStream outputStream) throws IOException {
    outputStream.write(doc.outerHtml().getBytes(StandardCharsets.UTF_8));
  }
}
