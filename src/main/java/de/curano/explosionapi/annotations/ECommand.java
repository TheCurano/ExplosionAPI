package de.curano.explosionapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ECommand {

    String name() default "";
    String[] aliases() default {};
    String description() default "";
    String label() default "";
    String permission() default "";
    String permissionMessage() default "";
    String usage() default "";

}
