package fr.red_spash.bedwars.Shop.Applications;

import fr.red_spash.bedwars.Shop.Applications.ItemShop;
import fr.red_spash.bedwars.Shop.Prix;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BasicItem extends ItemShop {

    public BasicItem(ItemStack itemStack, String name, String desription, Prix prix) {
        super(itemStack, name, desription, prix);
    }

    public BasicItem(ItemStack itemStack, String name, String desription, ArrayList<String> informations, Prix prix){
        super(itemStack,name,desription,informations,prix);
    }



}
