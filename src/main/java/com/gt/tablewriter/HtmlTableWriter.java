package com.gt.tablewriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlTableWriter extends WithDataFormatTableWriter {

  @Getter
  @Setter
  String htmlTemplate;

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
  String tableDataId = "table-data";

  OutputStream outputStream;

  protected Document doc;

  Element tableElement;

  Element curRow;

  public HtmlTableWriter() {
    super();
    this.prepare();
  }

  public HtmlTableWriter(Properties properties) {
    super(properties);
    this.prepare();
  }

  public HtmlTableWriter(Properties properties, OutputStream outputStream) {
    super(properties);
    this.outputStream = outputStream;
    this.prepare();
  }

  public HtmlTableWriter(OutputStream outputStream) {
    super();
    this.outputStream = outputStream;
    this.prepare();
  }

  public void prepare() {
    super.prepare();

    if (htmlTemplate == null) {
      this.setHtmlTemplate(properties.getProperty("HTML_TEMPLATE", null));
    }

    if (htmlTemplate == null) {
      this.setHtmlTemplate(loadTemplateFromResources());
    }

    this.doc = Jsoup.parse(this.htmlTemplate);

    this.setTableDataId(
        properties.getProperty("TABLE_DATA_ID", this.tableDataId)
      );

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
  }

  protected String loadTemplateFromResources() {
    InputStream inputStream = getClass().getResourceAsStream("/template.html");
    InputStreamReader streamReader = new InputStreamReader(
      inputStream,
      StandardCharsets.UTF_8
    );
    BufferedReader reader = new BufferedReader(streamReader);
    return reader.lines().collect(Collectors.joining("\n"));
  }

  public void setTableDataId(String tableDataId) {
    this.tableDataId = tableDataId;
    if (this.doc != null) {
      this.tableElement = this.doc.select("table#" + this.tableDataId).first();
    }
  }

  public boolean isOpen() {
    return this.doc != null;
  }

  public void writeTitles(String[] titles) {
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

  public void close() {}

  @Override
  protected void internalAddField(String formatedField, Class<?> clazz) {
    Element newField = doc.createElement("td");

    String cssClass = getCssClass(clazz);

    if (cssClass != null && !cssClass.isEmpty()) {
      newField.addClass(cssClass);
    }

    newField.text(formatedField);

    if (curRow == null) {
      this.addNewLine();
    }

    this.curRow.appendChild(newField);
  }

  private String getCssClass(Class<?> clazz) {
    if (clazz != null) {
      if (Number.class.isAssignableFrom(clazz)) {
        return numberCssClass;
      }
      if (clazz == Calendar.class || clazz == Date.class) {
        return dateCssClass;
      }
      if (clazz == Boolean.class || clazz == boolean.class) {
        return booleanCssClass;
      }
    }
    return null;
  }

  public void writeTo(OutputStream outputStream) throws IOException {
    outputStream.write(doc.outerHtml().getBytes(StandardCharsets.UTF_8));
  }
}
