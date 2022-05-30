package de.curano.explosionapi.annotations;

import io.github.classgraph.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

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
                                Objects.requireNonNull(plugin.getCommand(command.name())).setExecutor((CommandExecutor) clazz.getConstructor().newInstance());
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
                            Events events = clazz.getAnnotation(Events.class);
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
