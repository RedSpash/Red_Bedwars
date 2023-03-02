package fr.red_spash.bedwars.BedWarsCore;

import fr.red_spash.bedwars.Items.Items;
import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.Models.Base;
import fr.red_spash.bedwars.Models.ItemGenerator;
import fr.red_spash.bedwars.utils.GameState;
import fr.red_spash.bedwars.utils.Utils;
import net.minecraft.server.v1_8_R3.IBlockAccess;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class BedWarsGame implements Listener {

    public static final int TEAM_SIZE = 1;
    private static final double TIME_IRON_INGOT = 0.61;
    private static final double TIME_GOLD_INGOT = 3.0;
    private static BukkitTask decompte;

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
    public static ArrayList<ItemGenerator> ItemGenerators = new ArrayList<>();
    public static GameState gameStat = GameState.Waiting;
    public static Location spawn = null;

    public static ArrayList<Location> blockNotBreakable = new ArrayList<>();

    @EventHandler
    public void PlayerBreakBlock(BlockBreakEvent e){
        if(blockNotBreakable.contains(e.getBlock().getLocation())){
            if(!e.getPlayer().isOp() || e.getPlayer().getGameMode() == GameMode.SURVIVAL){
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cImpossible de casser le block !");
            }
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
        World world = Bukkit.createWorld(new WorldCreator("bedwars"));
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
                        blockNotBreakable.add(block.getLocation());
                        Block upperblock = world.getBlockAt(new Location(world,x,y+1,z));
                        if(upperblock.getType()== Material.AIR){
                            if(block.getType() == Material.EMERALD_BLOCK){
                                new ItemGenerator( new ItemStack(Material.EMERALD), Material.EMERALD_BLOCK, upperblock.getLocation().add(0.5,1,0.5), 60.0, false, 3, "§2§lEmeraude");
                            }else if(block.getType() == Material.DIAMOND_BLOCK){
                                new ItemGenerator( new ItemStack(Material.DIAMOND), Material.DIAMOND_BLOCK, upperblock.getLocation().add(0.5,1,0.5), 30.0, false, 5, "§b§lDiamants");
                            }
                        }
                        if(block.getType() == Material.COMMAND){
                            ItemGenerator ironGenerator = new ItemGenerator(new ItemStack(Material.IRON_INGOT), null, upperblock.getLocation().add(0.5,2,0.5), TIME_IRON_INGOT, true, 150, "BaseX");
                            ItemGenerator goldGenerator = new ItemGenerator(new ItemStack(Material.GOLD_INGOT), null, upperblock.getLocation().add(0.5,2,0.5), TIME_GOLD_INGOT, true, 150, "BaseX");
                            DyeColor color = colors.get(Utils.random_number(0,colors.size()-1));
                            colors.remove(color);
                            Base base = new Base(upperblock.getLocation().add(0.5,2,0.5), color, new ArrayList<>(Arrays.asList(ironGenerator, goldGenerator)));
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
                    Vector vector = CloserBase.getItemGeneratorLocation().subtract(bed.getLocation()).toVector();
                    if((int) vector.getX() > 1 ||  (int) vector.getX() < -1){
                        vector.setX(5);
                    } else if ((int) vector.getZ() > 1 ||  (int) vector.getZ() < -1) {
                        vector.setZ(5);
                    }
                    CloserBase.setSpawnLocation(CloserBase.getItemGeneratorLocation().add(vector.add(new Vector(0.5,1,0.5))).setDirection(vector));
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
                int i = 5;
                @Override
                public void run() {
                    if(i == 0){
                        Bukkit.broadcastMessage("§a§lLancement de la partie !");
                        decompte.cancel();
                        decompte = null;
                        gameStat = GameState.Started;

                        for(Player p : Bukkit.getWorld("bedwars").getPlayers()){
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
                        for(Player pl : Bukkit.getOnlinePlayers()){
                            pl.playSound(pl.getLocation(), Sound.CLICK,1,1);
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
