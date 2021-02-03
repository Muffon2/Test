

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.Collection;


public class Bot {



    private Bot() throws LoginException {



        JDABuilder jdaBuilder = JDABuilder.createDefault(Config.get("token"));
                 jdaBuilder
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new Listen(),new LISTNER())
                .setActivity(Activity.watching("Anime"))
                .build();



    }

    public static void main(String[] args) throws LoginException, SQLException, InterruptedException {
        new Bot();

    }



}