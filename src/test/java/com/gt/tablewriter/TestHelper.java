package com.gt.tablewriter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class TestHelper {

  public static List<Pojo> buildPojos(int cant) {
    List<Pojo> ret = new ArrayList<>();

    ColConfig<String> nombre = new ColConfig<String>(
      "Nombre",
      String.class,
      10,
      18,
      null,
      null,
      null
    );
    ColConfig<Integer> edad = new ColConfig<Integer>(
      "Edad",
      Integer.class,
      null,
      null,
      18,
      80,
      null
    );
    ColConfig<Boolean> activo = new ColConfig<Boolean>(
      "Activo",
      Boolean.class,
      null,
      null,
      null,
      null,
      null
    );
    ColConfig<Date> inicio = new ColConfig<Date>(
      "Inicio",
      Date.class,
      null,
      null,
      null,
      null,
      null
    );
    ColConfig<Double> saldo = new ColConfig<Double>(
      "Saldo",
      Double.class,
      null,
      null,
      null,
      null,
      null
    );

    for (int i = 0; i < cant; i++) {
      ret.add(
        Pojo
          .builder()
          .nombre(buildString(nombre))
          .activo(buildBoolean(activo))
          .activo1(buildBoolean(activo))
          .edad(buildInt(edad))
          .edad1(buildInt(edad))
          .inicio(buildDate(inicio))
          .saldo(buildDouble(saldo))
          .saldo1(buildDouble(saldo))
          .shortValue(buildInt(edad).shortValue())
          .shortValue1(buildInt(edad).shortValue())
          .longValue(buildInt(edad).longValue())
          .longValue1(buildInt(edad).longValue())
          .build()
      );
    }

    return ret;
  }

  public static List<Object[]> buildData(
    List<ColConfig<?>> colDataType,
    int rows
  ) {
    List<Object[]> ret = new ArrayList<>();

    for (int i = 0; i < rows; i++) {
      Object[] row = buildRow(colDataType);
      ret.add(row);
    }

    return ret;
  }

  public static Object[] buildRow(List<ColConfig<?>> colConfigs) {
    return colConfigs
      .stream()
      .map(cc -> buildRandom(cc))
      .collect(Collectors.toList())
      .toArray(new Object[colConfigs.size()]);
  }

  @SuppressWarnings("unchecked")
  public static <T> T buildRandom(ColConfig<T> colConfig) {
    if (colConfig.getDefaultValue() != null) {
      return colConfig.getDefaultValue();
    }

    T ret = null;
    switch (colConfig.getClazz().getName()) {
      case "java.lang.Boolean":
        ret = (T) buildBoolean((ColConfig<Boolean>) colConfig);
        break;
      case "java.lang.String":
        ret = (T) buildString((ColConfig<String>) colConfig);
        break;
      case "java.util.Date":
        ret = (T) buildDate((ColConfig<Date>) colConfig);
        break;
      case "java.lang.Integer":
        ret = (T) buildInt((ColConfig<Integer>) colConfig);
        break;
      case "java.lang.Double":
        ret = (T) buildDouble((ColConfig<Double>) colConfig);
        break;
      default:
    }

    return ret;
  }

  public static String buildString(ColConfig<String> colConfig) {
    if (Optional.ofNullable(colConfig.getMaxLength()).orElse(0) < 1) {
      colConfig.setMaxLength(255);
    }

    if (Optional.ofNullable(colConfig.getMinLength()).orElse(0) < 1) {
      colConfig.setMinLength(colConfig.getMaxLength());
    }

    Integer delta = colConfig.getMaxLength() - colConfig.getMinLength();

    Integer len = colConfig.getMinLength() + new Random().nextInt(delta);

    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    Random random = new Random();

    return random
      .ints(leftLimit, rightLimit + 1)
      .limit(len)
      .collect(
        StringBuilder::new,
        StringBuilder::appendCodePoint,
        StringBuilder::append
      )
      .toString();
  }

  public static Date buildDate(ColConfig<Date> colConfig) {
    Date now = new Date();

    colConfig.setMaxValue(
      Optional.ofNullable(colConfig.getMaxValue()).orElse(now)
    );

    Calendar minCal = Calendar.getInstance();
    minCal.add(Calendar.YEAR, -10);

    colConfig.setMinValue(
      Optional.ofNullable(colConfig.getMinValue()).orElse(minCal.getTime())
    );

    Long delta =
      (colConfig.getMaxValue().getTime() - colConfig.getMinValue().getTime());
    //sec
    delta = delta / 1000;
    //min
    delta = delta / 60;
    //hour
    delta = delta / 60;
    //day
    delta = delta / 24;

    Calendar cal = Calendar.getInstance();
    cal.setTime(colConfig.getMinValue());

    cal.add(Calendar.DAY_OF_MONTH, 1 + new Random().nextInt(delta.intValue()));

    return cal.getTime();
  }

  public static Integer buildInt(ColConfig<Integer> colConfig) {
    colConfig.setMinValue(
      Optional.ofNullable(colConfig.getMinValue()).orElse(Integer.MIN_VALUE)
    );
    colConfig.setMaxValue(
      Optional.ofNullable(colConfig.getMinValue()).orElse(Integer.MAX_VALUE)
    );

    Long val = null;

    do {
      val = new Random().nextLong() - colConfig.getMinValue();
    } while (val >= colConfig.getMinValue() && val <= colConfig.getMaxValue());

    return val.intValue();
  }

  public static Boolean buildBoolean(ColConfig<Boolean> colConfig) {
    return (new Random().nextDouble()) > 0.5d;
  }

  public static Double buildDouble(ColConfig<Double> colConfig) {
    colConfig.setMinValue(-10000000D);
    colConfig.setMaxValue(10000000D);

    Double val = null;
    Double delta = colConfig.getMaxValue() - colConfig.getMinValue();

    // do {
    val = colConfig.getMinValue() + (delta * new Random().nextDouble());
    // } while (val >= colConfig.getMinValue() && val <= colConfig.getMaxValue());

    return val.doubleValue();
  }

  static List<ColConfig<?>> buyildColConfigs() {
    List<ColConfig<?>> colConfigs = new ArrayList<>();

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

    return colConfigs;
  }

  public static void writeFieldsData(ITableWriter writer) {
    writer.addField(Calendar.getInstance());
    writer.addField((Calendar) null);

    writer.addField(new Date());
    writer.addField((Date) null);

    writer.addField(Boolean.TRUE);
    writer.addField((Boolean) null);

    writer.addField(BigDecimal.valueOf(100l));
    writer.addField((BigDecimal) null);

    writer.addField(Double.valueOf(10));
    writer.addField((Double) null);

    writer.addField(Float.valueOf("123"));
    writer.addField((Float) null);

    writer.addField(BigInteger.valueOf(123l));
    writer.addField((BigInteger) null);

    writer.addField(123L);
    writer.addField((Long) null);

    writer.addField(Integer.valueOf(123));
    writer.addField((Integer) null);

    writer.addField(Short.valueOf("123"));
    writer.addField((Short) null);

    writer.addField("");
    writer.addField((String) null);
  }

  public static void writePojoData(ITableWriter writer) {
    List<Pojo> data = TestHelper.buildPojos(500);

    writer.addPojos(data);

    data = TestHelper.buildPojos(100);

    writer.addPojos(data, false);
  }

  public static void writePlainData(ITableWriter writer) {
    List<ColConfig<?>> colConfigs = buyildColConfigs();

    writer.writeTitles(
      colConfigs
        .stream()
        .map(cc -> cc.getName())
        .collect(Collectors.toList())
        .toArray(new String[] {})
    );

    List<Object[]> data = TestHelper.buildData(colConfigs, 500);

    writer.addLines(data);

    List<Pojo> pojos = TestHelper.buildPojos(100);

    writer.addPojos(pojos, false);
  }
}
