package edu.hit.ehealth.main.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GlobalApplicationContext {

    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
            new String[]{"applicationContext.xml"});

    public static ApplicationContext getContext() {
        return applicationContext;
    }

}
