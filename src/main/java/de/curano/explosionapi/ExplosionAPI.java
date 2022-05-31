package de.curano.explosionapi;

import de.curano.explosionapi.annotations.AnnotationProcessor;
import de.curano.explosionapi.items.ItemEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplosionAPI {

    public static final String VERSION = "1.0 BETA";
    public static boolean enabled = false;

    public static void register(JavaPlugin plugin) {
        if (!enabled) {
            enabled = true;

            Bukkit.getPluginManager().registerEvents(new ItemEvents(), plugin);
        }
        AnnotationProcessor.processRegister(plugin);
    }

    public static void unregister(JavaPlugin plugin) {
        AnnotationProcessor.processUnregister(plugin);
    }

}
