package fr.red_spash.bedwars.BedWarsCore;

import fr.red_spash.bedwars.Items.Items;
import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.Models.*;
import fr.red_spash.bedwars.utils.GameState;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class BedWarsGame implements Listener {

    public static final int TEAM_SIZE = 1;
    public static final int MAX_ITEM_FORGE = 150;
    public static final double EMERALD_GENERATOR_SPEED = 60.0;
    public static final double DIAMOND_GENERATOR_SPEED = 30.0;
    private static final int MAX_ITEM_DIAMOND = 7;
    private static final int MAX_ITEM_EMERALD = 3;

    public static ArrayList<Forge> Forges = new ArrayList<>();
    public static HashMap<UUID,BukkitTask> inRespawn = new HashMap<>();
    public static HashMap<UUID, DamageCaused> lastDamageCaused = new HashMap<>();
    private static BukkitTask decompte;
    public static World world;

    private static ArrayList<UUID> playerSpectator = new ArrayList<>();
    private static final ArrayList<DyeColor> allColor = new ArrayList<>(Arrays.asList(
            DyeColor.CYAN,
            DyeColor.RED,
            DyeColor.BLUE,
            DyeColor.PINK,
            DyeColor.GRAY,
            DyeColor.ORANGE,
            DyeColor.LIME,
            DyeColor.PURPLE,
            DyeColor.WHITE,
            DyeColor.YELLOW,
            DyeColor.GREEN));
    public static ArrayList<Base> bases = new ArrayList<>();
    public static HashMap<UUID,Base> playerBase = new HashMap<>();
    public static HashMap<UUID, Generator> itemSpawned = new HashMap<>();
    public static ArrayList<ItemGenerator> ItemGenerators = new ArrayList<>();
    public static GameState gameStat = GameState.Waiting;
    public static Location spawn = null;

    public static ArrayList<Location> blockBreakable = new ArrayList<>();

    @EventHandler
    public void PlayerBreakBlock(BlockBreakEvent e){
        if(!blockBreakable.contains(e.getBlock().getLocation())){
            if(!e.getPlayer().isOp() || e.getPlayer().getGameMode() == GameMode.SURVIVAL){
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cImpossible de casser le block !");
            }
        }
    }

    @EventHandler
    public void BlockPlace(BlockPlaceEvent e){
        if(!e.getPlayer().isOp() || e.getPlayer().getGameMode() == GameMode.SURVIVAL){
            blockBreakable.add(e.getBlock().getLocation());
        }
    }


    public static void startGame() {
        Bukkit.broadcastMessage("§e§lCréation en cours de la partie de Bedwars...");
        ArrayList<DyeColor> colors = (ArrayList<DyeColor>) allColor.clone();
        Path path = Paths.get("template");
        Path path2 = Paths.get("bedwars");
        File file = new File(path2.toString());
        Bukkit.broadcastMessage(file.getAbsolutePath());
        if(file.exists()){
            Bukkit.unloadWorld("bedwars",false);
            Bukkit.broadcastMessage("UNLOADED");
            Utils.deleteDirectory(file);
            Bukkit.broadcastMessage("DELETED");
            try {
                Bukkit.broadcastMessage("COPIED");
                Utils.copyDirectory(path.toString(), path2.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Bukkit.broadcastMessage("CREATION...");
        world = Bukkit.createWorld(new WorldCreator("bedwars"));
        if(world == null){
            world = Bukkit.getWorld("bedwars");
        }

        if(world != null){
            world.setGameRuleValue("mobGriefing","false");
            world.setGameRuleValue("doDaylightCycle","false");
            world.setGameRuleValue("doFireTick","false");
            world.setGameRuleValue("showDeathMessages","false");
            world.setGameRuleValue("doMobSpawning","false");

            ArrayList<Block> Beds = new ArrayList<>();

            for(int x = 150; x>= -150; x--){
                for(int y = 200; y>= 0; y--){
                    for(int z = 150; z>= -150; z--){

                        Block block = world.getBlockAt(new Location(world,x,y,z));
                        Block upperblock = world.getBlockAt(new Location(world,x,y+1,z));
                        if(upperblock.getType()== Material.AIR){
                            if(block.getType() == Material.EMERALD_BLOCK){
                                new ItemGenerator( new ItemStack(Material.EMERALD), Material.EMERALD_BLOCK, upperblock.getLocation().add(0.5,1,0.5), EMERALD_GENERATOR_SPEED, MAX_ITEM_EMERALD, "§2§lEmeraude");
                            }else if(block.getType() == Material.DIAMOND_BLOCK){
                                new ItemGenerator( new ItemStack(Material.DIAMOND), Material.DIAMOND_BLOCK, upperblock.getLocation().add(0.5,1,0.5), DIAMOND_GENERATOR_SPEED, MAX_ITEM_DIAMOND, "§b§lDiamants");
                            }
                        }
                        if(block.getType() == Material.COMMAND){
                            DyeColor color = colors.get(Utils.random_number(0,colors.size()-1));
                            colors.remove(color);
                            new Base(upperblock.getLocation().add(0.5,2,0.5), color);
                        } else if (block.getType() == Material.BED_BLOCK) {
                            boolean canAdd = true;
                            for(Block bed : Beds){
                                if(bed.getLocation().distance(block.getLocation())<= 3){
                                    canAdd = false;
                                }
                            }
                            if(canAdd){
                                Beds.add(block);
                            }
                        }
                    }
                }
            }

            for(Block bed : Beds){
                double distance = 1000.0;
                Base CloserBase = null;
                for(Base base : bases){
                    double calc = base.getItemGeneratorLocation().distance(bed.getLocation());
                    if(calc <= distance){
                        CloserBase = base;
                        distance = calc;
                    }
                }
                if(CloserBase != null){
                    CloserBase.setBedLocation(bed.getLocation());

                    Vector vector = bed.getLocation().add(0.5,0,0.5).subtract(CloserBase.getItemGeneratorLocation().add(0,0.5,0)).toVector();

                    if((int) vector.getX() > 1 ||  (int) vector.getX() < -1){
                        if(vector.getX()>1){
                            vector.setX(5);
                        }else{
                            vector.setX(-5);
                        }
                    } else if ((int) vector.getZ() > 1 ||  (int) vector.getZ() < -1) {
                        if(vector.getZ()>1){
                            vector.setZ(5);
                        }else{
                            vector.setZ(-5);
                        }
                    }
                    Location location = CloserBase.getItemGeneratorLocation().add(vector.add(new Vector(0,1,0))).setDirection(vector);
                    location.setPitch(0);
                    CloserBase.setSpawnLocation(location);
                }else{
                    Bukkit.broadcastMessage("§cErreur lors du chargement du lit en "+bed.getLocation().toString());
                }
            }
            for(Base base : bases){
                base.setBaseColor(true);
            }
            spawn = new Location(world,0.5,145,0.5,0,0);
            for(Player pl : Bukkit.getOnlinePlayers()){
                pl.teleport(spawn);
                pl.getInventory().clear();
                pl.getInventory().setItem(4, Items.getEquipeItem());
            }
        }

        BedWarsGame.checkCanStart();
    }

    public static void checkCanStart() {
        Bukkit.broadcastMessage(Bukkit.getWorld("bedwars").getPlayers().size()+"");
        if(Bukkit.getWorld("bedwars").getPlayers().size() >= (bases.size()*TEAM_SIZE)*0){
            startGameCount();
        }

    }

    private static void startGameCount() {
        if(decompte == null){
            decompte = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
                int i = 20;
                @Override
                public void run() {
                    if(i == 0){
                        Bukkit.broadcastMessage("§a§lLancement de la partie !");
                        decompte.cancel();
                        decompte = null;
                        gameStat = GameState.Started;

                        for(Player p : Bukkit.getWorld("bedwars").getPlayers()){
                            p.getEnderChest().clear();
                            Utils.sendTitle(p,"§a§lC'est parti !","§7Développé par Red_Spash#5918",0,40,20);
                            if(BedWarsGame.playerBase.containsKey(p.getUniqueId())){
                                Base base = playerBase.get(p.getUniqueId());
                                p.teleport(base.getSpawnLocation());
                                base.setDefaultInventory(p);
                                p.setGameMode(GameMode.SURVIVAL);
                            }else{
                                ArrayList<Base> base = (ArrayList<Base>) bases.clone();
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

                            }
                        }

                        for(ItemGenerator itemGenerator : BedWarsGame.ItemGenerators){
                            itemGenerator.startSpawn();
                        }

                        for(Forge forge : BedWarsGame.Forges){
                            forge.startSpawn();
                        }

                        int RADIUS = 15;
                        int i =0;
                        for(int x = RADIUS; x>= -RADIUS; x--) {
                            for (int y = RADIUS; y >= -RADIUS; y--) {
                                int finalX = x;
                                int finalY = y;
                                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int z = RADIUS; z >= -RADIUS; z--) {
                                            Block block = BedWarsGame.spawn.clone().add(finalX, z,finalY).getBlock();
                                            block.setType(Material.AIR);
                                        }
                                    }
                                },i);
                                i =i +1;
                            }
                        }
                        return;
                    }
                    if(i%10 == 0 || i%5 == 0  || i <= 10){
                        Bukkit.broadcastMessage("§eLancement de la partie dans §6"+i+" seconde(s)§e !");
                        boolean title = false;
                        if(i<=5){
                            title = true;
                        }
                        for(Player pl : Bukkit.getOnlinePlayers()){
                            pl.playSound(pl.getLocation(), Sound.CLICK,1,1);
                            if(title){
                                Utils.sendTitle(pl,"§c§l"+i,"§e§lPréparez vous !",0,22,0);
                            }
                        }
                    }
                    i = i -1;
                }
            },1,20);
        }

    }

    public static void addSpectator(UUID uniqueId) {
        playerSpectator.add(uniqueId);
        Player p = Bukkit.getPlayer(uniqueId);
        p.sendMessage("§cVous passez en spectateur !");
    }
}
