package com.gt.tablewriter;

import java.util.Properties;

public class TableWriterBuilder {
    public static ITableWriter buildTableWriter(TableWriterFormat format) {
        return buildTableWriter(format, (Properties) null);
    }

    public static ITableWriter buildTableWriter(TableWriterFormat format, Properties properties) {
        return switch (format) {
            case CSV -> new CsvTableWriter(properties);
            case HTML -> new HtmlTableWriter(properties);
            case PDF -> new PdfTableWriter(properties);
            default -> new XlsxTableWriter(properties);
        };
    }

    public static ITableWriter buildTableWriter(TableWriterFormat format, String htmlTemplate) {
        Properties prop = new Properties();
        prop.put(WithDataFormatTableWriter.PROPERTY_HTML_TEMPLATE, htmlTemplate);
        return buildTableWriter(format, prop);
    }

    public static ITableWriter buildTableWriter(TableWriterFormat format,
            Properties properties, String htmlTemplate) {

        properties.put(WithDataFormatTableWriter.PROPERTY_HTML_TEMPLATE, htmlTemplate);
        return buildTableWriter(format, properties);
    }
}
