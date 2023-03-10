package fr.red_spash.bedwars.Items;

import fr.red_spash.bedwars.Shop.Applications.BasicItem;
import fr.red_spash.bedwars.Shop.Applications.ItemShop;
import fr.red_spash.bedwars.Shop.Prix;
import fr.red_spash.bedwars.listeners.InventoryListener;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {
    public static ItemStack getEquipeItem() {
        ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(InventoryListener.ITEM_TEAM_NAME);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemShop getLastItem(){
        return new BasicItem(new ItemStack(Material.BARRIER),"§c§lLimite atteinte","§cVous ne pouvez pas améliorer §ccette item plus que ça.",new Prix(null,-1));
    }

}
