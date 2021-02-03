

import me.duncte123.botcommons.BotCommons;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class Listen extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listen.class);

    @Override
    //@SubscribeEvent
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());

    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if(user.isBot() || event.isWebhookMessage()){
            return;
        }


        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();

        if(raw.equalsIgnoreCase(prefix+"ping")){

            event.getJDA().getRestPing().queue( (time)  -> event.getChannel().sendMessageFormat("Ping: %d ms", time).queue());

            }





    }

}