# ExplosionAPI

## Introduction

This API can be used without dropping it to the server. You only need to shadow/shade it into your Jar.
Dropping the API into to plugin folder will **NOT** work.
Gradle: https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow

## Gradle Implementation

```
    plugins {
        id 'java'
        id "com.github.johnrengelman.shadow" version "7.1.2"
    }

	repositories {
	    maven { url 'https://jitpack.io' }
	}
		
	dependencies {
	    implementation 'com.github.TheCurano:ExplosionAPI:1.0.4'
	}
```

## Maven Implementation

    ```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

	<dependency>
	    <groupId>com.github.TheCurano</groupId>
	    <artifactId>ExplosionAPI</artifactId>
	    <version>1.0.4</version>
	</dependency>
    ```

## Register

```Java
public final class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register your Plugin in the API
        ExplosionAPI.register(this);
    }

    @Override
    public void onDisable() {
        // Unregister your Plugin in the API
        ExplosionAPI.unregister(this);
    }
}
```

## Commands

```Java
// Only the name has to be defined
@ECommand(name = "test", aliases = {"t", "testing"}, description = "Test command", permission = "test.command", permissionMessage = "Permission Message", usage = "/test")
public class TestCommand extends Command {

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return super.tabComplete(sender, alias, args);
    }

    // Forced
    public TestCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return true;
    }
}

```

## Events

```Java

@Events
public class Events implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        event.setCancelled(true);
    }

}
```

## Items

```Java
public class Test {
    public void function(Player player) {
        EItemStack item = new EItemStack(Material.STONE, "Name")
                .setLore(List.of("Zeile 1", "Zeile 2"))
                .setInventoryClickEvent(event -> {
                    event.setCancelled(true);
                }, Timing.TEMPORARY);
        item.setPlayerInteractEvent(event -> {
            event.setCancelled(true);
        }, TIMIING.PERMANENTLY);
        player.getInventory().addItem(item);
    }
}
```

## InventoryBuilder
### Be careful, Event Functions will be deleted after reloads / restarts.
```Java
public class Test {
    public void function(Player player) {
        InventoryBuilder builder = new InventoryBuilder(9, "Test");
        builder.setTitle("Titel");
        builder.setInventoryClickEvent(event -> {
            event.setCancelled(true);
        });
        player.openInventory(builder.build());
    }
}
```

## Base64Converter

### (Object to String / String to Object)

### Know that only Objects which implements Serializable are supported!

### Items have there own function, because they need a specific handling.

```Java
public class Test {
    public void function(Object object, ItemStack itemStack) {
        String base64Object = Base64Converter.toString(object);
        String base64Item = Base64Converter.fullItemStackToString(itemStack);
        Object oldObject = Base64Converter.fromString(base64Object);
        ItemStack oldItem = Base64Converter.fromStringToItemStack(base64Item);
    }
}
```

## Database

```Java
public class Test {
    public void function() {
        Database database = new Database(DatabaseType.MARIADB_CONNECTION, "IP", 3306, "database_name", "username", "P@ssw0rd");
        DBManager dbManager = database.getDBManager();
        dbManager.createTable("test(id int, name varchar(255))");
        DBList list = dbManager.createList("test1");
        list.set("Test", "test");
        String test = list.getString("Test");
    }
}
```

## SidebarScoreboard

```Java
public class Test {
    public void function(Player player) {
        SidebarScoreboard sidebarScoreboard = new SidebarScoreboard(player);
        sidebarScoreboard.setTitle("Title");
        sidebarScoreboard.set("Line 1", "Line 2", "Line 3");
        sidebarScoreboard.set("Line 1", "Line 3", "Line 2");
    }
}
```
