package com.ovrckd.utils;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SpringUtils {

    private static final String DEVELOPMENT_PROFILE = "dev";

    public static String getActiveSpringProfile() {
        var context = new AnnotationConfigApplicationContext();
        var profiles = context.getEnvironment().getActiveProfiles();
        if (profiles.length == 0) {
            return "";
        }
        return profiles[0];
    }

    public static boolean isDevelopment() {
        return getActiveSpringProfile().equals(DEVELOPMENT_PROFILE);
    }
}
