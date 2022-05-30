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