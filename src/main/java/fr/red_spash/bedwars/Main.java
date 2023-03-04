package fr.red_spash.bedwars;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Commandes.testcommande;
import fr.red_spash.bedwars.Scoreboard.ScoreboardManager;
import fr.red_spash.bedwars.Shop.Shop;
import fr.red_spash.bedwars.Shop.ShopEvent;
import fr.red_spash.bedwars.Timer.UpdateTimers;
import fr.red_spash.bedwars.listeners.*;
import fr.red_spash.bedwars.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class Main extends JavaPlugin {
    public static final String START_GAME_NAME ="§e§lLancer une partie" ;
    public static HashMap<Item, Long> cooldownItemStackable = new HashMap<>();
    public static final Location SPAWN_LOCATION = new Location(Bukkit.getWorld("world"),0,100,0,-180,0);
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        Shop.initShopsCategories();
        getCommand("test").setExecutor(new testcommande());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new UpdateTimers(), 0,20);

        Bukkit.getPluginManager().registerEvents(new EventListener(),this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(),this);
        Bukkit.getPluginManager().registerEvents(new SpawnListener(),this);
        Bukkit.getPluginManager().registerEvents(new BedWarsGame(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ShopEvent(), this);


        WorldCreator wc = new WorldCreator("world");
        wc.type(WorldType.FLAT);
        wc.generatorSettings("2;0;1;");
        wc.generateStructures(false);
        World spawn = wc.createWorld();
        spawn.setGameRuleValue("mobGriefing","false");
        spawn.setGameRuleValue("doDaylightCycle","false");
        spawn.setGameRuleValue("doFireTick","false");
        spawn.setGameRuleValue("showDeathMessages","false");
        spawn.setGameRuleValue("doMobSpawning","false");

        Location villagerStart = new Location(Bukkit.getWorld("world"),0.5,98.0,-29.5,0,0);
        boolean spawned = false;
        for(Entity entity : villagerStart.getWorld().getNearbyEntities(villagerStart,0.5,0.5,0.5)){
            if(entity instanceof Villager){
                spawned = true;
            }
        }
        if(!spawned){
            Villager villager = (Villager) villagerStart.getWorld().spawnEntity(villagerStart, EntityType.VILLAGER);
            villager.teleport(villagerStart);
            villager.setCustomName(START_GAME_NAME);
            villager.setCustomNameVisible(true);
            villager.setProfession(Villager.Profession.LIBRARIAN);
            villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,10000*20,1000,false,false));
            Utils.setAI((LivingEntity) villager,false);
            Utils.setSilent(villager,true);

        }
        ScoreboardManager.initTimer();
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            double min = 10000;
            double actual;
            @Override
            public void run() {
                actual = MinecraftServer.getServer().recentTps[0];
                if(actual < min){
                    min = actual;
                }
                for(Player p : Bukkit.getOnlinePlayers()){
                    if(p.isOp()){
                        Utils.sendActionText(p,"§a"+actual+" §c- "+min);
                    }
                }
            }
        },0,10);


    }



    @Override
    public void onDisable() {
        for(Player p : BedWarsGame.world.getPlayers()){
            p.teleport(SPAWN_LOCATION);
        }
    }

}
