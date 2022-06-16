package de.curano.explosionapi.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.Properties;
import java.util.function.Consumer;

public class Database {

    private DatabaseType type;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private Connection connection = null;
    private HikariDataSource dataSource = null;
    private PoolSettings poolSettings;

    public Database(DatabaseType type, String host, int port, String database, String username, String password) {
        this(type, host, port, database, username, password, null);
    }

    public Database(DatabaseType type, String host, int port, String database, String username, String password, PoolSettings settings) {
        if (type == DatabaseType.MARIADB_POOL) {
            if (settings == null) {
                this.poolSettings = new PoolSettings();
            }
            this.type = type;
            this.host = host;
            this.port = port;
            this.database = database;
            this.username = username;
            this.password = password;
        } else if (type == DatabaseType.MARIADB_CONNECTION) {
            this.type = type;
            this.host = host;
            this.port = port;
            this.database = database;
            this.username = username;
            this.password = password;
            try {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoreconnect=true", this.username, this.password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setupDataSource(PoolSettings settings) {
        Properties properties = new Properties();
        properties.setProperty("dataSourceClassName", "org.mariadb.jdbc.MariaDbDataSource");
        properties.setProperty("dataSource.serverName", this.host);
        properties.setProperty("dataSource.portNumber", this.port + "");
        properties.setProperty("dataSource.user", this.username);
        properties.setProperty("dataSource.password", this.password);
        properties.setProperty("dataSource.databaseName", this.database);

        HikariConfig config = this.poolSettings.getConfig(properties);
        this.dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() {
        if (type == DatabaseType.MARIADB_POOL) {
            if (dataSource == null) {
                setupDataSource(poolSettings);
            }
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (type == DatabaseType.MARIADB_CONNECTION) {
            return this.connection;
        }
        return null;
    }

    public void closeConnection() {
        if (type == DatabaseType.MARIADB_POOL) {
            if (dataSource != null) {
                dataSource.close();
            }
        } else if (type == DatabaseType.MARIADB_CONNECTION) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isConnected() {
        if (type == DatabaseType.MARIADB_POOL) {
            if (dataSource != null) {
                return dataSource.isRunning();
            }
        } else if (type == DatabaseType.MARIADB_CONNECTION) {
            if (connection != null) {
                try {
                    return !connection.isClosed();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public DatabaseType getType() {
        return type;
    }

    public void execute(String sql) {
        try {
            if (type == DatabaseType.MARIADB_CONNECTION) {
                Connection connection = getConnection();
                connection.createStatement().execute(sql);
            } else if (type == DatabaseType.MARIADB_POOL) {
                if (this.connection != null && !connection.isClosed()) {
                    this.connection.createStatement().execute(sql);
                } else {
                    this.connection = getConnection();
                    this.connection.createStatement().execute(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void execute(String sql, boolean newThread) {
        if (newThread) {
            new Thread(() -> execute(sql)).start();
        } else {
            execute(sql);
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            if (type == DatabaseType.MARIADB_CONNECTION) {
                Connection connection = getConnection();
                return connection.createStatement().executeQuery(sql);
            } else if (type == DatabaseType.MARIADB_POOL) {
                if (this.connection != null && !connection.isClosed()) {
                    return this.connection.createStatement().executeQuery(sql);
                } else {
                    this.connection = getConnection();
                    return this.connection.createStatement().executeQuery(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeQuery(String sql, Consumer<ResultSet> back) {
        new Thread(() -> {
            back.accept(executeQuery(sql));
        }).start();
    }

    public PreparedStatement prepareStatement(String sql) {
        try {
            if (type == DatabaseType.MARIADB_CONNECTION) {
                Connection connection = getConnection();
                return connection.prepareStatement(sql);
            } else if (type == DatabaseType.MARIADB_POOL) {
                if (this.connection != null && !connection.isClosed()) {
                    return this.connection.prepareStatement(sql);
                } else {
                    this.connection = getConnection();
                    return this.connection.prepareStatement(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDatabaseName() {
        return this.database;
    }

    public DBManager getDBManager() {
        return new DBManager(this);
    }
}
