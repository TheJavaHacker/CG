package net.central_games.Core.Utilities;

import net.central_games.Core.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TheTARDIS2176 on 09/08/2017.
 */
public class Listeners implements Listener {

    private final String INVALID_CMD = "Unknown command. Type \"/help\" for help.";

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.setDisplayName(RanksManager.prefix.get(RanksManager.getRank(player)) + player.getName());
        player.setPlayerListName(player.getDisplayName());
        e.setJoinMessage(null);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e){
        String UUID = e.getPlayer().getUniqueId().toString();
        String name = e.getPlayer().getName();

        try{

            PreparedStatement sql = RanksManager.connection.prepareStatement("INSERT INTO player_data (Name, UUID, Rank, Clan) SELECT * FROM (SELECT ?, ?, ?, ?) AS tmp WHERE NOT EXISTS (SELECT UUID FROM player_data WHERE UUID = ?);");
            sql.setString(1, name);
            sql.setString(2, UUID);
            sql.setString(3, Ranks.ALL.toString());
            sql.setString(4, "Server");
            sql.setString(5, UUID);

            sql.execute();
            sql.close();
        }catch(SQLException e1){
            e1.printStackTrace();
        }
        RanksManager.applyPerms(e.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(RanksManager.getRank(e.getPlayer()) == Ranks.Owner){
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §c" + "%2$s");
        }else if(RanksManager.getRank(e.getPlayer()) == Ranks.Manager){
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §e" + "%2$s");
        } else if(RanksManager.getRank(e.getPlayer()) == Ranks.Developer){
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §c" + "%2$s");
        } else if(RanksManager.getRank(e.getPlayer()) == Ranks.YouTuber) {
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §6" + "%2$s");
        } else if(RanksManager.getRank(e.getPlayer()) == Ranks.Friend) {
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §6" + "%2$s");
        } else if(RanksManager.getRank(e.getPlayer()) == Ranks.Sponsor){
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §6" + "%2$s");
        } else if(RanksManager.getRank(e.getPlayer()) == Ranks.Streamer){
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §d" + "%2$s");
        } else if(RanksManager.getRank(e.getPlayer()) == Ranks.Senior_Mod){
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §3" + "%2$s");
        } else if(RanksManager.getRank(e.getPlayer()) == Ranks.Junior_Mod){
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §3" + "%2$s");
        } else if(RanksManager.getRank(e.getPlayer()) == Ranks.Events){
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §3" + "%2$s");
        } else if(RanksManager.getRank(e.getPlayer()) == Ranks.Helper){
            e.setFormat(e.getPlayer().getDisplayName() + " §b» §3" + "%2$s");
        } else {
            e.setFormat(e.getPlayer().getDisplayName() + " §8» §7" + "%2$s");
        }
    }

    @EventHandler
    public void commandPreprocess(PlayerCommandPreprocessEvent e) {
        Pattern p = Pattern.compile("^/([a-zA-Z0-9_]+):");
        Matcher m = p.matcher(e.getMessage());
        String pluginRef;

        if (m.find()) pluginRef = m.group(1);
        else return;

        for (Plugin plugin : Core.plugin.getServer().getPluginManager().getPlugins()) {
            if (plugin.getName().toLowerCase().equals(pluginRef.toLowerCase())
                    || pluginRef.toLowerCase().equals("bukkit")
                    || pluginRef.toLowerCase().equals("minecraft")
                    || pluginRef.toLowerCase().equals("spigot")){
                e.getPlayer().sendMessage(INVALID_CMD);
                e.setCancelled(true);
                break;
            }
        }
    }
    
}
