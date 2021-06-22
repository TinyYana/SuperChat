package io.github.tinyyana.superchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class SuperChatCommand implements CommandExecutor {

    private SuperChatPlugin plugin;
    private HashMap<UUID,Long> coolDown = new HashMap<>();

    public SuperChatCommand(SuperChatPlugin superChatPlugin) {
        plugin = superChatPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player) sender;
        String content = args[0];
        Material item = Material.DIAMOND;
        ItemStack handItem = p.getInventory().getItemInMainHand();

        int amount = plugin.getConfig().getInt("CostAmount");
        int coolDownTime = plugin.getConfig().getInt("CoolDownTime");

        String notEnoughDiamond = plugin.getConfig().getString("NotEnoughDiamond");
        String successSend = plugin.getConfig().getString("SuperChatSuccess");
        String permissionMessage = plugin.getConfig().getString("PermissionMessage");
        String inCoolDown = plugin.getConfig().getString("InCoolDown");
        String notDiamond = plugin.getConfig().getString("NotDiamond");
        String notPlayer = plugin.getConfig().getString("NotPlayer");

        if(!p.isOp() | !p.hasPermission("SuperChatPlugin.Use")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', permissionMessage));
            return true;
        }

        if(!(sender instanceof  Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',notPlayer));
            return true;
        }

        if(args.length == 1) {
            if(coolDown.containsKey(p.getUniqueId())) {
                if(!p.isOp() | !p.hasPermission("SuperChatPlugin.CoolDownBypass")) {
                    long secLeft = ((coolDown.get(p.getUniqueId()) / 1000) + coolDownTime) - (System.currentTimeMillis() / 1000);
                    if(secLeft > 0) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',inCoolDown) + ChatColor.DARK_AQUA + secLeft);
                        return true;
                    }
                }
            }

            if(handItem.getType() == item) {
                if(handItem.getAmount() > amount) {
                    handItem.setAmount(handItem.getAmount() - amount);
                    Bukkit.getServer().broadcastMessage(ChatColor.GRAY + p.getName() + ChatColor.translateAlternateColorCodes('&',successSend) + ChatColor.LIGHT_PURPLE + content);
                    coolDown.put(p.getUniqueId(),System.currentTimeMillis());
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1.0F,8.0F);
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',notEnoughDiamond));
                }
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',notDiamond));
            }
            return true;
        }
        return false;
    }
}
