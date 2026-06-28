package me.yourname.proba.GeneralSettings;

import me.yourname.proba.menu.fine_menu_class;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class updateMenuDisplay_class {
    public static void updateMenuDisplay(Inventory menu, int balance, int amount, String menu1) {
        String playerName = Bukkit.getOfflinePlayer(myself_class.selectedPlayer).getName();

        if (menu1.equals("popolnit")) {
            int after = balance + amount;
            menu.setItem(2, createButt_class.createButt(Material.PLAYER_HEAD, "Выбрать игрока", "Текущий выбор: " + playerName));
            if (amount <= 0) {
                menu.setItem(4, createButt_class.createButt(Material.GOLD_INGOT, "Баланс счёта5 ___", "Название " + null + "\nБаланс: " + balance + "\nПосле пополнения: сумма не выбрана"));
                menu.setItem(8, createButt_class.createButt(Material.BARRIER, "Выберите сумму11", "Сначала выберите сумму для пополнения."));
            } else {
                menu.setItem(4, createButt_class.createButt(Material.GOLD_INGOT, "Баланс счёта6", "Название " + null + "\nБаланс: " + balance + "\nПосле пополнения: " + after));
                menu.setItem(8, createButt_class.createButt(Material.SLIME_BALL, "Подтвердить", "Нажми, чтобы подтвердить."));
            }
        }
        else if (menu1.equals("snat")) {
            int after = balance - amount;
            menu.setItem(2, createButt_class.createButt(Material.PLAYER_HEAD, "Выбрать игрока", "Текущий выбор: " + playerName));
            if (amount <= 0) {
                menu.setItem(4, createButt_class.createButt(Material.GOLD_INGOT, "Баланс счёта6 ___", "Название " + null + "\nБаланс: " + balance + "\nПосле снятия: сумма не выбрана"));
                menu.setItem(8, createButt_class.createButt(Material.BARRIER, "Выберите сумму12", "Сначала выберите сумму для пополнения."));
            } else {
                menu.setItem(4, createButt_class.createButt(Material.GOLD_INGOT, "Баланс счёта7", "Название " + null + "\nБаланс: " + balance + "\nПосле снятия: " + after));
                menu.setItem(8, createButt_class.createButt(Material.SLIME_BALL, "Подтвердить", "Нажми, чтобы подтвердить."));
            }
        }
        else if (menu1.equals("perevod")){
            int after = balance - amount;
            if (amount <= 0) {
                menu.setItem(4, createButt_class.createButt(Material.GOLD_INGOT, "Баланс счёта99 ___", "Название " + null + "\nБаланс: " + balance + "\nПосле перевода: сумма не выбрана"));
                menu.setItem(8, createButt_class.createButt(Material.BARRIER, "Выберите сумму11312", "Сначала выберите сумму для перевода."));
            } else {
                menu.setItem(4, createButt_class.createButt(Material.GOLD_INGOT, "Баланс счёта7", "Название " + null + "\nБаланс: " + balance + "\nПосле перевода: " + after));
                menu.setItem(8, createButt_class.createButt(Material.SLIME_BALL, "Подтвердить", "Нажми, чтобы подтвердить."));
            }
        }
        else if (menu1.equals("give_menu")) {
            if (amount <= 0) {
                menu.setItem(2, createButt_class.createButt(Material.PLAYER_HEAD, "Выбрать игрока", "Текущий выбор: " + playerName));
                menu.setItem(8, createButt_class.createButt(Material.BARRIER, "Выдать штраф", ""));
            } else {menu.setItem(8, createButt_class.createButt(Material.SLIME_BALL, "Подтвердить", "Нажми, чтобы подтвердить."));}
            menu.setItem(4, createButt_class.createButt(Material.WRITABLE_BOOK, "Причина штрафа", ""));
        } else if (menu1.equals("remove_menu")) {
            String xuy = "не выбран";
            if (myself_class.selectedPlayerForFine != null) {xuy = Bukkit.getOfflinePlayer(myself_class.selectedPlayerForFine).getName();}
            if (fine_menu_class.nal_bezNal){menu.setItem(13, createButt_class.createButt(Material.END_CRYSTAL, "Способ оплаты", "Выбор: Наличными"));
            } else {menu.setItem(13, createButt_class.createButt(Material.END_CRYSTAL, "Способ оплаты", "Выбор: Безнал"));}
            if (amount <=0){
                menu.setItem(0, createButt_class.createButt(Material.PLAYER_HEAD, "Выбрать игрока", "Текущий выбор: " + playerName));
                menu.setItem(17, createButt_class.createButt(Material.BARRIER, "НЕЛьзя", ""));
                menu.setItem(11, createButt_class.createButt(Material.CREEPER_HEAD, "Перевести валюту игроку", "Нажмите, чтобы выбрать игрока" + "\nдля перевода валюты после снятия штрафа" + "\nТекущий выбор: " + xuy));
            } else {
                menu.setItem(0, createButt_class.createButt(Material.PLAYER_HEAD, "Выбрать игрока", "Текущий выбор: " + playerName));
                menu.setItem(11, createButt_class.createButt(Material.CREEPER_HEAD, "Перевести валюту игроку", "Нажмите, чтобы выбрать игрока" + "\nдля перевода валюты после снятия штрафа" + "\nТекущий выбор: " + xuy));
                menu.setItem(17, createButt_class.createButt(Material.BARRIER, "Снять штраф", ""));
            }
        }
    }
}
