package fr.red_spash.bedwars.Shop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ItemCategorie {
    BLOCS("Blocs",Material.WOOL),
    ARMES("Armes",Material.DIAMOND_SWORD),
    ARMURES("Armures",Material.IRON_CHESTPLATE),
    OUTILS("Outils",Material.DIAMOND_PICKAXE),
    ARCS("Arcs",Material.BOW),
    POTIONS("Potions",Material.BREWING_STAND_ITEM),
    AUTRES("Autres",Material.TNT);

    String name;
    Boolean enabled = true;
    ItemStack itemStack;
    private ItemCategorie(String name, Material material){
        this.name = name;
        this.itemStack = new ItemStack(material);
    }

    public String getName() {
        return name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }
}
