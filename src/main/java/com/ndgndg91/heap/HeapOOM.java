package com.ndgndg91.heap;

import java.util.ArrayList;

/**
 * VM 매개 변수: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {
    static class OOMObject {}

    /**
     * > Task :HeapOOM.main() FAILED
     * java.lang.OutOfMemoryError: Java heap space
     * Dumping heap to java_pid1635.hprof ...
     * Heap dump file created [30150136 bytes in 0.086 secs]
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * 	at java.base/java.util.Arrays.copyOf(Arrays.java:3513)
     * 	at java.base/java.util.Arrays.copyOf(Arrays.java:3482)
     * 	at java.base/java.util.ArrayList.grow(ArrayList.java:237)
     * 	at java.base/java.util.ArrayList.grow(ArrayList.java:244)
     * 	at java.base/java.util.ArrayList.add(ArrayList.java:483)
     * 	at java.base/java.util.ArrayList.add(ArrayList.java:496)
     * 	at com.ndgndg91.HeapOOM.main(HeapOOM.java:14)
     */
    public static void main(String[] args) {
        var list = new ArrayList<OOMObject>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
