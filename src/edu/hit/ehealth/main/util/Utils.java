package edu.hit.ehealth.main.util;

import edu.hit.ehealth.main.dao.ErrorMessageDao;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.vo.ErrorMessage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class Utils {

    public static boolean SHOULD_PRT = true;
    static MessageDigest md5 = null;
    private static Logger LOGGER_INSTANCE;
    private static ErrorMessageDao emDao =
            GlobalApplicationContext.getContext().getBean(ErrorMessageDao.class);

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    public static Logger obtainLogger() {
        if (LOGGER_INSTANCE == null) {
            Properties props = new Properties();
            try {
                props.load(Utils.class.getResourceAsStream("/crawlerLog4j.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            PropertyConfigurator.configure(props);
            LOGGER_INSTANCE = Logger.getLogger(Utils.class);
        }
        return LOGGER_INSTANCE;
    }

    public static void writeExceptionToDB(ErrorMessage errorMessage) {
        try {
            emDao.save(errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getStackTrace() {
        StringBuilder stes = new StringBuilder();
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (StackTraceElement ste : trace) {
            stes.append("called by "
                    + ste.getClassName() + "." +
                    ste.getMethodName() + "/" +
                    ste.getFileName() + "\n"
            );
        }
        return stes.toString();
    }


    public static String strToMD5(String str) {
        md5.update(str.getBytes());
        byte[] bs = md5.digest();
        int i;
        StringBuffer sb = new StringBuffer();
        for (int offset = 0; offset < bs.length; offset++) {
            i = bs[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(i));
        }
        return sb.toString();
    }

    public static BufferedReader getUrlReader(String url) {
        URL myUrl = null;
        try {
            myUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(myUrl.openStream(), "gb2312")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public static Date getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return date;
    }


    public static void print(String out) {
        if (Utils.SHOULD_PRT)
            System.out.println(out);
    }

    //XXX: I can't parse
    public static String noWayParseDateText(String dateText) {
        return dateText;
    }


    public static void main(String[] args) throws ParseException {
        Logger logger = obtainLogger();
        logger.error("43");
    }

    public static List<String> readObjList(String path) {
        List<String> objList = new ArrayList<String>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            objList = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objList;
    }

    public static void writeObjList(List<String> l, String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(l);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
