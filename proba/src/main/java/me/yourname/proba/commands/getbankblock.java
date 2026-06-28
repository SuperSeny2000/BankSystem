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

public class getbankblock implements CommandExecutor {

    public static proba plugin;
    public static DataManager dataManager;

    public static NamespacedKey bankKey;

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

        //dataManager.addBankBlock(player.getLocation().getBlock().getLocation(), "atm");
        ItemStack bank_block = new ItemStack(Material.LODESTONE, 1);
        ItemMeta meta = bank_block.getItemMeta();
        meta.setDisplayName("§aБанкомат");
        bankKey = new NamespacedKey(plugin, "bankBlockKey");
        meta.getPersistentDataContainer().set(bankKey, PersistentDataType.BOOLEAN, true);
        bank_block.setItemMeta(meta);
        player.getInventory().addItem(bank_block);
        return true;
    }
}

