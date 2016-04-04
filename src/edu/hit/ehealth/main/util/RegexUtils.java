package edu.hit.ehealth.main.util;

import edu.hit.ehealth.main.util.mail.Mailer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    static String[] excludedRegex = {
            "\\((.*)\\)",
            "\\s+(\\S+)\\s*",
    };

    public static String regexReplace(String pattern, String text, String repSeg) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        if (matcher.find() && matcher.groupCount() > 0) {
            return matcher.replaceFirst(repSeg);
        } else {
            String replaceErrorMsg = pattern + System.lineSeparator()
                    + text + System.lineSeparator()
                    + repSeg + System.lineSeparator();
            Mailer.sendToAddressList(RegexUtils.class.getCanonicalName(), replaceErrorMsg + Utils.getStackTrace());
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
        regexFind("45", "");
    }

}
