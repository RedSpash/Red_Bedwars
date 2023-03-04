package fr.red_spash.bedwars.Shop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Shop {

    public static LinkedHashMap<ItemCategorie, LinkedList<ItemShop>> itemShop = new LinkedHashMap<>();

    public static void initShopsCategories(){
        initShopsBlocs();
    }

    private static void initShopsBlocs() {
        LinkedList<ItemShop> items = new LinkedList<>();
        items.add(new ItemShop(new ItemStack(Material.WOOL),"Laine","Un bloc peu chère.",new Prix(Material.IRON_INGOT,4)));
        items.add(new ItemShop(new ItemStack(Material.HARD_CLAY),"Hardened Clay","Utile pour une protection rapide.",new Prix(Material.IRON_INGOT,12)));
        items.add(new ItemShop(new ItemStack(Material.GLASS),"Verre","Résiste aux explosions.",new Prix(Material.IRON_INGOT,12)));
        items.add(new ItemShop(new ItemStack(Material.ENDER_STONE),"End Stone","Un bloc solide.",new Prix(Material.IRON_INGOT,24)));
        items.add(new ItemShop(new ItemStack(Material.LADDER),"Ladder","Le meilleur moyen d'escalader des blocs.",new Prix(Material.IRON_INGOT,4)));
        items.add(new ItemShop(new ItemStack(Material.WOOD),"Bois","Un matériel nécessitant une hache pour être cassé rapidement.",new Prix(Material.GOLD_INGOT,4)));
        items.add(new ItemShop(new ItemStack(Material.OBSIDIAN),"Obsidienne","Il n'y a pas de bloc plus dur que l'obsidienne !",new Prix(Material.GOLD_INGOT,4)));
        itemShop.put(ItemCategorie.BLOC,items);
    }

}
