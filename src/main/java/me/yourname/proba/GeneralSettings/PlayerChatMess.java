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

import static me.yourname.proba.GeneralSettings.PlayerChatAmount.amount;
import static me.yourname.proba.menu.bank_menu_class.popolnit_menu;

public class PlayerChatMess implements Listener {

    public static final Map<UUID, String> xuyInput = new HashMap<>();
    public static proba plugin;
    public static DataManager dataManager;
    public static String message1 = null;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // 1. Получаем игрока
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String action = xuyInput.get(uuid);

        // 2. Получаем текст сообщения и сохраняем в переменную
        PlayerChatMess.message1 = event.getMessage();

        // 3. Теперь переменная "message" содержит текст, который написал игрок
        // Можете использовать её для своих целей
        //player.sendMessage("Вы написали: " + message1);


        UUID targetPlayer = myself_class.selectedPlayer;
        int balance = dataManager.getBalance(targetPlayer);
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (action != null && action.equals("give_fine_mess")) {
                updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.give_fine_menu, balance, amount, "give_menu");
                player.openInventory(fine_menu_class.give_fine_menu);
                player.sendMessage("Вы вписали123123: " + message1);
            }
            xuyInput.remove(uuid);
        });
        // Например, можно сохранить в поле класса или в статическую переменную
        // this.lastMessage = message;
    }
}