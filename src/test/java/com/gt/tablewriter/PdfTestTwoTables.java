package com.gt.tablewriter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PdfTestTwoTables {

  static List<ColConfig<?>> colConfigs;

  @BeforeAll
  static void initializeAll() {
    colConfigs = new ArrayList<>();

    colConfigs.add(
        new ColConfig<String>("Nombre", String.class, 10, 18, null, null, null));
    colConfigs.add(
        new ColConfig<Date>("Fecha", Date.class, null, null, null, null, null));
    colConfigs.add(
        new ColConfig<Integer>("Numero", Integer.class, 10, 18, null, null, null));
    colConfigs.add(
        new ColConfig<Double>("Importe", Double.class, 10, 18, null, null, null));
    colConfigs.add(
        new ColConfig<Boolean>(
            "booleano",
            Boolean.class,
            10,
            18,
            null,
            null,
            null));
  }

  @Test
  @DisplayName("Generar y escribir pdf two tables")
  void generarYescribirPdf2tables() {

    // genero 999 registros aleatorios
    List<Object[]> data = TestHelper.buildData(colConfigs, 19);

    // le agrego valores nulos para ver si anda con nulos
    data.add(new Object[] {null, null, null, null, null});

    assertEquals(20, data.size(), "El largo de los datos debería ser 20");

    String template = """
        <html>
          <head>
            <style>
              table {
                font-family: arial, sans-serif;
                border-collapse: collapse;
                width: 100%;
              }

              .numeric-field {
                text-align: right;
              }

              .date-field {
                text-align: left;
              }

              .boolean-field {
                text-align: left;
              }

              .string-field {
                text-align: left;
              }

              td,
              th {
                border: 1px solid #dddddd;
                padding: 8px;
              }

              tr:nth-child(even) {
                background-color: #dddddd;
              }
            </style>
          </head>
          <body>
            <div>CBU: {{cbu}}</div>
            <table id=\"table-data\"></table>
            <div>Acá va otra tabla</div>
            <table id=\"table-data1\"></table>
          </body>
        </html>
          """;

    template = template.replace("{{cbu}}", "65416846581496841654686844168");

    Properties props = new Properties();
    props.setProperty(PdfTableWriter.PROPERTY_DATE_FORMAT, "dd/MM/yyyy");
    props.setProperty(PdfTableWriter.PROPERTY_HTML_TEMPLATE, template);

    ITableWriter writer =
        (PdfTableWriter) TableWriterBuilder.buildTableWriter(TableWriterFormat.PDF, props);

    TestHelper.writePlainData(writer);
    TestHelper.writeFieldsData(writer);

    writer.setProperty(HtmlTableWriter.PROPERTY_TABLE_DATA_ID, "table-data1");

    TestHelper.writePlainData(writer);
    TestHelper.writeFieldsData(writer);

    assertDoesNotThrow(() -> {
      writer.writeTo("testOutput/tmp2t.pdf");
    });
  }
}
