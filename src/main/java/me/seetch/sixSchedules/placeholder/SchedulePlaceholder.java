package me.seetch.sixSchedules.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.seetch.sixSchedules.manager.ScheduleManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SchedulePlaceholder extends PlaceholderExpansion {
    private final Plugin plugin;
    private final ScheduleManager scheduleManager;

    public SchedulePlaceholder(Plugin plugin, ScheduleManager scheduleManager) {
        this.plugin = plugin;
        this.scheduleManager = scheduleManager;
    }

    @Override
    public String getIdentifier() {
        return "sixschedules";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.startsWith("nextin_")) {
            String[] parts = identifier.split("_");
            if (parts.length < 3) {
                return null;
            }

            String eventName = parts[1];
            String format = parts[2];

            long timeLeft = scheduleManager.getTimeUntilNextEvent(eventName);

            if (timeLeft == -1) {
                return "Событие не найдено или отключено.";
            }

            if (format.equals("string")) {
                return formatTimeString(timeLeft);
            } else {
                return formatTime(timeLeft, format);
            }
        }
        return null;
    }

    private String formatTime(long seconds, String format) {
        long targetTime = System.currentTimeMillis() + seconds * 1000;
        Date targetDate = new Date(targetTime);

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(targetDate);
        } catch (IllegalArgumentException e) {
            return "Неверный формат даты.";
        }
    }

    private String formatTimeString(long seconds) {
        long days = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays(seconds));
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds));

        StringBuilder timeString = new StringBuilder();

        if (days > 0) {
            timeString.append(days).append(" дн. ");
        }
        if (hours > 0) {
            timeString.append(hours).append(" ч. ");
        }
        if (minutes > 0) {
            timeString.append(minutes).append(" мин. ");
        }

        return timeString.toString().trim();
    }
}
