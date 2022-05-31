package de.curano.explosionapi.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.ItemMergeEvent;
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
import java.util.List;
import java.util.function.Consumer;

public class EItemStack extends ItemStack implements Serializable {


    public EItemStack(Material material) {
        super(material);
    }

    public EItemStack(Material material, int amount) {
        super(material, amount);
    }

    public EItemStack(Material material, int amount, String displayName) {
        super(material, amount);
        setDisplayName(displayName);
    }

    public EItemStack(ItemStack itemStack) {
        super(itemStack);
    }

    public void setDisplayName(String displayName) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.setItemMeta(itemMeta);
    }

    public String getDisplayName() {
        return this.getItemMeta().getDisplayName();
    }

    public void setLore(List<String> lore) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setLore(lore);
        this.setItemMeta(itemMeta);
    }

    public List<String> getLore() {
        return this.getItemMeta().getLore();
    }

    public void setInventoryClickEvent(SerializableConsumer<InventoryClickEvent> event) {
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
            PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
            persistentDataContainer.set(new NamespacedKey("ExplosionAPI", "explosion-inventoryclick"), PersistentDataType.STRING, consumer);
        }
    }

    public SerializableConsumer<InventoryClickEvent> getInventoryClickEvent() {
        ItemMeta meta = this.getItemMeta();
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        if (persistentDataContainer.has(new NamespacedKey("ExplosionAPI", "explosion-inventoryclick"), PersistentDataType.STRING)) {
            String data = persistentDataContainer.get(new NamespacedKey("ExplosionAPI", "explosion-inventoryclick"), PersistentDataType.STRING);
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
                SerializableConsumer<InventoryClickEvent> inventoryClickEvent = (SerializableConsumer<InventoryClickEvent>) dataInput.readObject();
                dataInput.close();
                return inventoryClickEvent;
            } catch (Exception e) {
                System.out.println(data);
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

    public void setPlayerInteractEvent(SerializableConsumer<PlayerInteractEvent> event) {
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
            PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
            persistentDataContainer.set(new NamespacedKey("ExplosionAPI", "explosion-playerinteract"), PersistentDataType.STRING, consumer);
        }
    }

    public SerializableConsumer<PlayerInteractEvent> getPlayerInteractEvent() {
        ItemMeta meta = this.getItemMeta();
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        if (persistentDataContainer.has(new NamespacedKey("ExplosionAPI", "explosion-playerinteract"), PersistentDataType.STRING)) {
            String data = persistentDataContainer.get(new NamespacedKey("ExplosionAPI", "explosion-playerinteract"), PersistentDataType.STRING);
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                SerializableConsumer<PlayerInteractEvent> playerInteractEvent = (SerializableConsumer<PlayerInteractEvent>) dataInput.readObject();


                dataInput.close();
                return playerInteractEvent;
            } catch (Exception e) {
                System.out.println(data);
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
