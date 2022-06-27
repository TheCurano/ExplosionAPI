package de.curano.explosionapi.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DBList {

    private DBManager dbManager;
    private String listName;
    private int ttlCache = 30000;
    private boolean cacheEnabled = false;
    private Cache cache = null;

    public DBList(DBManager dbManager, String listName) {
        this.dbManager = dbManager;
        this.listName = listName;
        createIfNotExists();
    }

    public DBList enableCache() {
        this.cache = new Cache(ttlCache);
        cacheEnabled = true;
        return this;
    }

    // In milliseconds
    public DBList setTTL(int ttl) {
        this.ttlCache = ttl;
        this.cache.setTTL(ttl);
        return this;
    }

    public int getTTL() {
        return ttlCache;
    }

    public DBList disableCache() {
        return this;
    }

    public DBList clearCache() {
        this.cache.clear();
        return this;
    }

    /**
     * Types: string 1 | int 2 | long 3 | double 4 | boolean 5 | float 6
     */
    private void createIfNotExists() {
        dbManager.createTable(listName + "(name VARCHAR(256), type BIT, value VARCHAR(4096))");
    }

    public String getString(String name) {
        if (cacheEnabled) {
            String cacheResult = (String) this.cache.get("1:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 1 AND name = '" + name + "';");
        try {
            if (result.next()) {
                return result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getInt(String name) {
        if (cacheEnabled) {
            Integer cacheResult = (Integer) this.cache.get("2:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 2 AND name = '" + name + "';");
        try {
            if (result.next()) {
                return Integer.parseInt(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getLong(String name) {
        if (cacheEnabled) {
            Long cacheResult = (Long) this.cache.get("3:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 3 AND name = '" + name + "';");
        try {
            if (result.next()) {
                return Long.parseLong(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getDouble(String name) {
        if (cacheEnabled) {
            Double cacheResult = (Double) this.cache.get("4:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 4 AND name = '" + name + "';");
        try {
            if (result.next()) {
                return Double.parseDouble(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean getBoolean(String name) {
        if (cacheEnabled) {
            Boolean cacheResult = (Boolean) this.cache.get("5:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 5 AND name = '" + name + "';");
        try {
            if (result.next()) {
                return Boolean.parseBoolean(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Float getFloat(String name) {
        if (cacheEnabled) {
            Float cacheResult = (Float) this.cache.get("6:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 6  AND name = '" + name + "';");
        try {
            if (result.next()) {
                return Float.parseFloat(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStringOrDefault(String name, String defaultValue) {
        if (cacheEnabled) {
            String cacheResult = (String) this.cache.get("1:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        String value = getString(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Integer getIntOrDefault(String name, Integer defaultValue) {
        if (cacheEnabled) {
            Integer cacheResult = (Integer) this.cache.get("2:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        Integer value = getInt(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Long getLongOrDefault(String name, Long defaultValue) {
        if (cacheEnabled) {
            Long cacheResult = (Long) this.cache.get("3:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        Long value = getLong(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Double getDoubleOrDefault(String name, Double defaultValue) {
        if (cacheEnabled) {
            Double cacheResult = (Double) this.cache.get("4:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        Double value = getDouble(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Boolean getBooleanOrDefault(String name, Boolean defaultValue) {
        if (cacheEnabled) {
            Boolean cacheResult = (Boolean) this.cache.get("5:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        Boolean value = getBoolean(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Float getFloatOrDefault(String name, Float defaultValue) {
        if (cacheEnabled) {
            Float cacheResult = (Float) this.cache.get("6:" + name);
            if (cacheResult != null) {
                return cacheResult;
            }
        }
        Float value = getFloat(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public void removeString(String name) {
        if (cacheEnabled) {
            this.cache.remove("1:" + name);
        }
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 1;");
    }

    public void removeInt(String name) {
        if (cacheEnabled) {
            this.cache.remove("2:" + name);
        }
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 2;");
    }

    public void removeLong(String name) {
        if (cacheEnabled) {
            this.cache.remove("3:" + name);
        }
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 3;");
    }

    public void removeDouble(String name) {
        if (cacheEnabled) {
            this.cache.remove("4:" + name);
        }
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 4;");
    }

    public void removeBoolean(String name) {
        if (cacheEnabled) {
            this.cache.remove("5:" + name);
        }
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 5;");
    }

    public void removeFloat(String name) {
        if (cacheEnabled) {
            this.cache.remove("6:" + name);
        }
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 6;");
    }

    public void set(String name, String value) {
        if (cacheEnabled) {
            this.cache.set("1:" + name, value);
        }
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO " + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 1, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, int value) {
        if (cacheEnabled) {
            this.cache.set("2:" + name, value);
        }
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO " + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 2, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, long value) {
        if (cacheEnabled) {
            this.cache.set("3:" + name, value);
        }
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO '" + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 3, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, double value) {
        if (cacheEnabled) {
            this.cache.set("4:" + name, value);
        }
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO " + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 4, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, boolean value) {
        if (cacheEnabled) {
            this.cache.set("5:" + name, value);
        }
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO " + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 5, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, float value) {
        if (cacheEnabled) {
            this.cache.set("6:" + name, value);
        }
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO " + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 6, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public HashMap<String, String> getAllStrings() {
        HashMap<String, String> map = new HashMap<>();
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT name, value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 1;");
        try {
            while (result.next()) {
                map.put(result.getString(1), result.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public HashMap<String, Integer> getAllInts() {
        HashMap<String, Integer> map = new HashMap<>();
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT name, value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 2;");
        try {
            while (result.next()) {
                map.put(result.getString(1), Integer.parseInt(result.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public HashMap<String, Long> getAllLongs() {
        HashMap<String, Long> map = new HashMap<>();
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT name, value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 3;");
        try {
            while (result.next()) {
                map.put(result.getString(1), Long.parseLong(result.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public HashMap<String, Double> getAllDoubles() {
        HashMap<String, Double> map = new HashMap<>();
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT name, value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 4;");
        try {
            while (result.next()) {
                map.put(result.getString(1), Double.parseDouble(result.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public HashMap<String, Boolean> getAllBooleans() {
        HashMap<String, Boolean> map = new HashMap<>();
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT name, value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 5;");
        try {
            while (result.next()) {
                map.put(result.getString(1), Boolean.parseBoolean(result.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public HashMap<String, Float> getAllFloats() {
        HashMap<String, Float> map = new HashMap<>();
        ResultSet result = dbManager.getDatabase().executeQuery("SELECT name, value FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE type = 6;");
        try {
            while (result.next()) {
                map.put(result.getString(1), Float.parseFloat(result.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

}
