package me.seetch.sixSchedules;

import me.seetch.sixSchedules.command.SixSchedulesCommand;
import me.seetch.sixSchedules.manager.ScheduleManager;
import me.seetch.sixSchedules.placeholder.SchedulePlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SixSchedules extends JavaPlugin {
    private ScheduleManager scheduleManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        scheduleManager = new ScheduleManager(this);
        scheduleManager.loadEvents();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SchedulePlaceholder(this, scheduleManager).register();
        }
        getCommand("sixschedules").setExecutor(new SixSchedulesCommand(this));
    }

    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    public void reloadPluginConfig() {
        reloadConfig();
        scheduleManager.loadEvents();
    }
}
