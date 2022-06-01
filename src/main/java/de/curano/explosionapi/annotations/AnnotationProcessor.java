package de.curano.explosionapi.annotations;

import io.github.classgraph.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationProcessor {

    public static void processRegister(JavaPlugin plugin) {
        String packageName = plugin.getClass().getPackage().getName();
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(packageName).scan()) {
            ClassInfoList classInfos = scanResult.getAllClasses();
            for (ClassInfo classInfo : classInfos) {
                try {
                    if (classInfo.hasAnnotation(ECommand.class)) {
                        Class<?> clazz = classInfo.loadClass();

                        int constructorLen = -1;
                        Constructor<?> constructor = null;
                        for (Constructor<?> constr : clazz.getConstructors()) {
                            constructorLen = constr.getParameterCount();
                            if (constructorLen == 1 && constr.getParameterTypes()[0].equals(String.class)) {
                                constructor = constr;
                                break;
                            }
                        }

                        if (clazz.getSuperclass().equals(Command.class) && constructorLen == 1) {
                            ECommand command = clazz.getAnnotation(ECommand.class);
                            if (!command.name().equals("")) {
                                org.bukkit.command.Command pluginCommand = (org.bukkit.command.Command) constructor.newInstance(command.name());
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
                                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

                                bukkitCommandMap.setAccessible(true);
                                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
                                commandMap.register(command.name(), pluginCommand);
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
