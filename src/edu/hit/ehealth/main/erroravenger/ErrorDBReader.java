package edu.hit.ehealth.main.erroravenger;

import edu.hit.ehealth.main.dao.ErrorMessageDao;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.ErrorMessage;

public class ErrorDBReader {

    private ErrorMessageDao emDao =
            GlobalApplicationContext.getContext().getBean(ErrorMessageDao.class);

    public static void main(String[] args) {
        ErrorDBReader errorDBReader = new ErrorDBReader();
        errorDBReader.test();
    }

    private void test() {
        Iterable<ErrorMessage> all = emDao.findAll();
        for (ErrorMessage errorMessage : all) {
            if (Utils.SHOULD_PRT) System.out.println("errorMessage = " + errorMessage);
        }
    }

    public Iterable<ErrorMessage> getAllErrorMsg() {
        Iterable<ErrorMessage> allEM = emDao.findAll();
        return allEM;
    }

    public int getErrorCount() {
        int count = 0;
        for (ErrorMessage errorMessage : getAllErrorMsg()) {
            count++;
        }
        return count;
    }

    public void deleteErrorMsg(ErrorMessage em) {
        emDao.delete(em);
    }

}
