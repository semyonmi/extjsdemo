package ru.semyonmi.extjsdemo.util;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.databind.util.ISO8601Utils;

import java.text.FieldPosition;
import java.util.Date;

public class ISO8601DateFormatWithMillis extends ISO8601DateFormat {

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        String value = ISO8601Utils.format(date, true);
        toAppendTo.append(value);
        return toAppendTo;
    }
}
