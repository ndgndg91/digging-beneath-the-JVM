package com.ndgndg91.chapter2.stack;

/**
 * VM ARG : -Xss180k
 */
public class JavaVMStackOverFlow_1 {
    private int stackLength = 1;
    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    /**
     * > Task :JavaVMStackOverFlow_1.main() FAILED
     * stack length : 883
     * Exception in thread "main" java.lang.StackOverflowError
     * 	at com.ndgndg91.JavaVMStackStackOverFlow_1.stackLeak(JavaVMStackStackOverFlow_1.java:9)
     * 	at com.ndgndg91.JavaVMStackStackOverFlow_1.stackLeak(JavaVMStackStackOverFlow_1.java:10)
     * 	at com.ndgndg91.JavaVMStackStackOverFlow_1.stackLeak(JavaVMStackStackOverFlow_1.java:10)
     * 	... 생략
     */
    public static void main(String[] args) {
        var oom = new JavaVMStackOverFlow_1();
        try {
            oom.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length : " + oom.stackLength);
            throw e;
        }
    }
}
