package de.curano.explosionapi;

import de.curano.explosionapi.items.PDCObject;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Base64Converter {

    private static final List<PersistentDataType> persistentDataTypes = List.of(PersistentDataType.BYTE, PersistentDataType.SHORT, PersistentDataType.INTEGER, PersistentDataType.LONG, PersistentDataType.FLOAT, PersistentDataType.DOUBLE, PersistentDataType.STRING, PersistentDataType.BYTE_ARRAY, PersistentDataType.INTEGER_ARRAY, PersistentDataType.LONG_ARRAY, PersistentDataType.TAG_CONTAINER_ARRAY, PersistentDataType.TAG_CONTAINER);

    // Be careful, this method will not work for all objects, only for those that implement Serializable
    public static String toString(Object obj) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(obj);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String itemStackToString(ItemStack obj) {
        ArrayList<PDCObject> pdc = new ArrayList<>();
        Map<String, Object> itemStackData = obj.serialize();
        if (obj.getItemMeta() == null) {
            return null;
        }
        PersistentDataContainer persistentDataContainer = obj.getItemMeta().getPersistentDataContainer();
        for (NamespacedKey key : obj.getItemMeta().getPersistentDataContainer().getKeys()) {
            for (PersistentDataType type : persistentDataTypes) {
                try {
                    Object value = persistentDataContainer.get(key, type);
                    if (value != null) {
                        pdc.add(new PDCObject(key, type, persistentDataContainer.get(key, type)));
                    }
                } catch (IllegalArgumentException ignore) {

                }
            }
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStackData);
            dataOutput.writeObject((List) pdc);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String itemStacksToString(ItemStack[] inventory) {
        String[] itemStacks = new String[inventory.length];
        for (int i = 0; i < inventory.length; i++) {
            itemStacks[i] = itemStackToString(inventory[i]);
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.length);
            for (int i = 0; i < inventory.length; i++) {
                dataOutput.writeUTF(itemStacks[i]);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack[] fromStringToItemStacks(String string) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(string));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] itemStacks = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < itemStacks.length; i++) {
                itemStacks[i] = fromStringToItemStack(dataInput.readUTF());
            }
            dataInput.close();
            return itemStacks;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object fromString(String data) {
        if (data != null) {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
                Object obj = dataInput.readObject();
                dataInput.close();
                return obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ItemStack fromStringToItemStack(String data) {
        if (data != null) {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
                Object obj = dataInput.readObject();
                Object pdcObj = null;
                try {
                    pdcObj = dataInput.readObject();
                } catch (Exception ignore) {
                }
                dataInput.close();
                if (obj instanceof Map) {
                    ItemStack itemStack = ItemStack.deserialize((Map<String, Object>) obj);
                    if (pdcObj instanceof List<?>) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        List<PDCObject> pdc = (List<PDCObject>) pdcObj;
                        for (PDCObject pdcObject : pdc) {
                            itemMeta.getPersistentDataContainer().set(pdcObject.getKey(), pdcObject.getType(), pdcObject.getValue());
                        }
                        itemStack.setItemMeta(itemMeta);
                    }
                    return itemStack;
                } else if (obj instanceof ItemStack) {
                    return (ItemStack) obj;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
