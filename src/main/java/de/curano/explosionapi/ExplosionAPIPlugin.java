package de.curano.explosionapi;

import org.bukkit.plugin.java.JavaPlugin;

public class ExplosionAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ExplosionAPI.register(this);
    }
}
