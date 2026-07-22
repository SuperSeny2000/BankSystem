package me.yourname.proba.commands;

import me.yourname.proba.menu.avito_menu_class;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class avito implements CommandExecutor {

    // хуйня
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Только для игроков.");
            return true;
        }

        player.openInventory(avito_menu_class.avito_menu);

        //commandSender.sendMessage("пока что соси");

        return false;
    }
}
