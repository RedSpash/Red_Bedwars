package fr.red_spash.bedwars.Shop;

import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ShopEvent implements Listener {

    public static final String TITLE_SHOP = "Boutique d'objets";

    @EventHandler
    public void InteractVillager(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof Villager){
            Villager villager = (Villager) e.getRightClicked();
            if(villager.getCustomName() != null){
                if(villager.getCustomName().equalsIgnoreCase("§6§lItems")){
                    e.setCancelled(true);
                    openShopInventory(e.getPlayer(),ItemCategorie.BLOCS);
                }
            }
        }
    }

    @EventHandler
    public static void Interact(InventoryClickEvent e){
        if(e.getWhoClicked() instanceof Player){
            Player p = (Player) e.getWhoClicked();
            InventoryView view = e.getView();
            if(view.getTitle().equalsIgnoreCase(TITLE_SHOP)){
                e.setCancelled(true);
                if(e.getClickedInventory() != null){
                    if(e.getClickedInventory().getName().equalsIgnoreCase(TITLE_SHOP)){
                        if(e.getCurrentItem() != null){
                            ItemStack item = e.getCurrentItem();
                            if(item.getItemMeta() != null){
                                if(item.getItemMeta().getDisplayName() != null){

                                    //CATEGORIES
                                    if(e.getSlot() <= 8){
                                        ItemCategorie categorie = Enum.valueOf(ItemCategorie.class,(item.getItemMeta().getDisplayName().replace("§l","").substring(2)).toUpperCase());
                                        openShopInventory(p,categorie);
                                    }
                                }
                            }

                        }

                    }
                }
            }
        }
    }

    public static void openShopInventory(Player player, ItemCategorie categorie) {
        Inventory inv = Bukkit.createInventory(null,9*6, TITLE_SHOP);
        for(int i = 9; i<= 17; i++){
            inv.setItem(i, Utils.removeDataCategorieGlassPane(Material.STAINED_GLASS_PANE, DyeColor.GRAY));
        }
        int i =1;
        for(ItemCategorie itemCategorie : Shop.itemShop.keySet()){
            ItemStack itemStack = getBlockCategorie(itemCategorie);
            inv.setItem(i,itemStack);
            if(categorie == itemCategorie){
                inv.setItem(i+9, Utils.removeDataCategorieGlassPane(Material.STAINED_GLASS_PANE, DyeColor.LIME));
            }
            i = i +1;
        }

        i = 19;
        for(ItemShop itemShop : Shop.itemShop.get(categorie)){
            inv.setItem(i,itemShop.getItemStack());
            i = i +1;

            if(i+1%9 == 0){
                i = i +2;
            }
        }

        player.openInventory(inv);
    }

    private static ItemStack getBlockCategorie(ItemCategorie itemCategorie){
        switch (itemCategorie){
            case BLOCS: return generateItemStackCategorie(Material.WOOL,"§aBlocs");
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
