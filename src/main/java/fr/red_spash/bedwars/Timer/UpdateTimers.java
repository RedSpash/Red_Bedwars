package fr.red_spash.bedwars.Timer;

import fr.red_spash.bedwars.Models.ItemGenerator;
import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;

public class UpdateTimers implements Runnable{
    @Override
    public void run() {
        for(ItemGenerator itemGenerator : BedWarsGame.ItemGenerators){
            itemGenerator.updateArmorStands();
        }
    }

}
