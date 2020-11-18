package com.roylin.rpc.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author roylin
 * @since 2020/10/29 18:46
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface RoylinRpcService {

 String version() default "";
 String className();
}

