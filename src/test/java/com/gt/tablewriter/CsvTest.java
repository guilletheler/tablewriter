package com.gt.tablewriter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CsvTest {

  static List<ColConfig<?>> colConfigs;

  @BeforeAll
  static void initializeAll() {
    colConfigs = new ArrayList<>();

    colConfigs.add(
      new ColConfig<String>("Nombre", String.class, 10, 18, null, null, null)
    );
    colConfigs.add(
      new ColConfig<Date>("Fecha", Date.class, null, null, null, null, null)
    );
    colConfigs.add(
      new ColConfig<Integer>("Numero", Integer.class, 10, 18, null, null, null)
    );
    colConfigs.add(
      new ColConfig<Double>("Importe", Double.class, 10, 18, null, null, null)
    );
    colConfigs.add(
      new ColConfig<Boolean>(
        "booleano",
        Boolean.class,
        10,
        18,
        null,
        null,
        null
      )
    );
  }

  @Test
  @DisplayName("Generar y escribir csv")
  void escribirData() {
    // genero 999 registros aleatorios
    List<Object[]> data = DataGen.buildData(colConfigs, 999);

    // le agrego valores nulos para ver si anda con nulos
    data.add(new Object[] { null, null, null, null, null });

    assertEquals(1000, data.size(), "El largo de los datos debería ser 1000");

    String template =
      "<html><div>CBU: {{cbu}}<div><table id=\"table-data\"></table></html>";

    template = template.replace("{{cbu}}", "65416846581496841654686844168");

    Properties props = new Properties();
    props.setProperty("DATE_FORMAT", "dd/MM/yyyy");
    props.setProperty("TEMPLATE", template);

    ITableWriter writer = new CsvTableWriter(props);
    // ITableWriter writer = new XlsxTableWriter(props);
    // ITableWriter writer = new PdfTableWriter(props);

    writer.writeTitles(
      colConfigs
        .stream()
        .map(cc -> cc.name)
        .collect(Collectors.toList())
        .toArray(new String[] {})
    );

    writer.addLines(data);

    assertDoesNotThrow(() -> {
      writer.writeTo("tmp.csv");
    });
  }

  @Test
  @DisplayName("Generar y escribir csv")
  void escribirPojoData() {
    // genero 999 registros aleatorios
    List<Pojo> data = DataGen.buildPojos(500);

    String template =
      "<html><div>CBU: {{cbu}}<div><table id=\"table-data\"></table></html>";

    template = template.replace("{{cbu}}", "65416846581496841654686844168");

    Properties props = new Properties();
    props.setProperty("DATE_FORMAT", "dd/MM/yyyy");
    props.setProperty("TEMPLATE", template);

    ITableWriter writer = new CsvTableWriter(props);
    // ITableWriter writer = new XlsxTableWriter(props);
    // ITableWriter writer = new PdfTableWriter(props);

    writer.writeTitles(
      new String[] { "Nombre", "Edad", "Activo", "Inicio", "Saldo" }
    );

    writer.addLines(
      data
        .stream()
        .map(pojo ->
          new Object[] {
            pojo.getNombre(),
            pojo.getEdad(),
            pojo.getActivo(),
            pojo.getInicio(),
            pojo.getSaldo(),
          }
        )
        .collect(Collectors.toList())
    );

    // try {
    //   ByteArrayOutputStream out = new ByteArrayOutputStream();
    //   writer.writeTo(out);
    //   out.toByteArray();
    // } catch (IOException e) {
    //   // TODO Auto-generated catch block
    //   e.printStackTrace();
    // }

    assertDoesNotThrow(() -> {
      writer.writeTo("tmpPojo.csv");
    });
  }

  @Test
  @DisplayName("Generar y escribir pdf")
  void escribirPojoDataPdf() {
    // genero 999 registros aleatorios
    List<Pojo> data = DataGen.buildPojos(500);

    Properties props = new Properties();
    props.setProperty("DATE_FORMAT", "dd/MM/yyyy");

    String tipo = "pdf";

    ITableWriter writer = null;

    switch (tipo) {
      case "pdf":
        String template =
          "<html><div>CBU: {{cbu}}<div><table id=\"table-data\"></table></html>";
        template = template.replace("{{cbu}}", "65416846581496841654686844168");
        props.setProperty("TEMPLATE", template);
        writer = new PdfTableWriter(props);
        break;
      case "csv":
        writer = new CsvTableWriter(props);
        break;
      case "xls":
        writer = new XlsxTableWriter(props);
        break;
    }

    writer.writeTitles(
      new String[] { "Nombre", "Edad", "Activo", "Inicio", "Saldo" }
    );

    writer.addLines(
      data
        .stream()
        .map(pojo ->
          new Object[] {
            pojo.getNombre(),
            pojo.getEdad(),
            pojo.getActivo(),
            pojo.getInicio(),
            pojo.getSaldo(),
          }
        )
        .collect(Collectors.toList())
    );

    // try {
    //   ByteArrayOutputStream out = new ByteArrayOutputStream();
    //   writer.writeTo(out);
    //   out.toByteArray();
    // } catch (IOException e) {
    //   // TODO Auto-generated catch block
    //   e.printStackTrace();
    // }

    ITableWriter tableWriter = writer;

    assertDoesNotThrow(() -> {
      tableWriter.writeTo("tmpPojo.pdf");
    });
  }
}
