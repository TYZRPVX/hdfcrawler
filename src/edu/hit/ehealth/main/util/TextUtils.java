package edu.hit.ehealth.main.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    public static String gk2312ToUtf8(String s) {
        String utf8_s = s;
        try {
            byte[] bytes = s.getBytes("gb2312");
            utf8_s = new String(bytes, "utf-8");
            System.out.println("utf8_s = " + utf8_s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return utf8_s;
    }

    public static String getTextInBrace(String text) {
        String pattern = "\\(\\S+\\)";
        String retText = text;
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        if (matcher.find()) {
            String matchText = matcher.group(0);
            retText = matchText.substring(1, matchText.length() - 1);
        }
        return retText;
    }
}
