package fr.red_spash.bedwars.Shop;

import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ShopEvent implements Listener {

    @EventHandler
    public void InteractVillager(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof Villager){
            Villager villager = (Villager) e.getRightClicked();
            if(villager.getCustomName() != null){
                if(villager.getCustomName().equalsIgnoreCase("§6§lItems")){
                    e.setCancelled(true);
                    openShopInventory(e.getPlayer(),ItemCategorie.BLOC);
                }
            }
        }
    }

    public static void openShopInventory(Player player, ItemCategorie categorie) {
        Inventory inv = Bukkit.createInventory(null,9*6,"Boutique d'objets");
        int i =1;
        for(ItemCategorie itemCategorie : Shop.itemShop.keySet()){
            ItemStack itemStack = getBlockCategorie(itemCategorie);
            inv.setItem(i,itemStack);
            if(categorie == itemCategorie){
                inv.setItem(i+9, Utils.removeDataCategorieGlassPane(Material.STAINED_GLASS_PANE, DyeColor.LIME));
            }else{
                inv.setItem(i+9, Utils.removeDataCategorieGlassPane(Material.STAINED_GLASS_PANE, DyeColor.GRAY));
            }
            i = i +1;
        }

        i = 19;
        switch (categorie){
            case BLOC:
                for(ItemShop itemShop : Shop.itemShop.get(ItemCategorie.BLOC)){
                    inv.setItem(i,itemShop.getItemStack());
                    i = i +1;

                    if(i+1%9 == 0){
                        i = i +2;
                    }
                }
        }

        player.openInventory(inv);
    }

    private static ItemStack getBlockCategorie(ItemCategorie itemCategorie){
        switch (itemCategorie){
            case BLOC: return generateItemStackCategorie(Material.WOOL,"§aBlocs");
            case ARCS: return generateItemStackCategorie(Material.BOW,"§aArcs");
            case ARMES: return generateItemStackCategorie(Material.DIAMOND_SWORD,"§aArmes");
            case AUTRES: return generateItemStackCategorie(Material.GOLDEN_APPLE,"§aAutres");
            case OUTILS: return generateItemStackCategorie(Material.WOOL,"§aOutils");
            case ARMURES: return generateItemStackCategorie(Material.WOOL,"§aArmures");
            case POTIONS: return generateItemStackCategorie(Material.WOOL,"§aPotions");
        }
        return new ItemStack(Material.BARRIER);
    }

    private static ItemStack generateItemStackCategorie(Material material, String name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(new ArrayList<>(Arrays.asList("§f","§7Clique pour aller dans cette catégorie.")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
