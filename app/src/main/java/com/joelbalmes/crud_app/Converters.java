package com.joelbalmes.crud_app;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

  @TypeConverter
  public Date fromTimeStamp(Long value ) {
    return value == null ? null : new Date( value );
  }

  @TypeConverter
  public Long dateToTimeStamp( Date date ) {
    return date == null ? null : date.getTime();
  }

}
