package de.curano.explosionapi.data;

import com.zaxxer.hikari.HikariConfig;

import java.util.Properties;

public class PoolSettings {

    private boolean autoCommit = true;
    private int maxPoolSize = 10;
    private int minIdle = 5;
    private int maxLifetime = 60000;
    private int connectionTimeout = 5000;

    public PoolSettings() { }

    public HikariConfig getConfig(Properties properties) {
        HikariConfig config = new HikariConfig(properties);
        config.setMaximumPoolSize(this.maxPoolSize);
        config.setMinimumIdle(this.minIdle);
        config.setMaxLifetime(this.maxLifetime);
        config.setConnectionTimeout(this.connectionTimeout);
        config.setAutoCommit(this.autoCommit);
        config.setConnectionTestQuery("SELECT 1");
        return config;
    }

    public PoolSettings setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
        return this;
    }

    public PoolSettings setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public PoolSettings setMinIdle(int minIdle) {
        this.minIdle = minIdle;
        return this;
    }

    public PoolSettings setMaxLifetime(int maxLifetime) {
        this.maxLifetime = maxLifetime;
        return this;
    }

    public PoolSettings setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

}
