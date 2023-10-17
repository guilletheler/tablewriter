package com.gt.tablewriter;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pojo {

  String nombre;
  Integer edad;
  Double saldo;
  Date inicio;
  Boolean activo;
}
