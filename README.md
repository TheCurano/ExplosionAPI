# ExplosionAPI

## Introduction

This API can be used without dropping it to the server. You only need to shadow it into your Jar.
It can also be dropped to the server and used there without shadowing it.

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

## Items

```Java
public class Test {
    public void function(Player player) {
        EItemStack item = new EItemStack(Material.STONE, "Name")
                .setLore(List.of("Zeile 1", "Zeile 2"))
                .setInventoryClickEvent(event -> {
                    event.setCancelled(true);
                });
        item.setPlayerInteractEvent(event -> {
            event.setCancelled(true);
        });
        player.getInventory().addItem(item);
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