package edu.hit.ehealth.main.define;

public class TextValue {

    public static class Error {
        public final static String PARSE_ERROR = "PARSE_ERROR";
        public final static String TIME_OUT = "TIME_OUT";
        public final static String PAGE_NOT_FOUND = "PAGE_NOT_FOUND";
        public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
        public static final String REGEX_ERROR = "REGEX_ERROR";
    }

    public static class Path {
        public static final String provinces = "utils\\output\\allProvinces.obj";
        public static String hospitals = "utils\\output\\allHospitals.obj";
        public static String doctorHomepages = "utils\\output\\allDoctorHomepages.obj";
        public static String doctorInfoCenters = "utils\\output\\allDoctorInfoCenter.obj";
        public static String facultys = "utils\\output\\allFacultys.obj";
        public static String specialitys = "utils\\output\\allSpecialitys.obj";
        public static String phoneDoctors = "utils\\output\\phoneDoctors.obj";
        public static String exceptionsLog = "utils\\output\\log4j-exceptions-details.txt";
    }

    public static class Mail {

        //        public static String content = "Deeply sorry for disturbing you, " +
//                "but I have to inform you that I have crawled a database just now " +
//                "or done something important, the attachment is my fault, " +
//                "it is very nice of you to correct my fault if possible ;-)";
        public static String content = "blank";
    }
}
