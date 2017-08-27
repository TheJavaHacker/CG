package net.central_games.Core.Utilities;

import net.central_games.Core.Core;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by TheTARDIS2176 on 09/08/2017.
 */
public class RanksManager {

    public static HashMap<Player, Ranks> ranks = new HashMap<Player, Ranks>();
    public static HashMap<Ranks, String> prefix = new HashMap<Ranks, String>();
    public static HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

    public static Connection connection;
    public static String host, database, username, password;
    public static int port;

    public static void registerPrefixes(){
        prefix.put(Ranks.Owner, "§8[§4§lOwner§8] §4");
        prefix.put(Ranks.Developer, "§8[§4Developer§8] §4");
        prefix.put(Ranks.Manager, "§8[§9Manager§8] §e");
        prefix.put(Ranks.Senior_Mod, "§8[§dSenior-Mod§8] §d");
        prefix.put(Ranks.Junior_Mod, "§8[§5Junior-Mod§8] §5");
        prefix.put(Ranks.Events, "§8[§2Events§8] §2");
        prefix.put(Ranks.Helper, "§8[§2Helper§8] §2");
        prefix.put(Ranks.Sponsor, "§8[§aS§bp§co§dn§es§6o§3r§8] §c");
        prefix.put(Ranks.YouTuber, "§8[§4You§fTuber§8] §c");
        prefix.put(Ranks.Streamer, "§8[§5Streamer§8] §c");
        prefix.put(Ranks.Friend, "§8[§9Friend§8] §9");
        prefix.put(Ranks.MVP, "§8[§bMVP§8] §b");
        prefix.put(Ranks.VIP, "§8[§aVIP§8] §a");
        prefix.put(Ranks.ALL, "§7");
    }

    public static void openConnection() throws SQLException, ClassNotFoundException {
        if(connection != null && !connection.isClosed()){
            return;
        }

        synchronized(Core.plugin){
            if(connection != null && connection.isClosed()){
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
        }
    }

    public static void setRank(Player p, Ranks r){
        ranks.put(p, r);

        String UUID = p.getUniqueId().toString();
        String name = p.getPlayer().getName();

        try{

            PreparedStatement sql = connection.prepareStatement("UPDATE `player_data` SET `Name`= ?,`UUID`= ?,`Rank`= ? WHERE `UUID` = ?;");
            sql.setString(1, name);
            sql.setString(2, UUID);
            sql.setString(3, r.toString());
            sql.setString(4, UUID);

            sql.execute();
            sql.close();
        }catch(SQLException e1){
            e1.printStackTrace();
        }
        p.setDisplayName(prefix.get(r) + p.getName());
        p.setPlayerListName(prefix.get(r) + p.getName());
    }

    public static Ranks getRank(Player p) {
        String UUID = p.getPlayer().getUniqueId().toString();

        try {

            PreparedStatement sql = connection.prepareStatement("SELECT `Rank` FROM `player_data` WHERE `UUID`= ?;");
            sql.setString(1, UUID);

            ResultSet rs = sql.executeQuery();
            while (rs.next()) {
                ranks.put(p, Ranks.valueOf(rs.getString("Rank")));
            }

            sql.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        Ranks r = ranks.get(p.getPlayer());
        return r;
    }

    public static void applyPerms(Player player){
        PermissionAttachment attachment = player.addAttachment(Core.plugin);
        perms.put(player.getUniqueId(), attachment);
        PermissionAttachment pperms = perms.get(player.getUniqueId());
        for(String permissions : Core.plugin.getConfig().getStringList("Permissions." + getRank(player))){
            pperms.setPermission(permissions, true);
        }
    }

}
