package com.gt.tablewriter;

import java.util.Date;

public interface ITableWriter {
	public void open();
	public void addRecord();
	public void addField(Integer value);
	public void addRecord(Integer value);
	public void addField(Long value);
	public void addRecord(Long value);
	public void addField(Double value);
	public void addField(Boolean value);
	public void addRecord(Double value);
	public void addField(String value);
	public void addRecord(String value);
	public void addField(Date value);
	public void addRecord(Date value);
	public void addRecord(Boolean value);
	public void close();
}
