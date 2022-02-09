package com.gt.tablewriter;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public interface ITableWriter {
	void open();
	void open(Properties properties);

	void addNewLine();

	<T> void addPojos(Iterable<T> iterable);
	<T> void addPojo(T pojo);
	void addLine(Object[] line);
	void addLines(Iterable<Object[]> line);

	void addField(String value);

	void addField(Short value);
	void addField(Integer value);
	void addField(Long value);
	void addField(BigInteger value);

	void addField(Float value);
	void addField(Double value);
	void addField(BigDecimal value);

	void addField(Boolean value);

	void addField(Date value);
	void addField(Calendar value);
	void close();

	void writeTo(OutputStream outputStream) throws IOException;
	void writeTo(String fileName) throws IOException;

	void addObjectValue(Object value);
}
