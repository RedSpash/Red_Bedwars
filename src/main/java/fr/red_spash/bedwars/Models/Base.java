package fr.red_spash.bedwars.Models;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import org.xml.sax.helpers.LocatorImpl;

import java.util.ArrayList;
import java.util.UUID;

public class Base {

    private Villager itemsShop;
    private Villager upgrades;
    private DyeColor teamColor;
    private ArrayList<UUID> playersUUID = new ArrayList<>();
    private Location bedLocation;
    private Location spawnLocation;
    private Forge forge;
    private Location itemGeneratorLocation;

    private int respawnTime = 5;



    public Base(Location itemGeneratorLocation, DyeColor teamColor){
        this.itemGeneratorLocation = itemGeneratorLocation;
        this.teamColor = teamColor;
        //speed true: 0.666666
        this.forge = new Forge(itemGeneratorLocation,1.0, BedWarsGame.MAX_ITEM_FORGE);
        BedWarsGame.bases.add(this);
    }

    public String getTeamName() {
        return Utils.getChatColorOf(this.teamColor)+Utils.upperCaseFirst(Utils.getColorName(this.teamColor));
    }

    public void setUpgradesVillager(Villager villager) {
        villager.setProfession(Villager.Profession.FARMER);
        this.upgrades = villager;
        Location location = villager.getLocation();
        location.setYaw(90);
        location.setPitch(90);
        location.add(0.5,0,0.5);
        Utils.setRotation(villager,90,0);
    }

    public void setItemsShopVillager(Villager villager) {
        villager.setProfession(Villager.Profession.FARMER);
        this.itemsShop = villager;
        Location location = villager.getLocation();
        villager.teleport(new Location(villager.getWorld(),0,100,0));
        Vector dirBetweenLocations = villager.getLocation().toVector().subtract(this.spawnLocation.clone().toVector()).multiply(-1);
        location.setDirection(dirBetweenLocations);
        location.add(0.5,0,0.5);
        villager.teleport(location);
        Utils.setAI(villager,false);
        Utils.setSilent(villager,true);
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getItemGeneratorLocation() {
        return itemGeneratorLocation.clone();
    }

    public void setBedLocation(Location bedLocation) {
        this.bedLocation = bedLocation;
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
                           block.setType(Material.BEDROCK);
                       }
                    }
                }
            }
        }
    }

    public void addPlayer(UUID uuid){
        playersUUID.add(uuid);
        Player p = Bukkit.getPlayer(uuid);
        if(BedWarsGame.playerBase.containsKey(uuid)){
            Base base = BedWarsGame.playerBase.get(uuid);
            if(base.playersUUID.contains(uuid)){
                base.removePlayer(uuid);
                base.sendMessage("§c"+p.getName()+" vient de quitter l'équipe !");
            }
            BedWarsGame.playerBase.remove(uuid);
        }
        this.sendMessage("§7"+p.getName()+" vient de rejoindre l'équipe "+this.getTeamName()+"§7!");
        BedWarsGame.playerBase.put(uuid,this);
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

    public void setDefaultInventory(Player p){
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.addItem(new ItemStack(Material.WOOD_SWORD));
        inv.setHelmet(Utils.getArmorColor(Material.LEATHER_HELMET,this.getColor()));
        inv.setBoots(Utils.getArmorColor(Material.LEATHER_BOOTS,this.getColor()));
        inv.setChestplate(Utils.getArmorColor(Material.LEATHER_CHESTPLATE,this.getColor()));
        inv.setLeggings(Utils.getArmorColor(Material.LEATHER_LEGGINGS,this.getColor()));

    }

    public boolean isFull() {
        return this.playersUUID.size() >= BedWarsGame.TEAM_SIZE;
    }

    public int getRespawnTime() {
        return respawnTime;
    }
}
