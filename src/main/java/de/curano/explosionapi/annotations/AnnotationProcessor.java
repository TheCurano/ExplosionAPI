package de.curano.explosionapi.annotations;

import io.github.classgraph.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationProcessor {

    public static void processRegister(JavaPlugin plugin) {
        String packageName = plugin.getClass().getPackage().getName();
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(packageName).scan()) {
            ClassInfoList classInfos = scanResult.getAllClasses();
            for (ClassInfo classInfo : classInfos) {
                try {
                    if (classInfo.hasAnnotation(Command.class)) {
                        Class<?> clazz = classInfo.loadClass();
                        if (Arrays.stream(clazz.getInterfaces()).toList().contains(CommandExecutor.class)) {
                            Command command = clazz.getAnnotation(Command.class);
                            if (!command.name().equals("")) {
                                PluginCommand pluginCommand = plugin.getCommand(command.name());
                                pluginCommand.setExecutor((CommandExecutor) clazz.getConstructor().newInstance());
                                if (command.aliases().length != 0) {
                                    pluginCommand.setAliases(Arrays.asList(command.aliases()));
                                }
                                if (!command.description().equals("")) {
                                    pluginCommand.setDescription(command.description());
                                }
                                if (!command.label().equals("")) {
                                    pluginCommand.setLabel(command.label());
                                }
                                if (!command.permission().equals("")) {
                                    pluginCommand.setUsage(command.permission());
                                }
                                if (!command.permissionMessage().equals("")) {
                                    pluginCommand.setUsage(command.permissionMessage());
                                }
                                if (!command.usage().equals("")) {
                                    pluginCommand.setUsage(command.usage());
                                }
                            }
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                try {
                    if (classInfo.hasAnnotation(Events.class)) {
                        Class<?> clazz = classInfo.loadClass();
                        if (Arrays.stream(clazz.getInterfaces()).toList().contains(Listener.class)) {
                            Bukkit.getPluginManager().registerEvents((Listener) clazz.getConstructor().newInstance(), plugin);
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                for (MethodInfo methodInfo : classInfo.getMethodInfo()) {
                    try {
                        if (methodInfo.hasAnnotation(Trigger.class.getName()) && methodInfo.isPublic()) {
                            Method method = methodInfo.loadClassAndGetMethod();
                            Trigger annotation = method.getAnnotation(Trigger.class);
                            if (method.getParameterCount() == 0 && method.getReturnType() == void.class && annotation.type().equals(TriggerType.REGISTER)) {
                                method.invoke(null);
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void processUnregister(JavaPlugin plugin) {
        String packageName = plugin.getClass().getPackage().getName();
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(packageName).scan()) {
            ClassInfoList classInfos = scanResult.getAllClasses();
            for (ClassInfo classInfo : classInfos) {
                for (MethodInfo methodInfo : classInfo.getMethodInfo()) {
                    try {
                        if (methodInfo.hasAnnotation(Trigger.class.getName()) && methodInfo.isPublic() && methodInfo.isStatic()) {
                            Method method = methodInfo.loadClassAndGetMethod();
                            Trigger annotation = method.getAnnotation(Trigger.class);
                            if (method.getParameterCount() == 0 && method.getReturnType() == void.class && annotation.type().equals(TriggerType.UNREGISTER)) {
                                method.invoke(null);
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
