package fr.red_spash.bedwars.Commandes;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Items.Items;
import fr.red_spash.bedwars.Shop.ItemCategorie;
import fr.red_spash.bedwars.Shop.ShopEvent;
import fr.red_spash.bedwars.listeners.ItemsListener;
import fr.red_spash.bedwars.listeners.PlayerListener;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class testcommande implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length >= 1){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                switch (strings[0].toLowerCase()){
                    case "shop" :{
                        ShopEvent.openShopInventory(p, ItemCategorie.BLOCS);
                        break;
                    }
                    case "armor":{
                        BedWarsGame.playersDatas.get(p.getUniqueId()).updateArmor();
                        break;
                    }
                    case "tool":{
                        BedWarsGame.playersDatas.get(p.getUniqueId()).setDefaultInventory();
                        break;
                    }
                    case "all":{
                        BedWarsGame.playersDatas.get(p.getUniqueId()).resetInventory();
                        break;
                    }case "fireball":{
                        ItemsListener.FIREBALL_POWER = Double.parseDouble(strings[1]);
                        Bukkit.broadcastMessage(ItemsListener.FIREBALL_POWER+"");
                        break;
                    }
                    default:
                        p.teleport(Bukkit.getWorld(strings[0]).getSpawnLocation());
                        break;
                }
            }
            return true;
        }

        commandSender.sendMessage("§c§lNOT");

        return false;
    }
}
