package com.gt.tablewriter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColConfig<T> {

  String name;

  Class<T> clazz;

  Integer minLength;
  Integer maxLength;

  T minValue;
  T maxValue;

  T defaultValue;
}
