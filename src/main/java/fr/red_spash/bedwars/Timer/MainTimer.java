package fr.red_spash.bedwars.Timer;

import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.Models.ItemGenerator;
import org.bukkit.Bukkit;

public class MainTimer implements Runnable{
    @Override
    public void run() {
        Bukkit.broadcastMessage("d");
        for(ItemGenerator itemGenerator : Main.ItemGenerators){
            itemGenerator.updateArmorStands();
            itemGenerator.removeTimeNextItem(0.05);

        }
    }

}
