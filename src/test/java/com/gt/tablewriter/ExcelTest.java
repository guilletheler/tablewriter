package com.gt.tablewriter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExcelTest {

  @Test
  @DisplayName("Generar y escribir xlsx")
  void xlsxTest() {
    ITableWriter writer = new XlsxTableWriter();

    TestHelper.writePlainData(writer);
    TestHelper.writeFieldsData(writer);

    assertDoesNotThrow(() -> {
      writer.writeTo("testOutput/tmp.xlsx");
    });
  }
}
