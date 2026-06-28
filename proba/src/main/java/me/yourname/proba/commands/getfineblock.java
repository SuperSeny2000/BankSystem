package me.yourname.proba.commands;

import me.yourname.proba.DataManager;
import me.yourname.proba.proba;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class getfineblock implements CommandExecutor {

    public static proba plugin;
    public static DataManager dataManager;

    public static NamespacedKey fineKey;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Только для игроков.");
            return true;
        }
        if (!player.hasPermission("bankplugin.op")) {
            player.sendMessage("У вас нет прав.");
            return true;
        }

        //dataManager.addBankBlock(player.getLocation().getBlock().getLocation(), "fine");
        ItemStack fine_block = new ItemStack(Material.LECTERN, 1);
        ItemMeta meta = fine_block.getItemMeta();
        meta.setDisplayName("§aШтраф");
        fineKey = new NamespacedKey(plugin, "fineBlockKey");
        meta.getPersistentDataContainer().set(fineKey, PersistentDataType.BOOLEAN, true);
        fine_block.setItemMeta(meta);
        player.getInventory().addItem(fine_block);
        return true;
    }
}
