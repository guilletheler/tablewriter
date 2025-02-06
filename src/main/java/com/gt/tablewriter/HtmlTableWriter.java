package com.gt.tablewriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import lombok.Getter;
import lombok.Setter;

/**
 * Genera un HTML con la tabla insertada
 */
public class HtmlTableWriter extends WithDataFormatTableWriter {

    public static final String PROPERTY_NUMBER_CLASS = "NUMBER_CLASS";
    public static final String PROPERTY_TABLE_DATA_ID = "TABLE_DATA_ID";
    public static final String PROPERTY_DATE_CLASS = "DATE_CLASS";
    public static final String PROPERTY_BOOLEAN_CLASS = "BOOLEAN_CLASS";
    public static final String PROPERTY_STRING_CLASS = "STRING_CLASS";

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

    @Override
    public void prepare() {
        super.prepare();

        if (htmlTemplate == null) {
            this.setHtmlTemplate(getProperties().getProperty(PROPERTY_HTML_TEMPLATE, null));
        }

        if (htmlTemplate == null) {
            this.setHtmlTemplate(loadTemplateFromResources());
        }

        this.doc = Jsoup.parse(this.htmlTemplate);

        this.setTableDataId(
                getProperties().getProperty(PROPERTY_TABLE_DATA_ID, this.tableDataId));

        this.setNumberCssClass(
                getProperties().getProperty(PROPERTY_NUMBER_CLASS, this.numberCssClass));

        this.setDateCssClass(
                getProperties().getProperty(PROPERTY_DATE_CLASS, this.dateCssClass));

        this.setBooleanCssClass(
                getProperties().getProperty(PROPERTY_BOOLEAN_CLASS, this.booleanCssClass));

        this.setStringCssClass(
                getProperties().getProperty(PROPERTY_STRING_CLASS, this.stringCssClass));
    }

    protected String loadTemplateFromResources() {
        InputStream inputStream = getClass().getResourceAsStream("/template.html");
        InputStreamReader streamReader = new InputStreamReader(
                inputStream,
                StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public void setProperty(String key, String value) {
        super.setProperty(key, value);
        if (key.equals(PROPERTY_TABLE_DATA_ID)) {
            this.setTableDataId(value);
        }
    }

    public void setTableDataId(String tableDataId) {
        this.tableDataId = tableDataId;
        if (this.doc == null) {
            throw new RuntimeException("El documento html es nulo");
        }

        var elements = this.doc.select("table#" + this.tableDataId);
        if (elements != null && !elements.isEmpty()) {
            this.tableElement = elements.first();
        } else {
            throw new IllegalArgumentException("id de tabla incorrecto");
        }
    }

    public boolean isOpen() {
        return this.doc != null;
    }

    @Override
    public void writeTitles(String[] titles) {
        Element titlesElement = this.doc.createElement("tr");
        this.tableElement.appendChild(titlesElement);

        for (String title : titles) {
            Element titleElement = this.doc.createElement("th");
            titleElement.text(title);
            titlesElement.appendChild(titleElement);
        }
    }

    @Override
    public void addNewLine() {
        this.curRow = this.doc.createElement("tr");
        this.tableElement.appendChild(curRow);
    }

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
            if (clazz == Calendar.class
                    || clazz == Date.class
                    || clazz == LocalDate.class
                    || clazz == LocalTime.class
                    || clazz == ZonedDateTime.class) {
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
