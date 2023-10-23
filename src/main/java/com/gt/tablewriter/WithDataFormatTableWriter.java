package com.gt.tablewriter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;

public abstract class WithDataFormatTableWriter extends AbstractTableWriter {

  @Getter
  @Setter
  DateFormat sdf = null;

  @Getter
  @Setter
  DecimalFormat decf = null;

  @Getter
  @Setter
  DecimalFormat intf = null;

  public WithDataFormatTableWriter() {
    super();
  }

  public WithDataFormatTableWriter(Properties properties) {
    super();
  }

  @Override
  public void prepare() {
    super.prepare();
    sdf = new SimpleDateFormat(this.getDateFormat());

    decf = new DecimalFormat(this.getDecimalFormat());
    intf = new DecimalFormat(this.getIntegerFormat());
  }

  public void addField(Integer value) {
    if (value != null) {
      internalAddField(intf.format(value), Integer.class);
    } else {
      internalAddField("", Integer.class);
    }
  }

  public void addField(Long value) {
    if (value != null) {
      internalAddField(intf.format(value), Long.class);
    } else {
      internalAddField("", Long.class);
    }
  }

  public void addField(Double value) {
    if (value != null) {
      internalAddField(decf.format(value), Double.class);
    } else {
      internalAddField("", Double.class);
    }
  }

  public void addField(String value) {
    if (value != null) {
      internalAddField(value, String.class);
    } else {
      internalAddField("", String.class);
    }
  }

  public void addField(Date value) {
    if (value != null) {
      internalAddField(sdf.format(value), Date.class);
    } else {
      internalAddField("", Date.class);
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
    Class<?> clazz
  );
}
