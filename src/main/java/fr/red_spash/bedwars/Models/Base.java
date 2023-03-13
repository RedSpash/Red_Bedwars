package fr.red_spash.bedwars.Models;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class Base {

    private Villager itemsShopVillager;
    private Villager upgradesVillager;
    private DyeColor teamColor;
    private ArrayList<UUID> playersUUID = new ArrayList<>();
    private Location bedLocation;
    private Location spawnLocation;
    private boolean asBed = true;
    private BaseUpgrade upgrades;
    private Forge forge;
    private Location itemGeneratorLocation;

    private int respawnTime = 5;



    public Base(Location itemGeneratorLocation, DyeColor teamColor){
        this.itemGeneratorLocation = itemGeneratorLocation;
        this.teamColor = teamColor;
        //speed true: 0.666666
        this.forge = new Forge(itemGeneratorLocation,1.0, BedWarsGame.MAX_ITEM_FORGE);
        BedWarsGame.bases.add(this);
        this.upgrades = new BaseUpgrade();
    }

    private void focusBedLocation(){
        if(this.itemGeneratorLocation != null){
            Vector vector = this.itemGeneratorLocation.clone().subtract(this.bedLocation.clone().add(0.5,0,0.5)).toVector();
            if((int) vector.getX() > 1 ||  (int) vector.getX() < -1){
                if(vector.getX()>1){
                    vector.setX(1);
                }else{
                    vector.setX(-1);
                }
            } else if ((int) vector.getZ() > 1 ||  (int) vector.getZ() < -1) {
                if(vector.getZ()>1){
                    vector.setZ(1);
                }else{
                    vector.setZ(-1);
                }
            }
            if(this.bedLocation.clone().add(vector).getBlock().getType() == Material.BED_BLOCK){
                this.bedLocation = this.bedLocation.clone().add(vector);
            }
        }
    }

    public boolean asBed() {
        return asBed;
    }

    public void setAsBed(boolean asBed) {
        this.asBed = asBed;
        if(!asBed){
            for(UUID uuid : this.playersUUID){
                Player p = Bukkit.getPlayer(uuid);
                Utils.sendTitle(p,"§c⚠ §c§lLit détruit ! §c⚠","§aVous ne pouvez plus réapparaitre !",0,20*5,20);
                p.playSound(p.getLocation(), Sound.WITHER_SPAWN,1,1);
            }
        }
    }

    public String getTeamName() {
        return Utils.getChatColorOf(this.teamColor)+Utils.upperCaseFirst(Utils.getColorName(this.teamColor));
    }

    public boolean isMyBed(Block block){
        return block.getType().equals(Material.BED_BLOCK) && block.getLocation().distance(this.bedLocation) <= 1;
    }

    public void setUpgradesVillager(Villager villager) {
        villager.setProfession(Villager.Profession.FARMER);
        this.upgradesVillager = villager;
        Location location = villager.getLocation();
        Vector dirBetweenLocations = villager.getLocation().toVector().subtract(this.spawnLocation.clone().toVector());
        location.setDirection(dirBetweenLocations);
        location.add(0.5,0,0.5);
        villager.teleport(new Location(villager.getWorld(),0,100,0,location.getYaw(),location.getPitch()));
        villager.teleport(location);
        Utils.setAI(villager,false);
        Utils.setSilent(villager,true);
        villager.teleport(location);
        BedWarsGame.protectZone(villager.getLocation(),2);
    }

    public void setItemsShopVillager(Villager villager) {
        villager.setProfession(Villager.Profession.FARMER);
        this.itemsShopVillager = villager;
        Location location = villager.getLocation();
        villager.teleport(new Location(villager.getWorld(),0,100,0));
        Vector dirBetweenLocations = villager.getLocation().toVector().subtract(this.spawnLocation.clone().toVector()).multiply(-1);
        location.setDirection(dirBetweenLocations);
        location.add(0.5,0,0.5);
        villager.teleport(location);
        Utils.setAI(villager,false);
        Utils.setSilent(villager,true);
        villager.teleport(location);
        BedWarsGame.protectZone(villager.getLocation(),2);
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        BedWarsGame.protectZone(spawnLocation,3);
    }

    public Location getItemGeneratorLocation() {
        return itemGeneratorLocation.clone();
    }

    public void setBedLocation(Location bedLocation) {
        this.bedLocation = bedLocation;
        this.focusBedLocation();
    }

    public void setBaseColor(Boolean teamColor){
        int RADIUS = 25;
        for(int x = RADIUS; x>= -RADIUS; x--) {
            for (int y = RADIUS; y >= -RADIUS; y--) {
                for (int z = RADIUS; z >= -RADIUS; z--) {
                    Block block = this.bedLocation.clone().add(x,y,z).getBlock();

                    if(block.getType() == Material.WOOL){
                       if(teamColor){
                           block.setData(this.getColor().getWoolData());
                       }else{
                           block.setData(DyeColor.WHITE.getWoolData());
                       }
                    }
                }
            }
        }
    }

    public void addPlayer(UUID uuid){
        playersUUID.add(uuid);
        Player p = Bukkit.getPlayer(uuid);
        if(BedWarsGame.playersDatas.containsKey(uuid)){
            Base base = BedWarsGame.playersDatas.get(uuid).getBase();
            if(base != null){
                base.removePlayer(uuid);
                base.sendMessage("§c"+p.getName()+" vient de quitter l'équipe !");
            }
            BedWarsGame.playersDatas.get(p.getUniqueId()).setBase(null);
        }
        this.sendMessage("§7"+p.getName()+" vient de rejoindre l'équipe "+this.getTeamName()+"§7!");
        BedWarsGame.playersDatas.get(p.getUniqueId()).setBase(this);
    }

    public void sendMessage(String text){
        for(UUID uuid : this.playersUUID){
            Player p = Bukkit.getPlayer(uuid);
            if(p != null){
                p.sendMessage(text);
            }
        }
    }

    private void removePlayer(UUID uuid) {
        this.playersUUID.remove(uuid);
    }

    public void addPlayer(Player p){
        this.addPlayer(p.getUniqueId());
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public DyeColor getColor() {
        return teamColor;
    }

    public ArrayList<UUID> getPlayersUUID() {
        return this.playersUUID;
    }

    public boolean isFull() {
        return this.playersUUID.size() >= BedWarsGame.TEAM_SIZE;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public boolean canRespawn() {
        return true;
    }

    public void removeBed() {
        for(int x = 1; x>= -1; x--) {
            for (int y = 1; y >= -1; y--) {
                for (int z = 1; z >= -1; z--) {
                    Block block = this.bedLocation.clone().add(x,y,z).getBlock();
                    if(block.getType() == Material.BED_BLOCK){
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        for(Entity entity : this.bedLocation.getWorld().getNearbyEntities(this.bedLocation,1,1,1)){
            if(entity instanceof Item){
                entity.remove();
            }
        }
    }
}
