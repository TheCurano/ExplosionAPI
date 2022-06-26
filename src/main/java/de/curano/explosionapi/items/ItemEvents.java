package de.curano.explosionapi.items;

import de.curano.explosionapi.ExplosionAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.Consumer;

public class ItemEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null
                && event.getCurrentItem().getType() != Material.AIR
                && ExplosionAPI.isExplosionItem(event.getCurrentItem())) {
            EItemStack itemStack = new EItemStack(event.getCurrentItem());
            Consumer<InventoryClickEvent> permConsumer = itemStack.getInventoryClickEvent(Timing.PERMANENTLY);
            if (permConsumer != null) {
                permConsumer.accept(event);
            }
            Consumer<InventoryClickEvent> tempConsumer = itemStack.getInventoryClickEvent(Timing.TEMPORARY);
            if (tempConsumer != null) {
                tempConsumer.accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null
                && event.getItem().getType() != Material.AIR
                && ExplosionAPI.isExplosionItem(event.getItem())) {
            EItemStack itemStack = new EItemStack(event.getItem());
            Consumer<PlayerInteractEvent> permConsumer = itemStack.getPlayerInteractEvent(Timing.PERMANENTLY);
            if (permConsumer != null) {
                permConsumer.accept(event);
            }
            Consumer<PlayerInteractEvent> tempConsumer = itemStack.getPlayerInteractEvent(Timing.TEMPORARY);
            if (tempConsumer != null) {
                tempConsumer.accept(event);
            }
        }
    }

}
