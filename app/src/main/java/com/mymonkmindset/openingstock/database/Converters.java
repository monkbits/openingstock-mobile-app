package com.mymonkmindset.openingstock.database;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class Converters {
    @TypeConverter
    public static LocalDateTime toLocalDateTime(String dateString) {
        return dateString == null ? null : LocalDateTime.parse(dateString);
    }

    @TypeConverter
    public static String fromLocalDateTime(LocalDateTime date) {
        return date == null ? null : date.toString();
    }
}
