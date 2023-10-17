package com.gt.tablewriter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExcelTest {

  ITableWriter writer;

  OutputStream out;

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

  @BeforeEach
  void initialize() {
    // out = new ByteArrayOutputStream();
    writer = new XlsxTableWriter();

    writer.writeTitles(
      colConfigs
        .stream()
        .map(cc -> cc.name)
        .collect(Collectors.toList())
        .toArray(new String[] {})
    );
  }

  @Test
  @DisplayName("Generar y escribir xlsx")
  void addsTwoNumbers() {
    List<Object[]> data = DataGen.buildData(colConfigs, 999);
    data.add(new Object[] { null, null, null, null, null });

    assertEquals(1000, data.size(), "El largo de los datos debería ser 1000");

    this.writer.addLines(data);

    assertDoesNotThrow(() -> {
      this.writer.writeTo("tmp.xlsx");
    });
  }
  // @ParameterizedTest(name = "{0} + {1} = {2}")
  // @CsvSource({ "0,    1,   1", "1,    2,   3", "49,  51, 100", "1,  100, 101" })
  // void add(int first, int second, int expectedResult) {
  //   Calculator calculator = new Calculator();
  //   assertEquals(
  //     expectedResult,
  //     calculator.add(first, second),
  //     () -> first + " + " + second + " should equal " + expectedResult
  //   );
  // }
}
