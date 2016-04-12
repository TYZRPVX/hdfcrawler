package edu.hit.ehealth.main.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参考 <a href="http://www.runoob.com/java/java-regular-expressions.html">java-regular-expressions</a>
 */
public class RegexUtils {

    static String[] excludedRegex = {
            "\\((.*)\\)",
            "\\s+(\\S+)\\s*",
    };

    /**
     * 替换 group 中<strong>第二个</strong> group
     * 因此第一个group通常采用非贪婪模式
     * <pre>
     * {@code
     * String example = regexReplace("(.+?)(\D+)(.+)", "123abc456", "xyz"); //example=123xyz456
     * }
     * </pre>
     *
     * @param pattern 文本正则表达式
     * @param text    待替换文本
     * @param repSeg  替换片段
     * @return
     */
    public static String regexReplace(String pattern, String text, String repSeg) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        if (matcher.find() && matcher.groupCount() > 0) {
            String group = matcher.group(2);
//            String ret = matcher.replaceFirst("$1" + repSeg + "$3");
            String ret = matcher.replaceFirst("$1" + repSeg);
            return ret;
        } else {
//            String replaceErrorMsg = pattern + System.lineSeparator()
//                    + text + System.lineSeparator()
//                    + repSeg + System.lineSeparator();
//            Mailer.sendToAddressList(RegexUtils.class.getCanonicalName(), replaceErrorMsg + Utils.getStackTrace());
            return text;
        }
    }

    public static Set<String> getExcludedRegex() {
        Set<String> regexSet = new HashSet<String>(Arrays.asList(excludedRegex));
        return regexSet;
    }

    static boolean needLog(String pattern) {
        return !getExcludedRegex().contains(pattern);
    }

    /**
     * 惯用规则，返回第一个group
     * @param pattern
     * @param text
     * @return
     */
    public static String regexFind(String pattern, String text) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        if (matcher.find() && matcher.groupCount() > 0) {
            return matcher.group(1);
        } else {
            String findErrorMsg = pattern + System.lineSeparator()
                    + text + System.lineSeparator();
            Utils.obtainLogger().error(findErrorMsg + Utils.getStackTrace());
            return text;
        }
    }

    public static void main(String[] args) {
        String example = regexReplace("(.+?)(\\D+)(.+)", "123abc456", "xyz"); //非贪婪模式
        System.out.println("example = " + example);

    }

}
