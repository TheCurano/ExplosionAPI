package de.curano.explosionapi.inventorys;

import de.curano.explosionapi.annotations.Events;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.function.Consumer;

@Events
public class InventoryEvents implements Listener {

    protected static HashMap<Inventory, Consumer<InventoryInteractEvent>> inventoryInteractEvents = new HashMap<>();
    protected static HashMap<Inventory, Consumer<InventoryClickEvent>> inventoryClickEvents = new HashMap<>();
    protected static HashMap<Inventory, Consumer<InventoryCloseEvent>> inventoryCloseEvent = new HashMap<>();

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        Consumer<InventoryInteractEvent> consumer = inventoryInteractEvents.get(event.getInventory());
        if (consumer != null) {
            consumer.accept(event);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Consumer<InventoryClickEvent> consumer = inventoryClickEvents.get(event.getInventory());
        if (consumer != null) {
            consumer.accept(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Consumer<InventoryCloseEvent> consumer = inventoryCloseEvent.get(event.getInventory());
        if (consumer != null) {
            consumer.accept(event);
        }
    }

    protected static void setInventoryInteract(Inventory inventory, Consumer<InventoryInteractEvent> consumer) {
        if (inventoryInteractEvents.containsKey(inventory)) {
            inventoryInteractEvents.replace(inventory, consumer);
        } else {
            inventoryInteractEvents.put(inventory, consumer);
        }
    }

    protected static void setInventoryClick(Inventory inventory, Consumer<InventoryClickEvent> consumer) {
        if (inventoryClickEvents.containsKey(inventory)) {
            inventoryClickEvents.replace(inventory, consumer);
        } else {
            inventoryClickEvents.put(inventory, consumer);
        }
    }

    protected static void setInventoryClose(Inventory inventory, Consumer<InventoryCloseEvent> consumer) {
        if (consumer == null) {
            inventoryCloseEvent.remove(inventory);
            return;
        }
        if (inventoryCloseEvent.containsKey(inventory)) {
            inventoryCloseEvent.replace(inventory, consumer);
        } else {
            inventoryCloseEvent.put(inventory, consumer);
        }
    }
}
