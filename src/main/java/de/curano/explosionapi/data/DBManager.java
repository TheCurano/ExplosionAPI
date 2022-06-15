package de.curano.explosionapi.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager {

    private Database database;

    public DBManager(Database database) {
        if (database.getType() != DatabaseType.MARIADB_CONNECTION && database.getType() != DatabaseType.MARIADB_POOL) {
            throw new IllegalArgumentException("Database type must be MARIADB_CONNECTION or MARIADB_POOL");
        }
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    /**
     * Create Tables if they don't exist
     * Syntax: tablesname(name type, ...)
     * Example: test(id int, name varchar(255))
     * @param tables
     * @return instance
     */
    public DBManager createTable(String... tables) {
        for (String table : tables) {
            this.database.execute("CREATE TABLE IF NOT EXISTS " + database.getDatabaseName() + "." + table + ";");
        }
        return this;
    }

    public DBManager dropTable(String... tables) {
        for (String table : tables) {
            this.database.execute("DROP TABLE IF EXISTS " + database.getDatabaseName() + "." + table + ";");
        }
        return this;
    }

    public boolean tableExists (String table) {
        ResultSet result = this.database.executeQuery("SELECT count(*) FROM information_schema.TABLES WHERE TABLE_NAME = '" + table + "' AND TABLE_SCHEMA in (SELECT DATABASE());");
        try {
            return result.next() && result.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public DBList createList(String name) {
        return new DBList(this, name);
    }

    public DBList getList(String name) {
        if (this.tableExists(name + "(name VARCHAR(256), type BIT, value VARCHAR(4096))")) {
            return new DBList(this, name);
        }
        return null;
    }

}
