package fr.red_spash.bedwars.utils;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

    public static void removeArmorStandData(ArmorStand armorStand) {
        armorStand.setSmall(true);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
    }

    public static String upperCaseFirst(String val) {
        val = val.toLowerCase();
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    public static double arrondir(Double x){
        double roundDbl = Math.round(x*10.0)/10.0;
        return roundDbl;
    }

    public static String twoCaractere(int nextItem) {
        String msg = String.valueOf(nextItem);
        if(msg.length() == 1){
            msg = "0"+msg;
        }
        return msg;
    }

    public static int random_number(Integer min, Integer max){
        max = max +1;
        return (int) (Math.random()*(max-min)) + min;
    }

    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static String getChatColorOf(DyeColor color) {
        if (DyeColor.RED.equals(color)) {
            return "§c";
        }else if (DyeColor.CYAN.equals(color)) {
            return "§3";
        }else if (DyeColor.BLUE.equals(color)) {
            return "§9";
        }else if (DyeColor.PINK.equals(color)) {
            return "§d";
        }else if (DyeColor.GRAY.equals(color)) {
            return "§7";
        }else if (DyeColor.ORANGE.equals(color)) {
            return "§6";
        }else if (DyeColor.LIME.equals(color)) {
            return "§a";
        }else if (DyeColor.MAGENTA.equals(color)) {
            return "§5";
        }else if (DyeColor.LIGHT_BLUE.equals(color)) {
            return "§b";
        }else if (DyeColor.YELLOW.equals(color)) {
            return "§e";
        }else if (DyeColor.GREEN.equals(color)) {
            return "§2";
        }else{
            return "§0";
        }
    }

    public static String getColorName(DyeColor color) {
        if (DyeColor.RED.equals(color)) {
            return "ROUGE";
        }else if (DyeColor.CYAN.equals(color)) {
            return "CYAN";
        }else if (DyeColor.BLUE.equals(color)) {
            return "BLEU";
        }else if (DyeColor.PINK.equals(color)) {
            return "FUCHSIA";
        }else if (DyeColor.GRAY.equals(color)) {
            return "GRIS";
        }else if (DyeColor.ORANGE.equals(color)) {
            return "ORANGE";
        }else if (DyeColor.LIME.equals(color)) {
            return "VERT CLAIR";
        }else if (DyeColor.MAGENTA.equals(color)) {
            return "MAGENTA";
        }else if (DyeColor.LIGHT_BLUE.equals(color)) {
            return "AQUA";
        }else if (DyeColor.YELLOW.equals(color)) {
            return "JAUNE";
        }else if (DyeColor.GREEN.equals(color)) {
            return "VERT";
        }else{
            return "§0";
        }
    }

    public static void setAI(LivingEntity entity, boolean hasAi) {
        EntityLiving handle = ((CraftLivingEntity) entity).getHandle();
        handle.getDataWatcher().watch(15, (byte) (hasAi ? 0 : 1));
    }

    public static void setSilent(LivingEntity entity, boolean data) {
        EntityLiving handle = ((CraftLivingEntity) entity).getHandle();
        handle.b(data);
    }

    public static ItemStack getArmorColor(Material material,DyeColor color, Enchantment enchantment, Integer niveau){
        ItemStack item = new ItemStack(material);
        if(material == Material.LEATHER_HELMET ||
                material == Material.LEATHER_BOOTS ||
                material == Material.LEATHER_CHESTPLATE ||
                material == Material.LEATHER_LEGGINGS){
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
            lam.setColor(Utils.getColor(color));

            if(enchantment != null && niveau != 0){
                lam.addEnchant(enchantment,niveau,true);
            }

            item.setItemMeta(lam);
        }

        return Utils.makeUnbreakable(item);
    }


    public static ItemStack getArmorColor(Material material,DyeColor color) {
        return getArmorColor(material,color,null,0);
    }

    private static Color getColor(DyeColor color) {
        if (DyeColor.RED.equals(color)) {
            return Color.RED;
        }else if (DyeColor.CYAN.equals(color)) {
            return Color.fromRGB(0,139,139);
        }else if (DyeColor.BLUE.equals(color)) {
            return Color.BLUE;
        }else if (DyeColor.PINK.equals(color)) {
            return Color.fromRGB(255,20,147);
        }else if (DyeColor.GRAY.equals(color)) {
            return Color.GRAY;
        }else if (DyeColor.ORANGE.equals(color)) {
            return Color.ORANGE;
        }else if (DyeColor.LIME.equals(color)) {
            return Color.LIME;
        }else if (DyeColor.MAGENTA.equals(color)) {
            return Color.fromRGB(255,0,255);
        }else if (DyeColor.LIGHT_BLUE.equals(color)) {
            return Color.fromRGB(0,255,255);
        }else if (DyeColor.YELLOW.equals(color)) {
            return Color.YELLOW;
        }else if (DyeColor.GREEN.equals(color)) {
            return Color.GREEN;
        }else{
            return Color.BLACK;
        }
    }

    public static void deleteDirectory(File directory) {
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            if(files != null) {
                for(File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        if(directory.delete()) {
            directory.delete();
            System.out.println(directory + " is deleted");
        }
        else {
            System.out.println("Directory not deleted");
        }
    }

    public static void sendActionText(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendTitle(Player p,String title, String subtitle,int debut, int temps, int fin){
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}");
        IChatBaseComponent chatsubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtitle + "\"}");

        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatsubtitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(debut, temps, fin);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitlePacket);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(titlePacket);
    }

    public static ItemStack removeDataCategorieGlassPane(Material stainedGlassPane, DyeColor dyeColor) {
        ItemStack itemStack = new ItemStack(stainedGlassPane,1,dyeColor.getData());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§e§l⇧ Catégories");
        itemMeta.setLore(new ArrayList<>(Arrays.asList("§6§l⇩ Objets")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }

    public static void setRotation(LivingEntity entity, float yaw, float pitch) {
        EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
        nmsEntity.pitch = pitch;
        nmsEntity.lastPitch = pitch;
        nmsEntity.yaw = yaw;
        nmsEntity.lastYaw = yaw;
    }

    public static ItemStack materialWithEnchant(Material material, Enchantment enchantment, int i) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment,i,true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack makeUnbreakable(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void clearArmor(Player p){
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
    }


}
