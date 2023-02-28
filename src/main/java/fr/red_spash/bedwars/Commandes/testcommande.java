package fr.red_spash.bedwars.Commandes;

import fr.red_spash.bedwars.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class testcommande implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Main.itemGenerator.setSpeed(2.0);
        return false;
    }
}
