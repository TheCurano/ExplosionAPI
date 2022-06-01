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
@ECommand(name = "test", aliases = {"t", "testing"}, description = "Test command", permission = "test.command", permissionMessage = "Permission Message", usage = "/test")
public class TestCommand extends Command {

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return super.tabComplete(sender, alias, args);
    }

    // Verpflichtend
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