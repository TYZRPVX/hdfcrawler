package edu.hit.ehealth.test;

import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.doctor.DoctorExperienceDao;
import edu.hit.ehealth.main.vo.doctor.DoctorExperience;

public class TestDB {

    private static DoctorExperienceDao experienceDao =
            GlobalApplicationContext.getContext().getBean(DoctorExperienceDao.class);
    private DoctorExperience experience = new DoctorExperience();

    public static void main(String[] args) {
        TestDB testDB = new TestDB();
        testDB.testTry();
        System.out.println("end");
    }

    public void testTry() {
        try {
            System.out.println("1");
            System.out.println(1 / 0);
            System.out.println("2");
        } catch (Exception e) {

        }
    }

    public static String longStr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("1");
        }
        return sb.toString();
    }

    private void runTest() {
        String s = longStr();
        System.out.println("s = " + s.substring(1, 100));
        experience.setCureProcess(s);
        experienceDao.save(experience);
    }
}
