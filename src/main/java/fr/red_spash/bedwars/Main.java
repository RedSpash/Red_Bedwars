package fr.red_spash.bedwars;

import fr.red_spash.bedwars.Commandes.testcommande;
import fr.red_spash.bedwars.Models.ItemGenerator;
import fr.red_spash.bedwars.Timer.MainTimer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    public static ItemGenerator itemGenerator;
    public static ArrayList<ItemGenerator> ItemGenerators = new ArrayList<>();
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        itemGenerator = new ItemGenerator(new ItemStack(Material.DIRT),new Location(Bukkit.getWorld("world"),0,100,0),10.0);
        getCommand("test").setExecutor(new testcommande());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new MainTimer(), 0,1);

    }

    @Override
    public void onDisable() {
        //TODO
    }

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    public static double arrondir(Double x){
        double roundDbl = Math.round(x*10.0)/10.0;
        return roundDbl;
    }
}
