package fr.red_spash.bedwars.listeners;

import fr.red_spash.bedwars.Items.Items;
import fr.red_spash.bedwars.Models.Base;
import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Models.Generator;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.UUID;

public class InventoryListener implements Listener {

    public static final String ITEM_TEAM_NAME = "§6§lÉquipes";

    @EventHandler
    public void ItemClickEvent(InventoryClickEvent e){
        if(e.getView() != null){
            if (e.getView().getTitle().equalsIgnoreCase(ITEM_TEAM_NAME)) {
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if(item != null && e.getWhoClicked() instanceof Player){
                    Player p = (Player) e.getWhoClicked();
                    if(item.getType() == Material.WOOL){
                        Wool wool = (Wool)item.getData();
                        DyeColor dyeColor = wool.getColor();
                        for(Base base : BedWarsGame.bases){
                            if(base.getColor() == dyeColor){
                                if(base.getPlayersUUID().size() < BedWarsGame.TEAM_SIZE){
                                    base.addPlayer(p.getUniqueId());
                                    BedWarsGame.playersDatas.get(p.getUniqueId()).setBase(base);
                                    break;
                                }else{
                                    p.sendMessage("§cIl n'y a plus de place dans l'équipe !");
                                    p.playSound(p.getLocation(), Sound.VILLAGER_NO,1,1);
                                }
                                break;
                            }
                        }
                        for(Player pl : Bukkit.getOnlinePlayers()){
                            if(pl.getOpenInventory() != null){
                                if(pl.getOpenInventory().getTitle() != null){
                                    if(pl.getOpenInventory().getTitle().equalsIgnoreCase(ITEM_TEAM_NAME)){
                                        int i =0;
                                        for(ItemStack items : openTeamMenu()){
                                            pl.getOpenInventory().setItem(i,items);
                                            i = i +1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void interactItemEvent(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR ||
                e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(e.getItem() != null){
                if(e.getItem().equals(Items.getEquipeItem())){
                    e.setCancelled(true);
                    e.getPlayer().openInventory(openTeamMenu());
                }
            }
        }
    }

    private Inventory openTeamMenu() {
        double taille = BedWarsGame.bases.size()/9.0;
        int size = 9;
        if(taille > 9){
            size = 9*2;
        }

        Inventory inv = Bukkit.createInventory(null,size,ITEM_TEAM_NAME);
        int i = 0;
        for(Base base : BedWarsGame.bases){
            Wool wool = new Wool(base.getColor());
            ItemStack item = wool.toItemStack();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Utils.getChatColorOf(base.getColor())+Utils.upperCaseFirst(Utils.getColorName(base.getColor()))+"§7 - "+base.getPlayersUUID().size()+"/"+ BedWarsGame.TEAM_SIZE);
            ArrayList<String> lore = new ArrayList<>();
            for(UUID uuid : base.getPlayersUUID()){
                Player p = Bukkit.getPlayer(uuid);
                if(p != null){
                    lore.add("§7- "+p.getName());
                }
            }
            item.setAmount(lore.size());
            if(lore.size() == BedWarsGame.TEAM_SIZE){
                itemMeta.addEnchant(Enchantment.DURABILITY,1,true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }else{
                while(lore.size() < BedWarsGame.TEAM_SIZE){
                    lore.add("§f- Emplacement libre");
                }
            }
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            inv.setItem(i,item);
            i = i +1;
        }
        return inv;
    }

    @EventHandler
    public void ItemPickup(PlayerPickupItemEvent e){
        Item item = e.getItem();
        UUID uuid = item.getUniqueId();
        Player p = e.getPlayer();
        if(BedWarsGame.inRespawn.containsKey(p.getUniqueId()) || BedWarsGame.playerSpectator.contains(p.getUniqueId())){
            if(p.getGameMode() != GameMode.CREATIVE){
                e.setCancelled(true);
                return;
            }
        }
        if(BedWarsGame.itemSpawned.containsKey(uuid)){
            Generator generator = BedWarsGame.itemSpawned.get(uuid);
            generator.itemPickup(item.getItemStack().getAmount());
            BedWarsGame.itemSpawned.remove(uuid);

        }
    }

    @EventHandler
    public void ItemDispawn(ItemDespawnEvent e){
        e.setCancelled(true);
    }

}
