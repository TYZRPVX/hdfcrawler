package edu.hit.ehealth.main.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AntiCrawlerHacker {


    public static int randWaitSecond() {
        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand;
        int max = 1;
        int min = 0;
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

        return randomNum;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("randWaitSecond() = " + randWaitSecond());
        }
    }
}
