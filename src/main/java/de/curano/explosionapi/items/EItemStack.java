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
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class EItemStack extends ItemStack implements Serializable {

    private static HashMap<UUID, Consumer<InventoryClickEvent>> clickEvents = new HashMap<>();
    private static HashMap<UUID, Consumer<PlayerInteractEvent>> interactEvents = new HashMap<>();

    private void markItem() {
        ItemMeta itemMeta = this.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(new NamespacedKey("explosionapi", "item"), PersistentDataType.STRING, "true");
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
        if (!persistentDataContainer.has(new NamespacedKey("explosionapi", "item"), PersistentDataType.STRING)) {
            persistentDataContainer.set(new NamespacedKey("explosionapi", "item"), PersistentDataType.STRING, "true");
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






    public EItemStack setInventoryClickEvent(Consumer<InventoryClickEvent> clickEvent, Timing timing) {
        ItemMeta meta = this.getItemMeta();
        if (meta == null) return this;
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        if (clickEvent == null) {
            if (timing == Timing.PERMANENTLY) {
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "inventoryclick"));
            } else if (timing == Timing.TEMPORARY) {
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "inventoryclick-temp"));
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "inventoryclick-serveruuid"));
            }
            return this;
        }

        String consumer = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(clickEvent);
            dataOutput.close();
            consumer = Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (timing == Timing.PERMANENTLY && consumer != null) {
            persistentDataContainer.set(new NamespacedKey("explosionapi", "inventoryclick"), PersistentDataType.STRING, consumer);
        } else if (timing == Timing.TEMPORARY && consumer != null) {
            persistentDataContainer.set(new NamespacedKey("explosionapi", "inventoryclick-temp"), PersistentDataType.STRING, consumer);
            persistentDataContainer.set(new NamespacedKey("explosionapi", "inventoryclick-serveruuid"), PersistentDataType.STRING, ExplosionAPI.SERVER_UUID.toString());
        }
        return this;
    }

    public EItemStack setPlayerInteractEvent(Consumer<PlayerInteractEvent> interactEvent, Timing timing) {
        ItemMeta meta = this.getItemMeta();
        if (meta == null) return this;
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        if (interactEvent == null) {
            if (timing == Timing.PERMANENTLY) {
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "playerinteract"));
            } else if (timing == Timing.TEMPORARY) {
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "playerinteract-temp"));
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "playerinteract-serveruuid"));
            }
            return this;
        }

        String consumer = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(interactEvent);
            dataOutput.close();
            consumer = Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (consumer == null) return this;

        if (timing == Timing.PERMANENTLY) {
            persistentDataContainer.set(new NamespacedKey("explosionapi", "playerinteract"), PersistentDataType.STRING, consumer);
        } else if (timing == Timing.TEMPORARY) {
            persistentDataContainer.set(new NamespacedKey("explosionapi", "playerinteract-temp"), PersistentDataType.STRING, consumer);
            persistentDataContainer.set(new NamespacedKey("explosionapi", "playerinteract-serveruuid"), PersistentDataType.STRING, ExplosionAPI.SERVER_UUID.toString());
        }
        return this;
    }

    public SerializableConsumer<InventoryClickEvent> getInventoryClickEvent(Timing timing) {
        ItemMeta meta = this.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        if (timing == Timing.PERMANENTLY) {
            if (persistentDataContainer.has(new NamespacedKey("explosionapi", "inventoryclick"), PersistentDataType.STRING)) {
                String data = persistentDataContainer.get(new NamespacedKey("explosionapi", "inventoryclick"), PersistentDataType.STRING);
                if (data == null) return null;
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
        } else if (timing == Timing.TEMPORARY) {
            if (persistentDataContainer.has(new NamespacedKey("explosionapi", "inventoryclick-serveruuid"), PersistentDataType.STRING)
                    && !persistentDataContainer.get(new NamespacedKey("explosionapi", "inventoryclick-serveruuid"), PersistentDataType.STRING).equals(ExplosionAPI.SERVER_UUID.toString())) {
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "inventoryclick-temp"));
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "inventoryclick-serveruuid"));
                return null;
            }
            if (persistentDataContainer.has(new NamespacedKey("explosionapi", "inventoryclick-temp"), PersistentDataType.STRING)) {
                String data = persistentDataContainer.get(new NamespacedKey("explosionapi", "inventoryclick-temp"), PersistentDataType.STRING);
                if (data == null) return null;
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
        }
        return null;
    }

    public SerializableConsumer<PlayerInteractEvent> getPlayerInteractEvent(Timing timing) {
        ItemMeta meta = this.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        if (timing == Timing.PERMANENTLY) {
            if (persistentDataContainer.has(new NamespacedKey("explosionapi", "playerinteract"), PersistentDataType.STRING)) {
                String data = persistentDataContainer.get(new NamespacedKey("explosionapi", "playerinteract"), PersistentDataType.STRING);
                if (data == null) return null;
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
        } else if (timing == Timing.TEMPORARY) {
            if (persistentDataContainer.has(new NamespacedKey("explosionapi", "playerinteract-serveruuid"), PersistentDataType.STRING)
                    && !persistentDataContainer.get(new NamespacedKey("explosionapi", "playerinteract-serveruuid"), PersistentDataType.STRING).equals(ExplosionAPI.SERVER_UUID.toString())) {
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "playerinteract-temp"));
                persistentDataContainer.remove(new NamespacedKey("explosionapi", "playerinteract-serveruuid"));
                return null;
            }
            if (persistentDataContainer.has(new NamespacedKey("explosionapi", "playerinteract-temp"), PersistentDataType.STRING)) {
                String data = persistentDataContainer.get(new NamespacedKey("explosionapi", "playerinteract-temp"), PersistentDataType.STRING);
                if (data == null) return null;
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
        }
        return null;
    }

    protected UUID setUUID(UUID uuid) {
        if (uuid == null) uuid = UUID.randomUUID();
        if (this.getItemMeta() == null) return uuid;
        PersistentDataContainer persistentDataContainer = this.getItemMeta().getPersistentDataContainer();
        persistentDataContainer.set(new NamespacedKey("explosionapi", "uuid"), PersistentDataType.STRING, uuid.toString());
        return uuid;
    }

    public UUID getUUID() {
        if (this.getItemMeta() == null) return setUUID(UUID.randomUUID());
        if (!this.getItemMeta().getPersistentDataContainer().has(new NamespacedKey("explosionapi", "uuid"), PersistentDataType.STRING)) return setUUID(UUID.randomUUID());
        PersistentDataContainer persistentDataContainer = this.getItemMeta().getPersistentDataContainer();
        return UUID.fromString(Objects.requireNonNull(persistentDataContainer.get(new NamespacedKey("explosionapi", "uuid"), PersistentDataType.STRING)));
    }
}
