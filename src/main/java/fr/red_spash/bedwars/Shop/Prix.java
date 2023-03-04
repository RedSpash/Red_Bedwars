package fr.red_spash.bedwars.Shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Prix {

    private int amount;
    private Material itemTypeNeed;

    public Prix(Material itemTypeNeed,int amount){
        this.amount = amount;
        this.itemTypeNeed = itemTypeNeed;
    }

    public int getAmount() {
        return amount;
    }

    public Material getItemTypeNeed() {
        return itemTypeNeed;
    }

    public boolean canBuy(Player p){
        return p.getInventory().containsAtLeast(new ItemStack(itemTypeNeed),amount);
    }

    public void buy(Player p){
        ItemStack itemStack = new ItemStack(itemTypeNeed);
        itemStack.setAmount(amount);
        p.getInventory().remove(itemStack);
    }

}
