package me.yourname.proba.GeneralSettings;

public class resetState_class {

    // Сброс состояния
    public static void resetState() {
        myself_class.selectedPlayer = null;
        myself_class.selectedPlayerForFine = null;
        PlayerChatAmount.amount = 0;
        PlayerChatMess.message1 = null;
    }
}
