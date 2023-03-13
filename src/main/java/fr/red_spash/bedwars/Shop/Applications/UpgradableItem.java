package fr.red_spash.bedwars.Shop.Applications;

import fr.red_spash.bedwars.Items.Items;
import fr.red_spash.bedwars.Shop.Prix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class UpgradableItem extends ItemShop {

    private HashMap<UUID,Integer> playerLevel = new HashMap<>();
    private UpgradableItem beforeItemShop = null;
    private UpgradableItem afterItemShop = null;

    public UpgradableItem(ItemStack itemStack, String name, String desription, ArrayList<String> informations, Prix prix){
        super(itemStack,name,desription,informations,prix);
        super.setOnlyOne(true);
    }

    public UpgradableItem setNextUpgradeItemShop(UpgradableItem afterItemShop) {
        this.afterItemShop = afterItemShop;
        afterItemShop.beforeItemShop = this;
        return this;
    }

    public void resetPlayerData(){
        this.playerLevel.clear();
    }

    public UpgradableItem getMain(){
        UpgradableItem upgradableItem = this;
        while (upgradableItem.beforeItemShop != null){
            upgradableItem = upgradableItem.beforeItemShop;
        }
        return upgradableItem;
    }

    public UpgradableItem getDownGrade() {
        return beforeItemShop;
    }

    public UpgradableItem getUpgrade() {
        return afterItemShop;
    }

    public ItemShop getItemShop(Player p){
        if(!playerLevel.containsKey(p.getUniqueId())){
            playerLevel.put(p.getUniqueId(),0);
        }
        int level = playerLevel.get(p.getUniqueId());
        int i = 0;

        UpgradableItem itemShop = this;
        while(i<level && itemShop != null){
            i = i +1;
            itemShop = itemShop.getUpgrade();
        }
        ItemShop toReturnItemShop = itemShop;
        if(toReturnItemShop == null){
            toReturnItemShop = Items.getLastItem();
        }
        return toReturnItemShop;
    }

    public void addOneLevel(UUID uuid){
        if(beforeItemShop == null){
            if(!playerLevel.containsKey(uuid)){
                playerLevel.put(uuid,1);
            }else{
                playerLevel.put(uuid,playerLevel.get(uuid)+1);
            }
        }
    }

    public void addRemoveOneLevel(UUID uuid){
        if(beforeItemShop == null){
            if(!playerLevel.containsKey(uuid)){
                playerLevel.put(uuid,0);
            }else{
                if(playerLevel.get(uuid) != 0){
                    playerLevel.put(uuid,playerLevel.get(uuid)-1);
                }
            }
        }
    }

}
