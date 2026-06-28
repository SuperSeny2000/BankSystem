package me.yourname.proba.GeneralSettings;

import me.yourname.proba.DataManager;
import me.yourname.proba.menu.bankMe_menu_class;
import me.yourname.proba.menu.bank_menu_class;
import me.yourname.proba.menu.fine_menu_class;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.yourname.proba.GeneralSettings.PlayerChatAmount.amount;
import static me.yourname.proba.GeneralSettings.PlayerChatAmount.dataManager;

public class myself_class {

    public static DataManager dataManager;

    public static UUID selectedPlayer = null;
    public static UUID selectedPlayerForFine = null;

    public static void myself(Player player){
        if (selectedPlayer == null){
            selectedPlayer = player.getUniqueId();
            int balance = dataManager.getBalance(selectedPlayer);
            updateMenuDisplay_class.updateMenuDisplay(bank_menu_class.popolnit_menu, balance, amount, "popolnit");
            updateMenuDisplay_class.updateMenuDisplay(bank_menu_class.snat_menu, balance, amount, "snat");
            updateMenuDisplay_class.updateMenuDisplay(bankMe_menu_class.perevod_menu, balance, amount, "perevod");
            updateMenuDisplay_class.updateMenuDisplay(bankMe_menu_class.perevod_casna_menu, balance, amount, "perevod");
            updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.give_fine_menu, balance, amount, "give_menu");
            updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.remove_fine_menu, balance, amount, "remove_menu");
        }
        if (selectedPlayerForFine == null){
            String treasuryName = dataManager.getTreasuryAccount();
            if (treasuryName != null && !treasuryName.trim().isEmpty()) {
                OfflinePlayer name = Bukkit.getOfflinePlayer(treasuryName);
                selectedPlayerForFine = name.getUniqueId();
            } else {
                selectedPlayerForFine = player.getUniqueId();
                player.sendMessage("§cКазна не установлена! Использую вас как казну.");
            }
            int balance = dataManager.getBalance(selectedPlayer);
            updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.remove_fine_menu, balance, amount, "remove_menu");
        }
    }
}
