package de.curano.explosionapi.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class InventoryBuilder {

    private Inventory inventory;

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
        Inventory oldInv = this.inventory;
        this.inventory = Bukkit.createInventory(oldInv.getHolder(), oldInv.getType(), title);
        this.inventory.setContents(oldInv.getContents());
        this.inventory.setStorageContents(oldInv.getStorageContents());
        this.inventory.setMaxStackSize(oldInv.getMaxStackSize());
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

    public Inventory build() {
        return this.inventory;
    }

}
