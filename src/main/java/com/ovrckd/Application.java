package com.ovrckd;

import com.ovrckd.config.AppConfig;
import com.ovrckd.discord.listener.DiscordMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Application {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var jda = context.getBean(JDA.class);
        var messageListener = context.getBean(DiscordMessageListener.class);

        jda.addEventListener(messageListener);
    }
}