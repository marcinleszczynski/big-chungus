package com.ovrckd;

import com.ovrckd.config.AppConfig;
import com.ovrckd.discord.listener.BigChungusMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static com.ovrckd.utils.SpringUtils.getActiveSpringProfile;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class Application {

    @SneakyThrows
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext();
        var activeProfile = getActiveSpringProfile();
        if (!isBlank(activeProfile)) {
            var activeProfileConfig = new ClassPathResource("application-" + activeProfile + ".properties");
            var properties = new Properties();
            properties.load(activeProfileConfig.getInputStream());

            var propertySource = new PropertiesPropertySource(activeProfile, properties);
            var env = context.getEnvironment();
            env.getPropertySources().addLast(propertySource);
            context.register(AppConfig.class);
            context.refresh();
        }
        var jda = context.getBean(JDA.class);
        var bigChungusListener = context.getBean(BigChungusMessageListener.class);

        jda.addEventListener(bigChungusListener);
    }
}