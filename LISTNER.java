



import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import javax.annotation.Nonnull;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PseudoColumnUsage;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;


public class LISTNER extends ListenerAdapter {


  private static JSONObject obj = new JSONObject();


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        String jsonFileStr = null;
        try {
            jsonFileStr = new String(
                    Files.readAllBytes(Paths
                            .get("test.json")),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(jsonFileStr);

        try {
            obj = (JSONObject) new JSONParser().parse(jsonFileStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {

        String Id = event.getMember().getUser().getId();
        long time =Instant.now().getEpochSecond();


            obj.put(Id, Id);
            obj.put(Id+"1", new Long(time));


        try{

            FileWriter file = new FileWriter("test.json");

            file.write(obj.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {

        String Id = event.getMember().getUser().getId();
        if(obj.get(Id+"1")!=null) {
            long timeLeave = Instant.now().getEpochSecond();

            obj.put(Id, Id);
            obj.put(Id + "2", new Long(timeLeave));


            long timeTotal = timeLeave - (long) obj.get(Id + "1");


            if (obj.get(Id + "3") == null)
                obj.put(Id + "3", new Long(timeTotal));
            else obj.put(Id + "3", new Long((long) obj.get(Id + "3") + timeTotal));


            try {

                FileWriter file = new FileWriter("test.json");

                file.write(obj.toJSONString());
                file.flush();
                file.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String Id = event.getAuthor().getId();
        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();

        //==================================================
        if (obj.get(Id + "msg") != null)
            obj.put(Id + "msg", (long) obj.get(Id + "msg") + 1);
        else
            obj.put(Id + "msg", new Long(1));

        try {

            FileWriter file = new FileWriter("test.json");

            file.write(obj.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //==================================================

        if (event.getMessage().getContentRaw().startsWith(prefix + "stats")) {
            if (raw.equalsIgnoreCase(prefix + "stats")) {
                if (obj.get(Id + "3") != null) {
                    long minute = TimeUnit.SECONDS.toMinutes((long) obj.get(Id + "3"));
                    long hours = TimeUnit.SECONDS.toHours((long) obj.get(Id + "3"));
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("ShiroBot", null,
                            "https://media.discordapp.net/attachments/802701587293143041/8027" +
                                    "02412635832351/1530565453_97847679dadfe0ee8fe7f0a23209d9f6.gif?width=399&height=559");
                    embedBuilder.setTitle("Ваша статистика на локалке ");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    embedBuilder.setColor(Color.getHSBColor((float) Math.random() * 255, (float) Math.random() * 255, (float) Math.random() * 255));
                    if (minute >= 60) {
                        embedBuilder.addField("Часов в войсе: " + System.lineSeparator() + hours, " ", true);
                    } else {
                        embedBuilder.addField("Минут в войсе: " + System.lineSeparator() + minute, " ", true);
                    }
                    embedBuilder.addField("Сообщений в чате: " + System.lineSeparator() + obj.get(Id + "msg"), " ", true);
                    embedBuilder.setFooter("На локалке с " + event.getMember().getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE), event.getMember().getUser().getAvatarUrl());
                    embedBuilder.setThumbnail(event.getMember().getUser().getAvatarUrl());
                    TextChannel channel = event.getChannel();
                    if (obj.get(Id) != null && obj.get(Id + "3") != null) {
                        channel.sendMessage(embedBuilder.build()).queue();
                        channel.deleteMessageById(channel.getLatestMessageId()).queue();
                        channel.getHistoryAfter(channel.getLatestMessageId(), 1);
                    }
                } else {
                    event.getChannel().sendMessage("Нужно зайти, а затем выйти из голосового канала");
                }
            } else if (raw.equalsIgnoreCase(prefix + "stats " + event.getMessage().getContentRaw().split(" ")[1])) {
                String[] a1 = event.getMessage().getContentRaw().split(" ");
                String[] a2 = a1[1].split("!");
                String[] a3 = a2[1].split(">");
                String a4 = a3[0];
                Member member = event.getGuild().getMemberById(a4);
                if (obj.get(a4 + "3") != null) {
                    long minute = TimeUnit.SECONDS.toMinutes((long) obj.get(a4 + "3"));
                    long hours = TimeUnit.SECONDS.toHours((long) obj.get(a4 + "3"));
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("ShiroBot", null,
                            "https://media.discordapp.net/attachments/802701587293143041/802702412635832351/1530565453_97847679dadfe0ee8fe7f0a23209d9f6.gif?width=399&height=559");
                    embedBuilder.setTitle("Статистика на локалке ");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    embedBuilder.setColor(Color.getHSBColor((float) Math.random() * 255, (float) Math.random() * 255, (float) Math.random() * 255));
                    if (minute >= 60) {
                        embedBuilder.addField("Часов в войсе: " + System.lineSeparator() + hours, " ", true);
                    } else {
                        embedBuilder.addField("Минут в войсе: " + System.lineSeparator() + minute, " ", true);
                    }
                    embedBuilder.addField("Сообщений в чате: " + System.lineSeparator() + obj.get(a4 + "msg"), " ", true);
                    embedBuilder.setFooter("На локалке с " + member.getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE), member.getUser().getAvatarUrl());
                    embedBuilder.setThumbnail(member.getUser().getAvatarUrl());
                    TextChannel channel = event.getChannel();
                    if (obj.get(Id) != null && obj.get(Id + "3") != null) {
                        channel.sendMessage(embedBuilder.build()).queue();
                        channel.deleteMessageById(channel.getLatestMessageId()).queue();
                        channel.getHistoryAfter(channel.getLatestMessageId(), 1);
                    }
                } else {
                    event.getChannel().sendMessage("Нет статистики о пользователе");
                }
            }
        }
    }


    public void onMessageEmbed(@Nonnull MessageEmbedEvent event) {
        if(event.getMessageEmbeds().get(0).getAuthor().getName().equals("ShiroBot")) {
            event.getChannel().deleteMessageById(event.getMessageId()).queueAfter(10,TimeUnit.SECONDS);
        }
    }
}



