package com.ndgndg91.runtimeconstantpool;

import java.util.HashSet;

/**
 * VM ARG : (JDK7 이하) -XX:PermSize=6M -XX:MaxPermSize=6M
 * VM ARG : (JDK8 이상) -XX:MetaspaceSize=6M -XX:MaxMetaspaceSize=6M -Xmx6M
 *
 * JDK8 부터는 런타임 상수 풀이 메서드 영역이 아닌 힙으로 이동하였다. 따라서 JDK8 이상부터는 힙 사이즈를 메타스페이스 영역과 동일하게 설정해야지 에러를 확인할 수 있다.
 */
public class RuntimeConstantPoolOOM_1 {

    /**
     * JDK21 실행
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * 	at java.base/java.util.HashMap.resize(HashMap.java:710)
     * 	at java.base/java.util.HashMap.putVal(HashMap.java:669)
     * 	at java.base/java.util.HashMap.put(HashMap.java:618)
     * 	at java.base/java.util.HashSet.add(HashSet.java:229)
     * 	at com.ndgndg91.runtimeconstantpool.RuntimeConstantPoolOOM_1.main(RuntimeConstantPoolOOM_1.java:18)
     */
    public static void main(String[] args) {
        // full GC 가 상수 풀을 회수하지 못하도록 Set 이용해 상수 푸르이 참조를 유지
        var set = new HashSet<String>();
        // short 타입의 범위면 6MB 크기의 영구 세대에서 오버플로를 일으키기 충분함
        short i = 0;

        while (true) {
            set.add(String.valueOf(i++).intern());
        }
    }
}
