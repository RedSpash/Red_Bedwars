package fr.red_spash.bedwars.Models;

import org.bukkit.Color;
import org.bukkit.Location;

import java.util.ArrayList;

public class Base {

    private Color teamColor;
    private ArrayList<String> playerNames = new ArrayList<>();
    private Location bedLocation;
    private Location spawnLocation;
    private ItemGenerator itemGenerator;

    public Base(Location spawnLocation, Location bedLocation, ItemGenerator itemGenerator, Color teamColor){
        this.spawnLocation = spawnLocation;
        this.bedLocation = bedLocation;
        this.itemGenerator = itemGenerator;
        this.teamColor = teamColor;
    }

}
