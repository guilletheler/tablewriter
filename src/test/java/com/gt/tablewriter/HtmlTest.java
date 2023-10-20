package com.gt.tablewriter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HtmlTest {

  @Test
  @DisplayName("Generar y escribir html")
  void generarYescribirHtml() {
    ITableWriter writer = new HtmlTableWriter();

    TestHelper.writePlainData(writer);
    TestHelper.writeFieldsData(writer);

    assertDoesNotThrow(() -> {
      writer.writeTo("tmp.html");
    });
  }
}
