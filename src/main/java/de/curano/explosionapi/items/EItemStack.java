package de.curano.explosionapi.items;

import de.curano.explosionapi.ExplosionAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

public class EItemStack extends ItemStack implements Serializable {

    private void markItem() {
        ItemMeta itemMeta = this.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(new NamespacedKey("explosionapi", "explosion-item"), PersistentDataType.STRING, "true");
        setItemMeta(itemMeta);
    }

    public EItemStack(Material material) {
        super(material);
        markItem();
    }

    public EItemStack(Material material, int amount) {
        super(material, amount);
        markItem();
    }

    public EItemStack(Material material, int amount, String displayName) {
        super(material, amount);
        setDisplayName(displayName);
        markItem();
    }

    public EItemStack(Material material, String displayName) {
        super(material);
        setDisplayName(displayName);
        markItem();
    }

    public EItemStack(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        assert itemMeta != null;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (!persistentDataContainer.has(new NamespacedKey("explosionapi", "explosion-item"), PersistentDataType.STRING)) {
            persistentDataContainer.set(new NamespacedKey("explosionapi", "explosion-item"), PersistentDataType.STRING, "true");
        }
        return super.setItemMeta(itemMeta);
    }

    public EItemStack setDisplayName(String displayName) {
        ItemMeta itemMeta = this.getItemMeta();
        if (itemMeta == null) return this;
        itemMeta.setDisplayName(displayName);
        this.setItemMeta(itemMeta);
        return this;
    }

    public String getDisplayName() {
        if (this.getItemMeta() == null) return this.getType().name();
        return this.getItemMeta().getDisplayName();
    }

    public EItemStack setLore(List<String> lore) {
        ItemMeta itemMeta = this.getItemMeta();
        if (itemMeta == null) return this;
        itemMeta.setLore(lore);
        this.setItemMeta(itemMeta);
        return this;
    }

    public List<String> getLore() {
        if (this.getItemMeta() == null) return new ArrayList<>();
        return this.getItemMeta().getLore();
    }

    public EItemStack setInventoryClickEvent(SerializableConsumer<InventoryClickEvent> event) {
        String consumer = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(event);
            dataOutput.close();
            consumer = Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (consumer != null) {
            ItemMeta meta = this.getItemMeta();
            if (meta == null) return this;
            PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
            persistentDataContainer.set(new NamespacedKey("explosionapi", "explosion-inventoryclick"), PersistentDataType.STRING, consumer);
            this.setItemMeta(meta);
        }
        return this;
    }

    public SerializableConsumer<InventoryClickEvent> getInventoryClickEvent() {
        ItemMeta meta = this.getItemMeta();
        if (meta == null) return event -> {};
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        if (persistentDataContainer.has(new NamespacedKey("explosionapi", "explosion-inventoryclick"), PersistentDataType.STRING)) {
            String data = persistentDataContainer.get(new NamespacedKey("explosionapi", "explosion-inventoryclick"), PersistentDataType.STRING);
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
                SerializableConsumer<InventoryClickEvent> inventoryClickEvent = (SerializableConsumer<InventoryClickEvent>) dataInput.readObject();
                dataInput.close();
                return inventoryClickEvent;
            } catch (Exception e) {
                ExplosionAPI.LOGGER.log(Level.WARNING, "ExplosionAPI Error Data: " + data);
                e.printStackTrace();
            }
        }
        return null;
    }

    protected void triggerInventoryClickEvent(InventoryClickEvent event) {
        Consumer<InventoryClickEvent> consumer = getInventoryClickEvent();
        if (consumer != null) {
            consumer.accept(event);
        }
    }

    public EItemStack setPlayerInteractEvent(SerializableConsumer<PlayerInteractEvent> event) {
        String consumer = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(event);
            dataOutput.close();
            consumer = Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (consumer != null) {
            ItemMeta meta = this.getItemMeta();
            if (meta == null) return this;
            PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
            persistentDataContainer.set(new NamespacedKey("explosionapi", "explosion-playerinteract"), PersistentDataType.STRING, consumer);
        }
        return this;
    }

    public SerializableConsumer<PlayerInteractEvent> getPlayerInteractEvent() {
        ItemMeta meta = this.getItemMeta();
        if (meta == null) return event -> {};
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        if (persistentDataContainer.has(new NamespacedKey("explosionapi", "explosion-playerinteract"), PersistentDataType.STRING)) {
            String data = persistentDataContainer.get(new NamespacedKey("explosionapi", "explosion-playerinteract"), PersistentDataType.STRING);
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
                SerializableConsumer<PlayerInteractEvent> playerInteractEvent = (SerializableConsumer<PlayerInteractEvent>) dataInput.readObject();
                dataInput.close();
                return playerInteractEvent;
            } catch (Exception e) {
                ExplosionAPI.LOGGER.log(Level.WARNING, "ExplosionAPI Error Data: " + data);
                e.printStackTrace();
            }
        }
        return null;
    }

    protected void triggerPlayerInteractEvent(PlayerInteractEvent event) {
        Consumer<PlayerInteractEvent> consumer = getPlayerInteractEvent();
        if (consumer != null) {
            consumer.accept(event);
        }
    }

}
