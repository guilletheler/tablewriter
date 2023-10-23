package com.gt.tablewriter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PdfTest {

  @Test
  @DisplayName("Generar y escribir html")
  void generarYescribirHtml() {
    ITableWriter writer = new PdfTableWriter();

    TestHelper.writePlainData(writer);
    TestHelper.writeFieldsData(writer);

    assertDoesNotThrow(() -> {
      writer.writeTo("testOutput/tmp.pdf");
    });
  }
}
