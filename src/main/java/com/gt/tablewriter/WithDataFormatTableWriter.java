package com.gt.tablewriter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public abstract class WithDataFormatTableWriter extends AbstractTableWriter {

  public static final String PROPERTY_HTML_TEMPLATE = "HTML_TEMPLATE";

  protected DateFormat sdf = null;

  protected DateTimeFormatter ldf = null;

  protected DateTimeFormatter ltf = null;

  protected DateTimeFormatter ldtf = null;

  protected DecimalFormat decf = null;

  protected DecimalFormat intf = null;

  public WithDataFormatTableWriter() {
    super();
  }

  public WithDataFormatTableWriter(Properties properties) {
    super(properties);
  }

  @Override
  public void prepare() {
    super.prepare();
    sdf = new SimpleDateFormat(this.getDateTimeFormat());
    ldf = DateTimeFormatter.ofPattern(this.getDateFormat());
    ldtf = DateTimeFormatter.ofPattern(this.getDateTimeFormat());
    ltf = DateTimeFormatter.ofPattern(this.getTimeFormat());
    decf = new DecimalFormat(this.getDecimalFormat());
    intf = new DecimalFormat(this.getIntegerFormat());
  }

  @Override
  public void addField(Long value) {
    if (value != null) {
      internalAddField(intf.format(value), Long.class);
    } else {
      internalAddField("", Long.class);
    }
  }

  @Override
  public void addField(Double value) {
    if (value != null) {
      internalAddField(decf.format(value), Double.class);
    } else {
      internalAddField("", Double.class);
    }
  }

  @Override
  public void addField(String value) {
    if (value != null) {
      internalAddField(value, String.class);
    } else {
      internalAddField("", String.class);
    }
  }

  @Override
  public void addField(Date value) {
    if (value != null) {
      internalAddField(sdf.format(value), Date.class);
    } else {
      internalAddField("", Date.class);
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
  public void addField(LocalDate value) {
    if (value != null) {
      internalAddField(ldf.format(value), LocalDate.class);
    } else {
      internalAddField("", LocalDate.class);
    }
  }

  @Override
  public void addField(LocalDateTime value) {
    if (value != null) {
      internalAddField(ldtf.format(value), LocalDateTime.class);
    } else {
      internalAddField("", LocalDateTime.class);
    }
  }

  @Override
  public void addField(LocalTime value) {
    if (value != null) {
      internalAddField(ldf.format(value), LocalTime.class);
    } else {
      internalAddField("", LocalTime.class);
    }
  }

  @Override
  public void addField(Boolean value) {
    if (value != null) {
      internalAddField(value ? "Si" : "No", Boolean.class);
    } else {
      internalAddField("", Boolean.class);
    }
  }

  protected abstract void internalAddField(
      String formatedField,
      Class<?> clazz);
}
