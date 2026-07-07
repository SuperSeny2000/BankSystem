package me.yourname.proba.GeneralSettings;

import me.yourname.proba.DataManager;
import me.yourname.proba.menu.bank_menu_class;
import me.yourname.proba.menu.fine_menu_class;
import me.yourname.proba.proba;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerChatMess implements Listener {

    public static final Map<UUID, String> xuyInput = new HashMap<>();
    public static proba plugin;
    public static DataManager dataManager;
    public static String message1 = null;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String action = xuyInput.get(uuid);

        // Если игрок не ожидает ввода причины – выходим
        if (action == null) return;

        // Сохраняем сообщение как причину
        PlayerChatMess.message1 = event.getMessage();
        // Обновляем меню и открываем его (в синхронном потоке)
        UUID targetPlayer = myself_class.selectedPlayer;
        int balance = dataManager.getBalance(targetPlayer);

        Bukkit.getScheduler().runTask(plugin, () -> {
            // Обрабатываем только случай ввода причины
            if (action.equals("give_fine_mess")){
                updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.give_fine_menu, balance, PlayerChatAmount.amount, "give_menu");
                player.openInventory(fine_menu_class.give_fine_menu);
                player.sendMessage("Вы вписали причину: " + PlayerChatMess.message1);
            } else if (action.equals("addacc")) {
                updateMenuDisplay_class.updateAddAccMenuWithRealNames(myself_class.selectedPlayer, bank_menu_class.addAcc_menu);
                player.openInventory(bank_menu_class.addAcc_menu);
                player.sendMessage("Вы вписали причину: " + PlayerChatMess.message1);
            } else if (action.equals("create_new_acc")) {
            UUID target = myself_class.selectedPlayer;
            boolean success = dataManager.addAcc(target, PlayerChatMess.message1);

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (success) {
                    player.sendMessage("§aСчёт \"" + PlayerChatMess.message1 + "\" успешно создан!");
                    // Обновляем меню addAcc_menu
                    updateMenuDisplay_class.updateAddAccMenuWithRealNames(target, bank_menu_class.addAcc_menu);
                } else {
                    player.sendMessage("§cНе удалось создать счёт (максимум 3).");
                }
                player.openInventory(bank_menu_class.addAcc_menu);
            });
            xuyInput.remove(uuid);
        }
        });
        xuyInput.remove(uuid);
    }
}
