package de.curano.explosionapi.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null
                && event.getCurrentItem().getType() != Material.AIR
                && event.getCurrentItem() instanceof EItemStack) {
            SerializableConsumer<InventoryClickEvent> consumer = ((EItemStack) event.getCurrentItem()).getInventoryClickEvent();
            if (consumer != null) {
                consumer.accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null
                && event.getItem().getType() != Material.AIR
                && event.getItem() instanceof EItemStack) {
            SerializableConsumer<PlayerInteractEvent> consumer = ((EItemStack) event.getItem()).getPlayerInteractEvent();
            if (consumer != null) {
                consumer.accept(event);
            }
        }
    }

}
