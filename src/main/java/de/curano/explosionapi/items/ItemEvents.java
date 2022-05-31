package de.curano.explosionapi.items;

import de.curano.explosionapi.ExplosionAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

public class ItemEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null
                && event.getCurrentItem().getType() != Material.AIR
                && ExplosionAPI.isExplosionItem(event.getCurrentItem())) {
            SerializableConsumer<InventoryClickEvent> consumer = new EItemStack(event.getCurrentItem()).getInventoryClickEvent();
            if (consumer != null) {
                consumer.accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null
                && event.getItem().getType() != Material.AIR
                && ExplosionAPI.isExplosionItem(event.getItem())) {
            SerializableConsumer<PlayerInteractEvent> consumer = new EItemStack(event.getItem()).getPlayerInteractEvent();
            if (consumer != null) {
                consumer.accept(event);
            }
        }
    }

}
