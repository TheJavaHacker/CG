package net.central_games.Core.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by TheTARDIS2176 on 09/08/2017.
 */
public class RankCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("setrank")) {
                if (p.hasPermission("centralcore.setrank")) {
                    Player target = Bukkit.getPlayerExact(strings[0]);
                    RanksManager.setRank(target, Ranks.valueOf(strings[1]));
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        if (pl.hasPermission("centralcore.staff")) {
                            pl.sendMessage("§cSTAFF §8» " + RanksManager.prefix.get(RanksManager.getRank(p)) + p.getName() + " §7set §e" + target.getName() + "'s§7 rank to " + RanksManager.prefix.get(RanksManager.getRank(target)));
                            RanksManager.applyPerms(target);
                            return true;
                        }
                        return true;
                    }
                    return true;
                } else {
                    p.sendMessage("§fUnknown command. Type /help for help.");
                    return true;
                }
            }
        }else{
            Player target = Bukkit.getPlayerExact(strings[0]);
            RanksManager.setRank(target, Ranks.valueOf(strings[1]));
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("centralcore.staff")) {
                    pl.sendMessage("§cSTAFF §8» §cCentral-Games §7set §e" + target.getName() + "'s§7 rank to " + RanksManager.prefix.get(RanksManager.getRank(target)));
                    RanksManager.applyPerms(target);
                    return true;
                }
                return true;
            }
            return true;
        }
        return true;
        }
    }