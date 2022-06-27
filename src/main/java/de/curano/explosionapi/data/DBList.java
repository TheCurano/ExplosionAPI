package de.curano.explosionapi.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DBList {

    private DBManager dbManager;
    private String listName;

    public DBList(DBManager dbManager, String listName) {
        this.dbManager = dbManager;
        this.listName = listName;
        createIfNotExists();
    }

    /**
     * Types: string 1 | int 2 | long 3 | double 4 | boolean 5 | float 6
     */
    private void createIfNotExists() {
        dbManager.createTable(listName + "(name VARCHAR(256), type BIT, value VARCHAR(4096))");
    }

    public String getString(String name) {
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
        String value = getString(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Integer getIntOrDefault(String name, Integer defaultValue) {
        Integer value = getInt(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Long getLongOrDefault(String name, Long defaultValue) {
        Long value = getLong(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Double getDoubleOrDefault(String name, Double defaultValue) {
        Double value = getDouble(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Boolean getBooleanOrDefault(String name, Boolean defaultValue) {
        Boolean value = getBoolean(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Float getFloatOrDefault(String name, Float defaultValue) {
        Float value = getFloat(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public void removeString(String name) {
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 1;");
    }

    public void removeInt(String name) {
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 2;");
    }

    public void removeLong(String name) {
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 3;");
    }

    public void removeDouble(String name) {
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 4;");
    }

    public void removeBoolean(String name) {
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 5;");
    }

    public void removeFloat(String name) {
        dbManager.getDatabase().execute("DELETE FROM " + dbManager.getDatabase().getDatabaseName() + "." + listName + " WHERE name = '" + name + "' AND type = 6;");
    }

    public void set(String name, String value) {
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO " + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 1, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, int value) {
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO " + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 2, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, long value) {
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO '" + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 3, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, double value) {
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO " + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 4, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, boolean value) {
        if (getString(name) == null) {
            dbManager.getDatabase().execute("INSERT INTO " + dbManager.getDatabase().getDatabaseName() + "." + listName + " (name, type, value) VALUES ('" + name + "', 5, '" + value + "');");
        } else {
            dbManager.getDatabase().execute("UPDATE " + dbManager.getDatabase().getDatabaseName() + "." + listName + " SET value = '" + value + "' WHERE name = '" + name + "';");
        }
    }

    public void set(String name, float value) {
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
