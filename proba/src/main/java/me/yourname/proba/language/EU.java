package me.yourname.proba.language;

import me.yourname.proba.DataManager;

import java.util.*;

public class EU {

    public static DataManager dataManager;

    public enum MessageCategory {
        MAIN_MENU,
        SETTINGS,
        // добавьте сколько нужно
    }

    private static final Map<MessageCategory, Map<Boolean, String[]>> MESSAGES = new HashMap<>();

    static {
        // Инициализация для категории MAIN_MENU
        Map<Boolean, String[]> mainMenuMap = new HashMap<>();
        mainMenuMap.put(true, new String[]{"Главное меню", "Играть", "Настройки", "Выход"});
        mainMenuMap.put(false, new String[]{"Main Menu", "Play", "Settings", "Exit"});
        MESSAGES.put(MessageCategory.MAIN_MENU, mainMenuMap);

        // Категория SETTINGS
        Map<Boolean, String[]> settingsMap = new HashMap<>();
        settingsMap.put(true, new String[]{"Настройки", "Звук", "Язык", "Графика"});
        settingsMap.put(false, new String[]{"Settings", "Sound", "Language", "Graphics"});
        MESSAGES.put(MessageCategory.SETTINGS, settingsMap);
    }

    public static String[] Messages(MessageCategory category) {
        boolean lang = dataManager.getLanguage();
        Map<Boolean, String[]> categoryMap = MESSAGES.get(category);
        if (categoryMap == null) {
            return new String[0]; // или бросить исключение, если категория не найдена
        }
        return categoryMap.get(lang);
    }
}
