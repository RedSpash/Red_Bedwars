package fr.red_spash.bedwars.Shop;

import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Shop.Applications.ItemShop;
import fr.red_spash.bedwars.Shop.Applications.UpgradableItem;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class ShopEvent implements Listener {

    public static final String TITLE_SHOP = "Boutique d'objets";
    public static HashMap<UUID,ItemCategorie> playerCategorie = new HashMap<>();

    @EventHandler
    public void InteractVillager(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof Villager){
            Player p = e.getPlayer();
            if(BedWarsGame.inRespawn.containsKey(p.getUniqueId()) || BedWarsGame.playerSpectator.contains(p.getUniqueId())){
                if(p.getGameMode() != GameMode.CREATIVE){
                    e.setCancelled(true);
                    return;
                }
            }
            Villager villager = (Villager) e.getRightClicked();
            if(villager.getCustomName() != null){
                if(villager.getCustomName().equalsIgnoreCase("§6§lItems")){
                    e.setCancelled(true);
                    openShopInventory(p,ItemCategorie.BLOCS);
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
                                    }else if(e.getSlot() >= 19) {
                                        //SHOP
                                        ItemShop findedShop = null;
                                        if(p.getInventory().firstEmpty() == -1){
                                            p.sendMessage("§cVous n'avez plus de place dans l'inventaire !");
                                            p.playSound(p.getLocation(), Sound.VILLAGER_NO,1,1);
                                            return;
                                        }
                                        for (ItemShop shop : Shop.itemShop.get(playerCategorie.get(p.getUniqueId()))) {
                                            if(shop instanceof UpgradableItem){
                                                UpgradableItem upgradableItem = (UpgradableItem) shop;

                                                while(upgradableItem.getUpgrade() != null && !upgradableItem.getItemStack().isSimilar(item)){
                                                    upgradableItem = upgradableItem.getUpgrade();
                                                }
                                                if(upgradableItem.getItemStack().isSimilar(item)){
                                                    findedShop = upgradableItem;
                                                    break;
                                                }
                                            }else if (shop.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
                                                findedShop = shop;
                                                break;
                                            }
                                        }
                                        if(findedShop != null) {
                                            if (findedShop.getPrix().getAmount() == -1) {
                                                return;
                                            }
                                            if (findedShop.getPrix().canBuy(p)) {
                                                if(findedShop.getMaxItemInInventory() != -1){
                                                    int amount = 0;
                                                    for(ItemStack itemStack : p.getInventory()){
                                                        if(itemStack != null){
                                                            if(itemStack.getType() == findedShop.getItemStack().getType()){
                                                                amount = amount + itemStack.getAmount();
                                                            }
                                                        }
                                                    }
                                                    if(findedShop.getMaxItemInInventory() <= amount){
                                                        p.sendMessage("§cVous ne pouvez pas avoir plus de "+findedShop.getMaxItemInInventory()+" "+findedShop.getName()+"§c dans l'inventaire !");
                                                        p.playSound(p.getLocation(), Sound.VILLAGER_NO,1,1);
                                                        return;
                                                    }
                                                }

                                                findedShop.getPrix().buy(p);
                                                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 2, 1);
                                                if(findedShop instanceof UpgradableItem){
                                                    UpgradableItem upgradableItem = (UpgradableItem) findedShop;
                                                    upgradableItem.getMain().addOneLevel(p.getUniqueId());
                                                    openShopInventory(p,playerCategorie.get(p.getUniqueId()));
                                                }
                                                if(findedShop.getOnlyOne()){
                                                    String[] splitedWord = findedShop.getItemStack().getType().toString().split("_");
                                                    if(splitedWord.length <= 2){
                                                        String word = splitedWord[splitedWord.length-1];
                                                        int position = -1;
                                                        for(int i =0; i< p.getInventory().getSize(); i++){
                                                            ItemStack itemStack = p.getInventory().getItem(i);
                                                            if(itemStack != null){
                                                                if(itemStack.getType().toString().contains("_"+word)){
                                                                    position = i;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if(position == -1){
                                                            for(int i = 0; i < p.getInventory().getArmorContents().length; i++){
                                                                ItemStack itemStack = p.getInventory().getArmorContents()[i];
                                                                if(itemStack != null){
                                                                    if(itemStack.getType().toString().contains("_"+word)){
                                                                        position = i;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(position == -1){
                                                            position = p.getInventory().firstEmpty();
                                                        }
                                                        p.getInventory().setItem(position,findedShop.givableItemStack());
                                                    }
                                                }else{
                                                    p.getInventory().addItem(findedShop.givableItemStack());
                                                }
                                                if(!findedShop.getItemStack().getType().isBlock()){
                                                    if(findedShop instanceof UpgradableItem){
                                                        UpgradableItem upgradableItem = (UpgradableItem) findedShop;
                                                        if(upgradableItem.getItemStack().getType().toString().contains("_PICKAXE")){
                                                            BedWarsGame.playersDatas.get(p.getUniqueId()).setPickaxe(upgradableItem);
                                                        }else if(upgradableItem.getItemStack().getType().toString().contains("_AXE")){
                                                            BedWarsGame.playersDatas.get(p.getUniqueId()).setAxe(upgradableItem);
                                                        }
                                                    }else{
                                                        if(findedShop.getItemStack().getType().toString().contains("SHEARS")){
                                                            BedWarsGame.playersDatas.get(p.getUniqueId()).setSheart(true);
                                                        }if(findedShop.getName().contains("armure")){
                                                            BedWarsGame.playersDatas.get(p.getUniqueId()).setArmor(findedShop);
                                                            BedWarsGame.playersDatas.get(p.getUniqueId()).updateArmor();
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
                }
            }
        }
    }

    public static void openShopInventory(Player player, ItemCategorie categorie) {
        playerCategorie.put(player.getUniqueId(),categorie);
        Inventory inv = Bukkit.createInventory(null,9*6, TITLE_SHOP);
        for(int i = 9; i<= 17; i++){
            inv.setItem(i, Utils.removeDataCategorieGlassPane(Material.STAINED_GLASS_PANE, DyeColor.GRAY));
        }
        int i =1;
        for(ItemCategorie itemCategorie : Shop.itemShop.keySet()){
            ItemStack itemStack = getBlockCategorie(itemCategorie);
            if(categorie == itemCategorie){
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.addEnchant(Enchantment.DURABILITY,1,true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemStack.setItemMeta(itemMeta);
                inv.setItem(i+9, Utils.removeDataCategorieGlassPane(Material.STAINED_GLASS_PANE, DyeColor.LIME));
            }
            inv.setItem(i,itemStack);
            i = i +1;
        }

        i = 19;
        for(ItemShop itemShop : Shop.itemShop.get(categorie)){
            if(itemShop instanceof UpgradableItem){
                UpgradableItem upgradableItem = (UpgradableItem) itemShop;
                itemShop = upgradableItem.getItemShop(player);
            }
            inv.setItem(i,itemShop.getItemStack());
            i = i +1;

            if((i+1)%9 == 0){
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
            case OUTILS: return generateItemStackCategorie(Material.DIAMOND_PICKAXE,"§aOutils");
            case ARMURES: return generateItemStackCategorie(Material.IRON_CHESTPLATE,"§aArmures");
            case POTIONS: return generateItemStackCategorie(Material.BREWING_STAND_ITEM,"§aPotions");
        }
        return new ItemStack(Material.BARRIER);
    }

    private static ItemStack generateItemStackCategorie(Material material, String name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setLore(new ArrayList<>(Arrays.asList("§f","§7Clique pour aller dans cette catégorie.","§f")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
