package ru.javawebinar.topjava.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {

    public static String decode(String str) throws UnsupportedEncodingException {
        return new String(str.getBytes("ISO-8859-1"),"UTF8");
    }
}
