package com.gt.tablewriter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractTableWriter implements ITableWriter {

  static final Class<?>[] SUPPORTED_CLASSES = {
    Short.class,
    short.class,
    Integer.class,
    int.class,
    Long.class,
    long.class,
    BigInteger.class,
    Float.class,
    float.class,
    Double.class,
    double.class,
    BigDecimal.class,
    Boolean.class,
    boolean.class,
    String.class,
    Date.class,
    Calendar.class,
  };

  @Getter
  @Setter
  String dateFormat = "yyyy/MM/dd HH:mm";

  @Getter
  @Setter
  String decimalFormat = "#,##0.00";

  @Getter
  @Setter
  String integerFormat = "#,##0";

  @Getter
  @Setter
  Properties properties;

  public AbstractTableWriter() {
    this.properties = new Properties();
  }

  public AbstractTableWriter(Properties properties) {
    this.properties = properties;
  }

  @Override
  public void prepare() {
    this.setDateFormat(properties.getProperty("DATE_FORMAT", getDateFormat()));
    this.setDecimalFormat(
        properties.getProperty("DECIMAL_FORMAT", getDecimalFormat())
      );
    this.setIntegerFormat(
        properties.getProperty("INTEGER_FORMAT", getIntegerFormat())
      );
  }

  @Override
  public void writeTo(String fileName) throws IOException {
    OutputStream out = new java.io.FileOutputStream(fileName);
    writeTo(out);
    out.close();
    close();
  }

  @Override
  public void addLine(Object[] line) {
    for (Object object : line) {
      addObjectValue(object);
    }
    addNewLine();
  }

  @Override
  public void addField(BigInteger number) {
    if (number != null) {
      addField(number.intValue());
    } else {
      addField((Integer) null);
    }
  }

  @Override
  public void addField(Short number) {
    if (number != null) {
      addField(number.intValue());
    } else {
      addField((Integer) null);
    }
  }

  @Override
  public void addField(Float number) {
    if (number != null) {
      addField(number.doubleValue());
    } else {
      addField((Double) null);
    }
  }

  @Override
  public void addField(BigDecimal number) {
    if (number != null) {
      addField(number.doubleValue());
    } else {
      addField((Double) null);
    }
  }

  @Override
  public void addField(Calendar calendar) {
    if (calendar != null) {
      addField(calendar.getTime());
    } else {
      addField((Date) null);
    }
  }

  @Override
  public <T> void addPojos(Iterable<T> iterable) {
    this.addPojos(iterable, true);
  }

  @Override
  public <T> void addPojos(Iterable<T> iterable, boolean writeTitles) {
    List<Method> methods = null;

    Iterator<T> iterator = iterable.iterator();

    while (iterator.hasNext()) {
      T pojo = iterator.next();
      if (methods == null) {
        methods = findMethods(pojo.getClass());
        if (writeTitles) {
          writeTitles(getTitles(methods));
        }
      }
      addPojo(pojo, methods);
    }
  }

  @Override
  public void addLines(Iterable<Object[]> iterable) {
    Iterator<Object[]> iterator = iterable.iterator();

    while (iterator.hasNext()) {
      addLine(iterator.next());
    }
  }

  public <T> String[] getTitles(Class<T> pojoClass) {
    return getTitles(findMethods(pojoClass));
  }

  public String[] getTitles(List<Method> methods) {
    List<String> titles = new ArrayList<>();
    for (Method method : methods) {
      if (method.getName().startsWith("get")) {
        titles.add(method.getName().substring(3));
      } else if (method.getName().startsWith("is")) {
        titles.add(method.getName().substring(2));
      }
    }
    return titles.toArray(new String[] {});
  }

  public void writeTitles(String[] titles) {
    this.addLine(titles);
  }

  @Override
  public <T> void addPojo(T pojo) {
    List<Method> methods = findMethods(pojo.getClass());

    addPojo(pojo, methods);
  }

  private <T> void addPojo(T pojo, List<Method> methods) {
    if (pojo != null && methods != null) {
      for (Method method : methods) {
        try {
          addObjectValue(method.getReturnType(), method.invoke(pojo));
        } catch (
          IllegalAccessException
          | IllegalArgumentException
          | InvocationTargetException e
        ) {
          Logger
            .getLogger(getClass().getName())
            .log(Level.SEVERE, "Error adding field", e);
        }
      }
    }

    addNewLine();
  }

  @Override
  public void addObjectValue(Object value) {
    if (value == null) {
      addObjectValue(String.class, "");
    } else {
      addObjectValue(value.getClass(), value);
    }
  }

  private void addObjectValue(Class<?> objectClass, Object value) {
    switch (objectClass.getSimpleName()) {
      case "Short":
      case "short":
        addField((Short) value);
        break;
      case "Integer":
      case "int":
        addField((Integer) value);
        break;
      case "Long":
      case "long":
        addField((Long) value);
        break;
      case "BigInteger":
        addField((BigInteger) value);
        break;
      case "Float":
      case "float":
        addField((Float) value);
        break;
      case "Double":
      case "double":
        addField((Double) value);
        break;
      case "BigDecimal":
        addField((BigDecimal) value);
        break;
      case "Boolean":
      case "boolean":
        addField((Boolean) value);
        break;
      case "String":
        addField((String) value);
        break;
      case "Date":
        addField((Date) value);
        break;
      case "Calendar":
        addField((Calendar) value);
        break;
      case "Timestamp":
        addField(new Date(((Timestamp) value).getTime()));
        break;
      default:
        Logger
          .getLogger(getClass().getName())
          .log(
            Level.WARNING,
            "Clase no soportada: " + objectClass.getSimpleName()
          );
        addField(value.toString());
        break;
    }
  }

  private <T> List<Method> findMethods(Class<T> pojoClass) {
    List<Method> getters = Arrays
      .asList(pojoClass.getMethods())
      .stream()
      .filter(method ->
        (
          (
            method.getName().startsWith("get") ||
            method.getName().startsWith("is")
          ) &&
          supportedClass(method.getReturnType())
        )
      )
      .collect(Collectors.toList());

    return getters;
  }

  private boolean supportedClass(Class<?> returnType) {
    return Arrays.asList(SUPPORTED_CLASSES).contains(returnType);
  }
}
