package fr.red_spash.bedwars.Shop;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Shop {

    public static LinkedHashMap<ItemCategorie, LinkedList<ItemShop>> itemShop = new LinkedHashMap<>();

    public static void initShopsCategories(){
        initShopsBlocs();
        initShopsArmes();
    }

    private static void initShopsBlocs() {
        LinkedList<ItemShop> items = new LinkedList<>();
        items.add(new ItemShop(new ItemStack(Material.WOOL),"Laine","Un bloc peu chère.",new Prix(Material.IRON_INGOT,4)));
        items.add(new ItemShop(new ItemStack(Material.HARD_CLAY),"Hardened Clay","Utile pour une protection rapide.",new Prix(Material.IRON_INGOT,12)));
        items.add(new ItemShop(new ItemStack(Material.GLASS),"Verre","Résiste aux explosions.",new Prix(Material.IRON_INGOT,12)));
        items.add(new ItemShop(new ItemStack(Material.ENDER_STONE),"End Stone","Un bloc solide.",new Prix(Material.IRON_INGOT,24)));
        items.add(new ItemShop(new ItemStack(Material.LADDER),"Ladder","Le meilleur moyen d'escalader des blocs.",new Prix(Material.IRON_INGOT,4)));
        items.add(new ItemShop(new ItemStack(Material.WOOD),"Bois","Un matériel nécessitant une hache pour être cassé rapidement.",new Prix(Material.GOLD_INGOT,4)));
        items.add(new ItemShop(new ItemStack(Material.OBSIDIAN),"Obsidienne","Il n'y a pas de bloc plus dur que l'obsidienne !",new Prix(Material.EMERALD,4)));
        itemShop.put(ItemCategorie.BLOCS,items);
    }

    private static void initShopsArmes() {
        LinkedList<ItemShop> items = new LinkedList<>();
        items.add(new ItemShop(new ItemStack(Material.STONE_SWORD),"Epee en pierre","Une épée simple.",new Prix(Material.IRON_INGOT,10)));
        items.add(new ItemShop(new ItemStack(Material.IRON_SWORD),"Epee en fer","Une épée puissante à faible coût.",new Prix(Material.GOLD_INGOT,7)));
        items.add(new ItemShop(new ItemStack(Material.DIAMOND_SWORD),"Epee en diamant","La meilleur des épées !",new Prix(Material.EMERALD,4)));
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta itemMeta = stick.getItemMeta();
        itemMeta.addEnchant(Enchantment.KNOCKBACK,2,true);
        stick.setItemMeta(itemMeta);
        items.add(new ItemShop(stick,"Baton KnockBack II","Envoie tes ennemis dans le vide.",new Prix(Material.IRON_INGOT,24)));
        itemShop.put(ItemCategorie.ARMES,items);
    }

}
