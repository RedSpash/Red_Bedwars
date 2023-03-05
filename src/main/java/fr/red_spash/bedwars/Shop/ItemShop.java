package fr.red_spash.bedwars.Shop;

import fr.red_spash.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemShop {
    private ItemStack itemStack;
    private String name;
    private String desription;
    private Prix prix;
    private ArrayList<String> informations;


    public ItemShop(ItemStack itemStack,String name, String desription, Prix prix){
        this(itemStack,name,desription,null,prix);
    }

    public ItemShop(ItemStack itemStack,String name,String desription,ArrayList<String> informations, Prix prix){
        this.itemStack = itemStack;
        this.name = name;
        this.desription = desription;
        this.prix = prix;
        this.informations = informations;
    }

    public ArrayList<String> getInformations() {
        if(this.informations != null){
            return (ArrayList<String>) this.informations.clone();
        }
        return null;
    }

    public String getDesription() {
        return desription;
    }

    public String getName() {
        return name;
    }

    public Prix getPrix() {
        return prix;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = this.itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§a"+this.name);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        ArrayList<String> descriptionList = new ArrayList<>();
        if(this.desription.split(" ").length > 5){
            ArrayList<String> list = new ArrayList<>(Arrays.asList(this.desription.split(" ")));
            String message = "";
            for(String element : list.subList(0,(int) list.size()/2)){
                message = message +element+" ";
            }
            descriptionList.add("§f"+message);

            message = "";
            for(String element : list.subList(list.size()/2,list.size())){
                message = message +element+" ";
            }
            descriptionList.add("§f"+message);
        }else{
            descriptionList.add("§f"+this.desription);
        }

        if(this.informations != null){
            descriptionList.add("§f");
            descriptionList.add("§aContient:");
            for(String msg : this.informations){
                descriptionList.add("§7"+msg);
            }
        }
        descriptionList.add("§f");
        descriptionList.add("§6Prix: "+prixColor(this.prix.getItemTypeNeed())+this.prix.getAmount()+" "+ Utils.upperCaseFirst(this.prix.getItemTypeNeed().toString().toLowerCase().replace("_"," ")));
        itemMeta.setLore(descriptionList);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private String prixColor(Material itemTypeNeed) {
        switch (itemTypeNeed){
            case IRON_INGOT:return "§f";
            case GOLD_INGOT:return "§6";
            case DIAMOND:return "§b";
            case EMERALD:return "§2";
        }
        return "§d";
    }
}
