package de.curano.explosionapi;

import de.curano.explosionapi.annotations.AnnotationProcessor;
import de.curano.explosionapi.items.ItemEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.logging.Logger;

public class ExplosionAPI {

    public static final String VERSION = "1.0";
    public static final Logger LOGGER = Bukkit.getLogger();
    public static final UUID SERVER_UUID = UUID.randomUUID();
    private static boolean enabled = false;

    public static void register(Plugin plugin) {
        if (!enabled) {
            enabled = true;
            Bukkit.getPluginManager().registerEvents(new ItemEvents(), plugin);
        }
        AnnotationProcessor.processRegister(plugin);
    }

    public static void unregister(Plugin plugin) {
        AnnotationProcessor.processUnregister(plugin);
    }

    public static boolean isExplosionItem(ItemStack item) {
        if(item == null || item.getType() == Material.AIR || item.getItemMeta() == null) return false;
        PersistentDataContainer persistentDataContainer = item.getItemMeta().getPersistentDataContainer();
        return persistentDataContainer.has(new NamespacedKey("explosionapi", "item"), PersistentDataType.STRING);
    }

}
