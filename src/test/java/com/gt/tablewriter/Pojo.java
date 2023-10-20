package com.gt.tablewriter;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pojo {

  String nombre;
  Integer edad;
  int edad1;
  Double saldo;
  double saldo1;
  Date inicio;
  Boolean activo;
  boolean activo1;
  Short shortValue;
  short shortValue1;
  Long longValue;
  long longValue1;
}
