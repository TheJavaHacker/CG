package net.central_games.Core;

import net.central_games.Core.Utilities.Listeners;
import net.central_games.Core.Utilities.RankCommand;
import net.central_games.Core.Utilities.RanksManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static net.central_games.Core.Utilities.RanksManager.connection;

/**
 * Created by TheTARDIS2176 on 09/08/2017.
 */
public class Core extends JavaPlugin {

    public static Core plugin;

    @Override
    public void onEnable(){
        plugin = this;
        createConfig();
        RanksManager.registerPrefixes();
        RanksManager.host = getConfig().getString("Database.host");
        RanksManager.username = getConfig().getString("Database.username");
        RanksManager.password = getConfig().getString("Database.password");
        RanksManager.database = getConfig().getString("Database.dbname");
        RanksManager.port = getConfig().getInt("Database.port");

        try{
            RanksManager.openConnection();
            Statement stmt = connection.createStatement();
        }catch(SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        try{
            PreparedStatement sql = RanksManager.connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_data(PlayerID INT UNIQUE AUTO_INCREMENT, Name TEXT NOT NULL, UUID VARCHAR(36), Rank TEXT NOT NULL, Kills INT NOT NULL DEFAULT 0, Deaths INT NOT NULL DEFAULT 0, Banned BOOLEAN NOT NULL DEFAULT false, cKeys INT NOT NULL DEFAULT 0, Clan TEXT NOT NULL, Balance INT NOT NULL DEFAULT 0, pLevel INT NOT NULL DEFAULT 1);");
        }catch(SQLException e){
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getServer().getPluginCommand("setrank").setExecutor(new RankCommand());
    }

    @Override
    public void onDisable(){

    }


    private void createConfig(){
        try{
            if(!getDataFolder().exists()){
                getDataFolder().mkdirs();
            }

            File file = new File(getDataFolder(), "config.yml");
            if(!file.exists()){
                getLogger().info(ChatColor.RED + "Config file doesn't appear to exist, creating one now!");
                saveDefaultConfig();
            }else{
                getLogger().info(ChatColor.GREEN + "Config file has been found, loading it now!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
