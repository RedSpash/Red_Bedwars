package fr.red_spash.bedwars.Scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import fr.red_spash.bedwars.BedWarsCore.BedWarsGame;
import fr.red_spash.bedwars.Main;
import fr.red_spash.bedwars.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ScoreboardManager {

    public static HashMap<UUID, FastBoard> scoreBoards = new HashMap<>();

    public static void initTimer(){
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            for (FastBoard board : scoreBoards.values()) {
                updateScoreBoard(board);
            }
        }, 0, 20);
    }

    private static void updateScoreBoard(FastBoard board) {
        if(BedWarsGame.gameStat == GameState.Waiting){
            board.updateLines("§7",
                    "§e§lEn attente...",
                    "§7  • §fJoueurs: "+Bukkit.getWorld("bedwars").getPlayers().size());
        }

    }

    public static void setScoreboard(Player p){
        FastBoard board = new FastBoard(p);
        board.updateTitle("§6§lBedwars");
        updateScoreBoard(board);

        scoreBoards.put(p.getUniqueId(), board);
    }

}
