# AngelZCraft-Main-API

## Introduction

This API is only an addition. It's **not** a plugin.

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
// Es muss nur der Name definiert werden
@Command(name = "test", aliases = {"t", "testing"}, description = "Test command", permission = "test.command", permissionMessage = "Permission Message", usage = "/test")
public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

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