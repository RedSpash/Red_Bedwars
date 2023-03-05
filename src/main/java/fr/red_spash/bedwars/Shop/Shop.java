package fr.red_spash.bedwars.Shop;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Shop {

    public static LinkedHashMap<ItemCategorie, LinkedList<ItemShop>> itemShop = new LinkedHashMap<>();

    public static void initShopsCategories(){
        initShopsBlocs();
        initShopsArmes();
        initShopsArmors();
        initShopsOutils();
    }

    private static void initShopsOutils(){
        LinkedList<ItemShop> items = new LinkedList<>();
        items.add(new ItemShop(new ItemStack(Material.WOOD_PICKAXE),"Améliore votre pioche","Améliore la qualité de votre pioche.",new Prix(Material.BEDROCK,50)));
        items.add(new ItemShop(new ItemStack(Material.WOOD_AXE),"Améliore votre hache","Améliore la qualité de votre hache.",new Prix(Material.BEDROCK,12)));
        items.add(new ItemShop(new ItemStack(Material.SHEARS),"Shears","Permet de casser les blocks de laine rapidements.",new Prix(Material.BEDROCK,6)));
        itemShop.put(ItemCategorie.OUTILS,items);
    }

    private static void initShopsArmors(){
        LinkedList<ItemShop> items = new LinkedList<>();
        items.add(new ItemShop(new ItemStack(Material.CHAINMAIL_CHESTPLATE),"Armure en maille","Une armure de faible protection.",new ArrayList<>(Arrays.asList("  §7- Bottes en maille","  §7- Leggings en maille")),new Prix(Material.IRON_INGOT,50)));
        items.add(new ItemShop(new ItemStack(Material.IRON_CHESTPLATE),"Armure en fer","Une armure moyenne permettant de bon combat.",new ArrayList<>(Arrays.asList("  §7- Bottes en fer","  §7- Leggings en fer")),new Prix(Material.GOLD_INGOT,12)));
        items.add(new ItemShop(new ItemStack(Material.DIAMOND_CHESTPLATE),"Armure en diamant","La meilleur protection pour les armures.",new ArrayList<>(Arrays.asList("  §7- Bottes en diamant","  §7- Leggings en diamant")),new Prix(Material.EMERALD,6)));
        itemShop.put(ItemCategorie.ARMURES,items);
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
