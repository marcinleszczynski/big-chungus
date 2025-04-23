package com.ovrckd.discord.sender;

import com.ovrckd.discord.sender.dto.DiscordMessageSendRequest;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class DiscordMessageSendService {

    private final JDA jda;

    public void process(DiscordMessageSendRequest request) {
        var channel = getChannelFor(request.channelId());
        Objects.requireNonNull(channel, "Channel should not be null");
        channel.sendMessage(request.message()).queue();
    }

    private MessageChannel getChannelFor(String channelId) {
        var textChannel = jda.getTextChannelById(channelId);
        if (textChannel != null) {
            return textChannel;
        }
        return jda.getPrivateChannelById(channelId);
    }
}
