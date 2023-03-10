package fr.red_spash.bedwars.Shop;

import fr.red_spash.bedwars.Shop.Applications.BasicItem;
import fr.red_spash.bedwars.Shop.Applications.ItemShop;
import fr.red_spash.bedwars.Shop.Applications.UpgradableItem;
import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Shop {

    public static LinkedHashMap<ItemCategorie, LinkedList<ItemShop>> itemShop = new LinkedHashMap<>();

    public static void initShopsCategories(){
        initShopsBlocs();
        initShopsArmes();
        initShopsArmors();
        initShopsOutils();
        initShopsBow();
        initShopsPotions();
        initShopsOther();
    }

    private static void initShopsOther(){
        LinkedList<ItemShop> items = new LinkedList<>();
        items.add(new BasicItem(new ItemStack(Material.GOLDEN_APPLE),"Pomme en or","Un moyen simple de regagner de la vie.",new Prix(Material.GOLD_INGOT,3)));
        items.add(new BasicItem(new ItemStack(Material.SNOW_BALL),"FishBall","Fait apparaitre un silverfish à l'impacte.",new Prix(Material.IRON_INGOT,30)).setSpecial());
        items.add(new BasicItem(new ItemStack(Material.FIREBALL),"FireBall","Permet de lancer une boule de feu.",new Prix(Material.IRON_INGOT,40)).setSpecial());
        items.add(new BasicItem(new ItemStack(Material.TNT),"TNT","S'active automatiquement lorsque vous la posez.",new Prix(Material.GOLD_INGOT,4)));
        items.add(new BasicItem(new ItemStack(Material.ENDER_PEARL),"Ender Pearl","Le meilleur moyen de se déplacer.",new Prix(Material.EMERALD,4)));
        items.add(new BasicItem(new ItemStack(Material.WATER_BUCKET),"Sceau d'eau","Un simple sceau d'eau.",new Prix(Material.GOLD_INGOT,3)));
        items.add(new BasicItem(new ItemStack(Material.EGG),"Bridge Egg","Fait apparaitre un pont lorsqu'il est lancé.",new Prix(Material.EMERALD,2)).setSpecial());
        items.add(new BasicItem(new ItemStack(Material.COMPASS),"Cibleur","Permet de cibler un joueur aléatoirement.",new Prix(Material.EMERALD,2)).setSpecial());
        items.add(new BasicItem(new ItemStack(Material.CHEST),"Château de poche","Fait apparaitre une tour! §8(Spécialement pour Red_Spash)",new Prix(Material.IRON_INGOT,24)).setSpecial());
        items.add(new BasicItem(new ItemStack(Material.SPONGE),"Eponge","Pas besoin de plus pour expliquer.",new Prix(Material.GOLD_INGOT,3)));
        items.add(new BasicItem(new ItemStack(Material.MILK_BUCKET),"Lait Magique","Permet d'être protégé de tous les pièges ennemis pendant 60 secondes.",new Prix(Material.GOLD_INGOT,4)).setSpecial());

        itemShop.put(ItemCategorie.AUTRES,items);
    }

    private static void initShopsPotions(){
        LinkedList<ItemShop> items = new LinkedList<>();

        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setMainEffect(PotionEffectType.SPEED);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED,20*45,1,true),true);
        itemStack.setItemMeta(potionMeta);
        items.add(new BasicItem(itemStack,"Potion - Speed II (45 secs)","Augmente votre vitesse de déplacement.",new Prix(Material.EMERALD,1)));

        itemStack = new ItemStack(Material.POTION);
        potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setMainEffect(PotionEffectType.JUMP);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP,20*45,4,true,true),true);
        itemStack.setItemMeta(potionMeta);
        items.add(new BasicItem(itemStack,"Potion - Jump Boost V (45 secs)","Augmente votre capacité à sauter.",new Prix(Material.EMERALD,1)));

        itemStack = new ItemStack(Material.POTION);
        potionMeta = (PotionMeta) itemStack.getItemMeta();

        potionMeta.setMainEffect(PotionEffectType.INVISIBILITY);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20*30,0,true),true);
        itemStack.setItemMeta(potionMeta);
        items.add(new BasicItem(itemStack,"Potion - Invisibilité (30 secs)","Vous rends invisible.",new Prix(Material.EMERALD,2)));


        itemShop.put(ItemCategorie.POTIONS,items);
    }

    private static void initShopsBow(){
        LinkedList<ItemShop> items = new LinkedList<>();

        items.add(new BasicItem(new ItemStack(Material.ARROW,8),"Flèches","Le seul moyen de tirer avec un arc.",new Prix(Material.GOLD_INGOT,2)));

        items.add(new BasicItem(new ItemStack(Material.BOW),"Arc","Un arc classique mais utile.",new Prix(Material.GOLD_INGOT,12)));

        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
        item.setItemMeta(itemMeta);
        items.add(new BasicItem(item,"Arc - Power I","Un arc infligeant de gros dégats.",new Prix(Material.GOLD_INGOT,20)));

        item = new ItemStack(Material.BOW);
        itemMeta = item.getItemMeta();
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE,1,true);
        itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK,1,true);
        item.setItemMeta(itemMeta);
        items.add(new BasicItem(item,"Arc - Power I & Punch I","\"Un arc infligeant de gros dégats et expulsant les joueurs.",new Prix(Material.EMERALD,6)));
        itemShop.put(ItemCategorie.ARCS,items);
    }

    private static void initShopsOutils(){
        LinkedList<ItemShop> items = new LinkedList<>();
        ArrayList<String> pioche_desc = new ArrayList<>(Arrays.asList("§7Cet item est améliorable.","§7A chaque mort le niveau","§7de la pioche diminue de 1.","§7Vous ne pouvez pas descendre","en dessous de 1!"));
        items.add(
                new UpgradableItem(Utils.makeUnbreakable(new ItemStack(Material.WOOD_PICKAXE)),"Pioche en bois","§f",pioche_desc,new Prix(Material.IRON_INGOT,10)).setNextUpgradeItemShop(
                        new UpgradableItem(Utils.makeUnbreakable(Utils.materialWithEnchant(Material.IRON_PICKAXE,Enchantment.DIG_SPEED,2)),"Améliore votre pioche","§f",pioche_desc,new Prix(Material.IRON_INGOT,10)).setNextUpgradeItemShop(
                                new UpgradableItem(Utils.makeUnbreakable(Utils.materialWithEnchant(Material.GOLD_PICKAXE,Enchantment.DIG_SPEED,3)),"Améliore votre pioche","§f",pioche_desc,new Prix(Material.GOLD_INGOT,3)).setNextUpgradeItemShop(
                                        new UpgradableItem(Utils.makeUnbreakable(Utils.materialWithEnchant(Material.DIAMOND_PICKAXE,Enchantment.DIG_SPEED,3)),"Améliore votre pioche","§f",pioche_desc,new Prix(Material.GOLD_INGOT,6)))))
        );

        ArrayList<String> axe_desc = new ArrayList<>(Arrays.asList("§7Cet item est améliorable.","§7A chaque mort le niveau","§7de la pioche diminue de 1.","§7Vous ne pouvez pas descendre","en dessous de 1!"));

        items.add(
                new UpgradableItem(Utils.makeUnbreakable(Utils.materialWithEnchant(Material.WOOD_AXE,Enchantment.DIG_SPEED,1)),"Hache en bois","§f",axe_desc,new Prix(Material.IRON_INGOT,10)).setNextUpgradeItemShop(
                        new UpgradableItem(Utils.makeUnbreakable(Utils.materialWithEnchant(Material.IRON_AXE,Enchantment.DIG_SPEED,2)),"Améliore votre hache","§f",axe_desc,new Prix(Material.IRON_INGOT,10)).setNextUpgradeItemShop(
                                new UpgradableItem(Utils.makeUnbreakable(Utils.materialWithEnchant(Material.GOLD_AXE,Enchantment.DIG_SPEED,2)),"Améliore votre hache","§f",axe_desc,new Prix(Material.GOLD_INGOT,3)).setNextUpgradeItemShop(
                                        new UpgradableItem(Utils.makeUnbreakable(Utils.materialWithEnchant(Material.DIAMOND_AXE,Enchantment.DIG_SPEED,3)),"Améliore votre hache","§f",axe_desc,new Prix(Material.GOLD_INGOT,6)))))
        );

        items.add(new BasicItem(new ItemStack(Material.SHEARS),"Shears","Permet de casser les blocks de laine rapidements.",new Prix(Material.BEDROCK,6)).setOnlyOne(true));
        itemShop.put(ItemCategorie.OUTILS,items);
    }

    private static void initShopsArmors(){
        LinkedList<ItemShop> items = new LinkedList<>();
        items.add(new BasicItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE),"Armure en maille","Une armure de faible protection.",new ArrayList<>(Arrays.asList("§eContient: ","  §7- Bottes en maille","  §7- Leggings en maille")),new Prix(Material.IRON_INGOT,50)));
        items.add(new BasicItem(new ItemStack(Material.IRON_CHESTPLATE),"Armure en fer","Une armure moyenne permettant de bon combat.",new ArrayList<>(Arrays.asList("§eContient: ","  §7- Bottes en fer","  §7- Leggings en fer")),new Prix(Material.GOLD_INGOT,12)));
        items.add(new BasicItem(new ItemStack(Material.DIAMOND_CHESTPLATE),"Armure en diamant","La meilleur protection pour les armures.",new ArrayList<>(Arrays.asList("§eContient: ","  §7- Bottes en diamant","  §7- Leggings en diamant")),new Prix(Material.EMERALD,6)));
        itemShop.put(ItemCategorie.ARMURES,items);
    }

    private static void initShopsBlocs() {
        LinkedList<ItemShop> items = new LinkedList<>();
        items.add(new BasicItem(new ItemStack(Material.WOOL,16),"Laine","Un bloc peu chère.",new Prix(Material.IRON_INGOT,4)));
        items.add(new BasicItem(new ItemStack(Material.SANDSTONE,16),"Hardened Clay","Utile pour une protection rapide.",new Prix(Material.IRON_INGOT,12)));
        items.add(new BasicItem(new ItemStack(Material.GLASS,4),"Verre","Résiste aux explosions.",new Prix(Material.IRON_INGOT,12)));
        items.add(new BasicItem(new ItemStack(Material.ENDER_STONE,12),"End Stone","Un bloc solide.",new Prix(Material.IRON_INGOT,24)));
        items.add(new BasicItem(new ItemStack(Material.LADDER,16),"Ladder","Le meilleur moyen d'escalader des blocs.",new Prix(Material.IRON_INGOT,4)));
        items.add(new BasicItem(new ItemStack(Material.WOOD,16),"Bois","Un matériel nécessitant une hache pour être cassé rapidement.",new Prix(Material.GOLD_INGOT,4)));
        items.add(new BasicItem(new ItemStack(Material.OBSIDIAN,4 ),"Obsidienne","Il n'y a pas de bloc plus dur que l'obsidienne !",new Prix(Material.EMERALD,4)));
        itemShop.put(ItemCategorie.BLOCS,items);
    }

    private static void initShopsArmes() {
        LinkedList<ItemShop> items = new LinkedList<>();
        items.add(new BasicItem(new ItemStack(Material.STONE_SWORD),"Epee en pierre","Une épée simple.",new Prix(Material.IRON_INGOT,10)).setOnlyOne(true));
        items.add(new BasicItem(new ItemStack(Material.IRON_SWORD),"Epee en fer","Une épée puissante à faible coût.",new Prix(Material.GOLD_INGOT,7)).setOnlyOne(true));
        items.add(new BasicItem(new ItemStack(Material.DIAMOND_SWORD),"Epee en diamant","La meilleur des épées !",new Prix(Material.EMERALD,4)).setOnlyOne(true));
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta itemMeta = stick.getItemMeta();
        itemMeta.addEnchant(Enchantment.KNOCKBACK,2,true);
        stick.setItemMeta(itemMeta);
        items.add(new BasicItem(stick,"Baton KnockBack II","Envoie tes ennemis dans le vide.",new Prix(Material.IRON_INGOT,24)).setOnlyOne(true));
        itemShop.put(ItemCategorie.ARMES,items);
    }

}
