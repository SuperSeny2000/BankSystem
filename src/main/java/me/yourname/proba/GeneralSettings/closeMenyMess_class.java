package me.yourname.proba.GeneralSettings;

import org.bukkit.entity.Player;

public class closeMenyMess_class {

    // Ввести в чат кол-во
    public static void closeMenyMess(Player player){
        player.closeInventory();
        player.sendMessage("§aВведите в чат сумму (только число):");
        player.sendMessage("§cДля отмены напишите 'отмена'");
    }
}
