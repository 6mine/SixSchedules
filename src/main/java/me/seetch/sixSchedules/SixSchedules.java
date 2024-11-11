package me.seetch.sixSchedules;

import me.seetch.sixSchedules.command.SixSchedulesCommand;
import me.seetch.sixSchedules.manager.ScheduleManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SixSchedules extends JavaPlugin {
    private ScheduleManager scheduleManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        scheduleManager = new ScheduleManager(this);
        scheduleManager.loadEvents();
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
