package fr.red_spash.bedwars.Models;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BaseUpgrade {

    private HashMap<ItemStack,Upgrades> upgrades = new HashMap<>();

    public BaseUpgrade(){
        upgrades.put(presentation(Material.FURNACE,"Forge de fer",new ArrayList<>(Arrays.asList("§7Permet d'augmenter la","§7production de votre forge")),null), new Upgrades(new ItemStack(Material.AIR)));
    }

    private ItemStack presentation(Material material, String title, ArrayList<String> description, ArrayList<String> toAdd){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(title);
        description.add(0,"§f");
        if(toAdd != null){
            description.add("§f");
            description.addAll(toAdd);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
