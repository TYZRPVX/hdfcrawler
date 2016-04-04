package edu.hit.ehealth.main.util;

import edu.hit.ehealth.main.erroravenger.ErrorAvenger;
import edu.hit.ehealth.main.erroravenger.ErrorDBReader;
import edu.hit.ehealth.main.vo.ErrorMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于分析数据库中异常产生原因，统计异常的特征
 */

@Deprecated
class ErrorChecker {

    private ErrorDBReader reader;

    public ErrorChecker() {
        reader = new ErrorDBReader();
    }

    public static void main(String[] args) {
        new ErrorChecker().showAllReasons();
    }

    public void showAllReasons() {
        Set<String> errorReasons = new HashSet<String>();
        Set<ErrorMessage> shownMsgs = new HashSet<ErrorMessage>();
        Iterable<ErrorMessage> allErrorMsg = reader.getAllErrorMsg();
        for (ErrorMessage message : allErrorMsg) {
            String reason = message.getErrorReason();
            if (!errorReasons.contains(reason)) {
                errorReasons.add(reason);
                shownMsgs.add(message);
            }
        }
        soutAllMsg(shownMsgs);
        reRunCrawlers(shownMsgs);
    }

    private void reRunCrawlers(Set<ErrorMessage> shownMsgs) {
        for (ErrorMessage errorMessage : shownMsgs) {
            try {
                ErrorAvenger.getInstance(errorMessage).reflectCrawlMethod();
            } catch (Exception e) {
                e.printStackTrace();
            }
            reader.deleteErrorMsg(errorMessage);
        }
    }

    public void soutAllMsg(Iterable<ErrorMessage> ems) {
        for (ErrorMessage em : ems) {
            System.out.println("em = " + em);
        }
    }
}
