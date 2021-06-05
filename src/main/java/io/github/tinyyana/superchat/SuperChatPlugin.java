package io.github.tinyyana.superchat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SuperChatPlugin extends JavaPlugin {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        loadConfig();
        getCommand("superchat").setExecutor(new SuperChatCommand(this));
    }

    @Override
    public void onDisable() {
    }

    public void loadConfig() {
        config.options().copyDefaults(true);
        this.saveDefaultConfig();
        this.getConfig();
        saveConfig();
    }
}
