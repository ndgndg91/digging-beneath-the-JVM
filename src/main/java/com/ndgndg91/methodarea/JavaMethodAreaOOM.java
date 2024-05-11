package com.ndgndg91.methodarea;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * VM ARG : (JDK 7 이하) -XX:PermSize=10M -XX:MaxPermSize=10M
 * VM ARG : (JDK 8 이상) -XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M
 * 아래 예외 발생 시 추가 --add-opens java.base/java.lang=ALL-UNNAMED
 * Caused by: net.sf.cglib.core.CodeGenerationException: java.lang.reflect.InaccessibleObjectException-->Unable to make protected final java.lang.Class java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain) throws java.lang.ClassFormatError accessible: module java.base does not "opens java.lang" to unnamed module @3359c0cf
 */
public class JavaMethodAreaOOM {
    static class OOMObject {}

    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
     * Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "main"
     */
    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> methodProxy.invokeSuper(o, objects));
            enhancer.create();
        }
    }
}
