package de.curano.explosionapi.items;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.io.Serializable;

public class PDCObject implements Serializable {

    private static NamespacedKey key;
    private static PersistentDataType type;
    private static Object value;

    public PDCObject(NamespacedKey key, PersistentDataType type, Object value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }

    public static NamespacedKey getKey() {
        return key;
    }

    public static PersistentDataType getType() {
        return type;
    }

    public static Object getValue() {
        return value;
    }

}
