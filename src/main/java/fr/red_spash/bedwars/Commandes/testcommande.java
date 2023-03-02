package fr.red_spash.bedwars.Commandes;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Items.Items;
import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.Models.Base;
import fr.red_spash.bedwars.Models.ItemGenerator;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class testcommande implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 1){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(strings[0].equalsIgnoreCase("base")){
                    ArrayList<Base> base = (ArrayList<Base>) BedWarsGame.bases.clone();
                    Base temp = base.get(Utils.random_number(0,base.size()-1));
                    while(temp.isFull() && base.size() > 1){
                        base.remove(temp);
                        temp = base.get(Utils.random_number(0,base.size()-1));
                    }
                    if(temp.isFull()){
                        BedWarsGame.addSpectator(p.getUniqueId());
                    }else{
                        temp.addPlayer(p.getUniqueId());
                        p.teleport(temp.getSpawnLocation());
                        temp.setDefaultInventory(p);
                        p.setGameMode(GameMode.SURVIVAL);
                    }
                }else{
                    p.teleport(Bukkit.getWorld(strings[0]).getSpawnLocation());
                }
            }
            return true;
        }

        commandSender.sendMessage("§c§lNOT");

        return false;
    }
}
