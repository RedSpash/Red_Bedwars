package fr.red_spash.bedwars.Models;

public abstract class Generator {

    int itemSpawned = 0;

    public void itemPickup(int amount){
        this.itemSpawned = itemSpawned - amount;
    }

    public int getItemSpawned() {
        return itemSpawned;
    }

    public void addSpawnedItem(int amount){
        itemSpawned = itemSpawned + amount;
    }


}
