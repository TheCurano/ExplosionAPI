package de.curano.explosionapi.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class InventoryBuilder {

    private Inventory inventory;
    private boolean builded = false;

    public InventoryBuilder(InventoryHolder holder, int slots, String title) {
        this.inventory = Bukkit.createInventory(holder, slots, title);
    }

    public InventoryBuilder(InventoryHolder holder, int slots) {
        this.inventory = Bukkit.createInventory(holder, slots);
    }

    public InventoryBuilder(int slots) {
        this.inventory = Bukkit.createInventory(null, slots);
    }

    public InventoryBuilder(int slots, String title) {
        this.inventory = Bukkit.createInventory(null, slots, title);
    }

    public InventoryBuilder(InventoryType type) {
        this.inventory = Bukkit.createInventory(null, type);
    }

    public InventoryBuilder(InventoryType type, String title) {
        this.inventory = Bukkit.createInventory(null, type, title);
    }

    public InventoryBuilder(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryBuilder setItem(int slot, ItemStack item) {
        this.inventory.setItem(slot, item);
        return this;
    }

    public InventoryBuilder setItem(int slot, ItemStack item, int amount) {
        item.setAmount(amount);
        this.inventory.setItem(slot, item);
        return this;
    }

    public InventoryBuilder setTitle(String title) {
        if (builded) {
            throw new IllegalStateException("Inventory already builded.");
        }
        Inventory oldInv = this.inventory;
        this.inventory = Bukkit.createInventory(oldInv.getHolder(), oldInv.getType(), title);
        this.inventory.setContents(oldInv.getContents());
        this.inventory.setStorageContents(oldInv.getStorageContents());
        this.inventory.setMaxStackSize(oldInv.getMaxStackSize());
        if (InventoryEvents.inventoryCloseEvent.get(oldInv) != null) {
            InventoryEvents.setInventoryClose(this.inventory, InventoryEvents.inventoryCloseEvent.get(oldInv));
        }
        if (InventoryEvents.inventoryClickEvents.get(oldInv) != null) {
            InventoryEvents.setInventoryClick(this.inventory, InventoryEvents.inventoryClickEvents.get(oldInv));
        }
        oldInv.clear();
        return this;
    }

    public InventoryBuilder setMaxStackSize(int maxStackSize) {
        this.inventory.setMaxStackSize(maxStackSize);
        return this;
    }

    public InventoryBuilder fillEmptySlots(ItemStack itemStack) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null) {
                this.inventory.setItem(i, itemStack);
            }
        }
        return this;
    }

    public InventoryBuilder replaceItems(ItemStack itemStack, ItemStack newItemStack) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) != null && this.inventory.getItem(i).isSimilar(itemStack)) {
                this.inventory.setItem(i, newItemStack);
            }
        }
        return this;
    }

    public InventoryBuilder replaceItems(Material material, ItemStack newItemStack) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) != null && this.inventory.getItem(i).getType() == material) {
                this.inventory.setItem(i, newItemStack);
            }
        }
        return this;
    }

    public InventoryBuilder setInventoryCloseEvent(Consumer<InventoryCloseEvent> consumer) {
        InventoryEvents.setInventoryClose(this.inventory, consumer);
        return this;
    }

    public InventoryBuilder setInventoryClickEvent(Consumer<InventoryClickEvent> consumer) {
        InventoryEvents.setInventoryClick(this.inventory, consumer);
        return this;
    }

    public InventoryBuilder removeInventoryCloseEvent() {
        InventoryEvents.setInventoryClose(this.inventory, null);
        return this;
    }

    public InventoryBuilder removeInventoryClickEvent() {
        InventoryEvents.setInventoryClick(this.inventory, null);
        return this;
    }

    public Consumer<InventoryCloseEvent> getInventoryCloseEvent() {
        return InventoryEvents.inventoryCloseEvent.get(this.inventory);
    }

    public Consumer<InventoryClickEvent> getInventoryClickEvent() {
        return InventoryEvents.inventoryClickEvents.get(this.inventory);
    }

    public Inventory build() {
        builded = true;
        return this.inventory;
    }

}
