package fr.red_spash.bedwars.BedWarsCore;

import fr.red_spash.bedwars.Items.Items;
import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.Models.*;
import fr.red_spash.bedwars.Shop.Applications.ItemShop;
import fr.red_spash.bedwars.Shop.Applications.UpgradableItem;
import fr.red_spash.bedwars.Shop.ItemCategorie;
import fr.red_spash.bedwars.Shop.Shop;
import fr.red_spash.bedwars.utils.GameState;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    public static ArrayList<UUID> playerSpectator = new ArrayList<>();
    private static final ArrayList<DyeColor> allColor = new ArrayList<>(Arrays.asList(
            DyeColor.CYAN,
            DyeColor.RED,
            DyeColor.BLUE,
            DyeColor.PINK,
            DyeColor.GRAY,
            DyeColor.ORANGE,
            DyeColor.LIME,
            DyeColor.MAGENTA,
            DyeColor.LIGHT_BLUE,
            DyeColor.YELLOW,
            DyeColor.GREEN));
    public static ArrayList<Base> bases = new ArrayList<>();
    public static HashMap<UUID,PlayerData> playersDatas = new HashMap<>();
    public static HashMap<UUID, Generator> itemSpawned = new HashMap<>();
    public static ArrayList<ItemGenerator> ItemGenerators = new ArrayList<>();
    public static GameState gameStat = GameState.Waiting;
    public static Location spawn = null;

    public static ArrayList<Location> blockBreakable = new ArrayList<>();
    public static ArrayList<Location> locationNotPlaceable = new ArrayList<>();


    public static void startGame() {
        if (Bukkit.getWorld("bedwars") != null) {
            for(Player p : Bukkit.getWorld("bedwars").getPlayers()){
                p.teleport(Main.SPAWN_LOCATION);
            }
        }

        for(Player pl : Bukkit.getOnlinePlayers()){
            pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100000*20,5,false,false));
            pl.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,100000*20,250,false,false));
            pl.setSprinting(false);
            pl.setWalkSpeed(0);
        }
        Bukkit.broadcastMessage("§e§lCréation en cours de la partie de Bedwars...");
        ArrayList<DyeColor> colors = (ArrayList<DyeColor>) allColor.clone();
        deleteWorld();
        Bukkit.broadcastMessage("§aCréation du monde...");
        world = Bukkit.createWorld(new WorldCreator("bedwars"));
        if(world == null){
            world = Bukkit.getWorld("bedwars");
        }

        if(world != null){
            Bukkit.broadcastMessage("§aScan en cours du monde...");
            world.setGameRuleValue("mobGriefing","true");
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
                        } else if (block.getType() == Material.REDSTONE_BLOCK) {
                            if(upperblock.getLocation().add(0,1,0).getBlock().getType() == Material.AIR){
                                Villager villager = (Villager) world.spawnEntity(upperblock.getLocation().add(0,1,0).getBlock().getLocation(), EntityType.VILLAGER);
                                Utils.setSilent(villager,true);
                                villager.setCustomNameVisible(true);
                                villager.setCustomName("§6§lItems");

                            }
                        }else if (block.getType() == Material.LAPIS_BLOCK) {
                            if(upperblock.getLocation().add(0,1,0).getBlock().getType() == Material.AIR){
                                Villager villager = (Villager) world.spawnEntity(upperblock.getLocation().add(0,1,0).getBlock().getLocation(), EntityType.VILLAGER);
                                Utils.setSilent(villager,true);
                                villager.setCustomNameVisible(true);
                                villager.setCustomName("§6§lAméliorations");
                            }
                        }
                    }
                }
            }
            Bukkit.broadcastMessage("§aScan terminé : "+bases.size()+" bases trouvées !");
            Bukkit.broadcastMessage("§aPositionnement des points de réapparitions...");
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
                    for(Entity entity : location.getWorld().getNearbyEntities(location,10,5,10)){
                        if(entity instanceof Villager){
                            Villager villager = (Villager) entity;
                            if(villager.getCustomName() != null){
                                if(villager.getCustomName().equalsIgnoreCase("§6§lAméliorations")){
                                    CloserBase.setUpgradesVillager(villager);
                                } else if (villager.getCustomName().equalsIgnoreCase("§6§lItems")) {
                                    CloserBase.setItemsShopVillager(villager);
                                }
                            }
                        }
                    }
                }else{
                    Bukkit.broadcastMessage("§cErreur lors du chargement du lit en "+bed.getLocation().toString());
                }
            }
            Bukkit.broadcastMessage("§aTéléportation des joueurs ...");
            for(Base base : bases){
                base.setBaseColor(true);
            }
            spawn = new Location(world,0.5,145,0.5,0,0);
            for(Player pl : Bukkit.getOnlinePlayers()){
                pl.teleport(spawn);
                pl.getInventory().clear();
                pl.getEquipment().clear();
                pl.getInventory().setItem(4, Items.getEquipeItem());
                Utils.clearArmor(pl);
                for(PotionEffect potionEffect : pl.getActivePotionEffects()){
                    pl.removePotionEffect(potionEffect.getType());
                }
                pl.setWalkSpeed(0.2F);
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
                int i = 3;
                @Override
                public void run() {
                    List<Player> allplayers = (List<Player>) Bukkit.getOnlinePlayers();
                    if(allplayers.size() > 0){
                        Player firstplayer = allplayers.get(0);
                        if(firstplayer.getWorld().getName().equalsIgnoreCase("bedwars")){
                            if(i == 0){
                                Bukkit.broadcastMessage("§a§lLancement de la partie !");
                                decompte.cancel();
                                decompte = null;
                                gameStat = GameState.Started;

                                for(Player p : Bukkit.getWorld("bedwars").getPlayers()){
                                    p.getEnderChest().clear();
                                    Utils.sendTitle(p,"§a§lC'est parti !","§7Développé par Red_Spash#5918",0,40,20);
                                    if(BedWarsGame.playersDatas.get(p.getUniqueId()).getBase() != null){
                                        PlayerData playerData = playersDatas.get(p.getUniqueId());
                                        p.teleport(playerData.getBase().getSpawnLocation());
                                        playerData.setDefaultInventory();
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
                                            p.setGameMode(GameMode.SURVIVAL);
                                            playersDatas.get(p.getUniqueId()).setBase(temp);
                                            playersDatas.get(p.getUniqueId()).setDefaultInventory();
                                        }
                                    }
                                }
                                for(ItemGenerator itemGenerator : BedWarsGame.ItemGenerators){
                                    itemGenerator.startSpawn();
                                }

                                for(Forge forge : BedWarsGame.Forges){
                                    forge.startSpawn();
                                }

                                for(Base base : bases){
                                    if(base.getPlayersUUID().size() == 0){
                                        base.setBaseColor(false);
                                        base.removeBed();
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
                                                    if(BedWarsGame.spawn != null){
                                                        Block block = BedWarsGame.spawn.clone().add(finalX, z,finalY).getBlock();
                                                        block.setType(Material.AIR);
                                                    }
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
                    }

                }

            },1,20);
        }

    }

    public static void addSpectator(UUID uniqueId) {
        Player p = Bukkit.getPlayer(uniqueId);
        if (!playerSpectator.contains(uniqueId)) {
            playerSpectator.add(uniqueId);
            p.sendMessage("§cVous passez en spectateur !");
        }
        for(PotionEffect effect : p.getActivePotionEffects()){
            p.removePotionEffect(effect.getType());
        }
        p.setGameMode(GameMode.SPECTATOR);
        for(Player pl : Bukkit.getOnlinePlayers()){
            if(!pl.getName().equalsIgnoreCase(p.getName())){
                pl.hidePlayer(p);
            }
        }
        checkIsEnd();
    }

    public static boolean checkIsEnd(){
        if(gameStat != GameState.Started){
            return false;
        }
        int nbr = 0;
        Base victoryBase = null;
        for(Base base : bases){
            for(UUID uuid : base.getPlayersUUID()){
                if(!playersDatas.get(uuid).isDead()){
                    nbr = nbr +1;
                    victoryBase = base;
                    break;
                }
            }
            if(nbr >= 2){
                return false;
            }
        }

        for(Player p : Bukkit.getOnlinePlayers()){
           if(victoryBase != null) {
               if(victoryBase.getPlayersUUID().contains(p.getUniqueId())){
                   Utils.sendTitle(p,"§6§lVICTOIRE","§6Vous avez gagné la partie !",0,20*15,0);
               } else if (playersDatas.get(p.getUniqueId()).getBase() != null) {
                   Utils.sendTitle(p,"§c§lDEFAITE","§a§lL'équipe "+victoryBase.getTeamName()+"§a§l gagnent la partie !",0,20*15,20*2);
               }else{
                   Utils.sendTitle(p,"§c§lGAME OVER","§a§lL'équipe "+victoryBase.getTeamName()+"§a§l gagnent la partie !",0,20*15,20*2);
               }
           }else{
               Utils.sendTitle(p,"§c§lGAME OVER","§6§légalité, personne ne gagne.",0,20*10,20*2);
           }
           Utils.clearArmor(p);
            p.getInventory().clear();

            p.playSound(p.getLocation(),Sound.ENDERDRAGON_DEATH,1000,1);
            for(Player pl : Bukkit.getOnlinePlayers()){
                p.showPlayer(pl);
            }
            p.setGameMode(GameMode.SURVIVAL);
            p.setAllowFlight(true);
            p.setFlying(true);
        }
        if(victoryBase != null){
            Bukkit.broadcastMessage("§6\n§a§lLa partie est remportée par l'équipe "+victoryBase.getTeamName()+" §a§l!\n§f§o §f\n§f");
        }else{
            Bukkit.broadcastMessage("§6\n§a§lLa partie se termine sur une égalité !\n§f§o §f\n§f");
        }
        gameStat = GameState.Finish;
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                resetGame();
            }
        },20*20);
        return true;
    }

    private static void resetGame() {
        for(Player pl : Bukkit.getOnlinePlayers()){
            pl.teleport(Main.SPAWN_LOCATION);
        }
        Bukkit.broadcastMessage("§c§lFin du jeu !");

        for(BukkitTask bukkitTask : inRespawn.values()){
            bukkitTask.cancel();
        }
        inRespawn.clear();
        lastDamageCaused.clear();
        for(Player p : Bukkit.getOnlinePlayers()){
            playersDatas.put(p.getUniqueId(),new PlayerData(p.getUniqueId(),null));
            for(Player pl : Bukkit.getOnlinePlayers()){
                p.showPlayer(pl);
            }
            for(PotionEffect potionEffect : p.getActivePotionEffects()){
                p.removePotionEffect(potionEffect.getType());
            }
            p.setFlying(false);
            p.setAllowFlight(false);
        }
        itemSpawned.clear();
        for(ItemGenerator itemGenerator : ItemGenerators){
            itemGenerator.delete();
        }
        for(Forge forge : Forges){
            forge.delete();
        }

        for(ItemCategorie itemCategorie : Shop.itemShop.keySet()){
            for(ItemShop itemShop : Shop.itemShop.get(itemCategorie)){
                if(itemShop instanceof UpgradableItem){
                    UpgradableItem upgradableItem = (UpgradableItem) itemShop;
                    upgradableItem.resetPlayerData();
                }
            }
        }

        playerSpectator.clear();
        ItemGenerators.clear();
        spawn = null;
        blockBreakable.clear();
        bases.clear();
        locationNotPlaceable.clear();
        Main.cooldownItemStackable.clear();
        gameStat = GameState.Waiting;
        deleteWorld();

    }

    public static void deleteWorld() {
        Bukkit.getWorld("world").getWorldFolder().delete();
        Bukkit.unloadWorld("bedwars",false);

        Path path = Paths.get("template");
        Path path2 = Paths.get("bedwars");
        File file = new File(path2.toString());
        if(file.exists()){
            Bukkit.unloadWorld("bedwars",false);
            Utils.deleteDirectory(file);
        }
        try {
            Utils.copyDirectory(path.toString(), path2.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void protectZone(Location location, int size){
        for(int x = -size; x <= size; x++){
            for(int y = -size; y <= size; y++){
                for(int z = -size; z <= size; z++){
                    locationNotPlaceable.add(new Location(location.getWorld(),location.getBlockX()+x,location.getBlockY()+y,location.getBlockZ()+z));
                }
            }
        }

    }

    @EventHandler
    public void blockexplode(EntityExplodeEvent e){
        e.setCancelled(true);
        for(Block block : e.blockList()){
            if(blockBreakable.contains(new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()))){
                if(Utils.random_number(0,2) == 0){
                    block.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                }
                blockBreakable.remove(new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()));
                block.setType(Material.AIR);
            }
        }
    }
    @EventHandler
    public void blockexplode(BlockExplodeEvent e){
        e.setCancelled(true);
        for(Block block : e.blockList()){
            if(blockBreakable.contains(new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()))){
                if(Utils.random_number(0,2) == 0){
                    block.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                }
                blockBreakable.remove(new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()));
                block.setType(Material.AIR);
            }
        }
    }
}
